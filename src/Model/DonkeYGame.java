package Model;



import View.Graphics;
import java.util.ArrayList;
import java.util.Random;


public  class DonkeYGame implements Runnable {

    private static DonkeYGame donkey = null;

    public GameObj[][] gameTable;
    public GameObj[][] worldTable;
    public final static int PLAYER = 1;
    public final static int BARREL = 2;
    public final static int FERRO = 4;
    public final static int LADDER = 5;
    public final static int VUOTO = 6;
    public int [] intervalli;
    public Player player;
    public Ferro ferro;
    public Vuoto vuoto;
    public Scala scala;
    public int vinto = 0;
    public ArrayList<Barile> barili = new ArrayList<>();
    public Random random;
    public LogicProgram logicProgram;
    public int posizione = 0;
    public String encoding = "encoding/regoleLogiche.txt";

    public final static int dimension = Main.DIM/20;

    /**
     * Metodo usato per creare il mondo di gioco, chiamato nel costruttore
     */
    public void makeTable() {
        vuoto = new Vuoto();
        player = new Player();
        ferro = new Ferro();
        scala = new Scala();
        barili = new ArrayList<>();
        gameTable = new GameObj[dimension][dimension];
        worldTable = new GameObj[dimension][dimension];
        intervalli = new int [3];

        //INTERVALLI DI SPAWN USATI
        intervalli[0] = 35;
        intervalli[2] = 35;
        intervalli[1] = 35;

        //SETTAGGIO DEL MONDO DI GIOCO TUTTO A VUOTO
        for (int i = 0; i < gameTable.length; i++) {
            for (int i1 = 0; i1 < gameTable.length; i1++) {
                gameTable[i1][i] = vuoto ;
                worldTable[i1][i] = vuoto;
            }
        }


        //GENERAZIONE DEL MONDO DI GIOCO

        //ULTIMO PIANO
        for (int i = 16; i < gameTable.length; i++)
            worldTable[i][12] = ferro;

        //PENULTIMO PIANO
        for (int i = 2; i < gameTable.length - 15; i++)
            worldTable[i][16] = ferro;
        //3* PIANO
        for (int i = 0; i < gameTable.length - 14; i++)
            worldTable[i][21] = ferro;
        //2* PIANO
        for (int i = 10; i < gameTable.length ; i++)
            worldTable[i][26] = ferro;
        //1* PIANO
        for (int i = 0; i < gameTable.length - 10; i++)
            worldTable[i][32] = ferro;
        //PIANO TERRA
        for (int i = 0; i < gameTable.length; i++)
            worldTable[i][37] = ferro;

        //GENERAZIONE DELLE SCALE

        //SCALA PIANO TERRA
        for (int i = 32; i < 37; i++){
            worldTable[30][i] = scala;
        }
        //SCALA 2* PIANO
        for (int i = 26; i < 32; i++){
            worldTable[12][i] = scala;
        }
        //SCALA 3* PIANO
        for (int i = 21; i < 26; i++){
            worldTable[24][i] = scala;
        }
        //SCALA 4* PIANO
        for (int i = 16; i < 21; i++){
            worldTable[5][i] = scala;
        }
        //ULTIMA SCALA
        for (int i = 12; i < 16; i++){
            worldTable[22][i] = scala;
        }
    }

    /**
     * Ritorna la classe donkey per gestire il singleton
     * @return la classe
     */
    public static DonkeYGame getInstance(){
        if (donkey == null){
            donkey = new DonkeYGame();
        }
        return donkey;
    }

    /**
     * Costruttore per singleton
     */
    private DonkeYGame() {
        makeTable();
        random = new Random();
        logicProgram = new LogicProgram(encoding);
    }

    /**
     * Se il contatore arriva è 0 ( viene controllato dal thread in run ), i barili vengono spawnati random.
     * @param contatore viene usato per spawnare i barili ad una soglia
     */
    public void generaBarili(int contatore){
        if (contatore == 0) {
            posizione++;
            if (posizione%2==0) {
                Barile barilone = new Barile(34, 10);
                barili.add(barilone);
                gameTable[34][10] = barilone;
            }
            else{
                Barile barilone = new Barile(10, 10);
                barili.add(barilone);
                gameTable[10][10] = barilone;
            }
        }
    }

    /**Gestione della matrice del mondo per quanto riguarda il movimento dei barili.
     * @param oldX è la vecchia posizione di X da cambiare
     * @param oldY è la vecchia posizione di Y da cambiare
     */
    public void swap(int oldX, int oldY){

        switch(player.oldObj.type){
            case FERRO:{
                gameTable[oldX][oldY] = ferro;
                break;
            }
            case LADDER:{
                gameTable[oldX][oldY] = scala;
                break;
            }
            case VUOTO:{
                gameTable[oldX][oldY] = vuoto;
                break;
            }
        }
        gameTable[player.posX][player.posY] = player;
    }


    /** Gestione della matrice del mondo per quanto riguarda il movimento dei barili.
     *
     * @param oldX è la vecchia posizione di X da cambiare
     * @param oldY è la vecchia posizione di Y da cambiare
     * @param index l'indice del barile sul quale si vuole operare
     */
    public void swapBarile(int oldX, int oldY, int index){
        switch(barili.get(index).oldObj.type){
            case FERRO:{
                gameTable[oldX][oldY] = ferro;
                break;
            }
            case LADDER:{
                gameTable[oldX][oldY] = scala;
                break;
            }
            case VUOTO:{
                gameTable[oldX][oldY] = vuoto;
                break;
            }
        }
        gameTable[barili.get(index).posX][barili.get(index).posY] = barili.get(index);

    }


    /**QUANDO UN BARILE ARRIVA ALL'ULTIMO LIVELLO, VIENE DISTRUTTO QUANDO ARRIVA ALLA PARTE DI SINISTRA*/
    public synchronized void distruzioneBarili(){
        for (int i = 0; i < barili.size(); i++) {
            if (barili.get(i).posX-2<=0 && barili.get(i).posY>=34){
                gameTable[barili.get(i).posX][barili.get(i).posY] = vuoto;
                barili.remove(i);
            }
        }
    }


    /**Se il player arriva alla fine, vince*/
    public void checkWin(){
        if (player.posX == 38 && player.posY==11){
            vinto = 2;
            Graphics.getInstance().repaint();
        }
    }

    @Override
    public void run() {
        //CONTATORE: SPAWN DEI BARILI AL RAGGIUNGIMETNO DI UNA SOGLIA
        //RANDOM INTERVALLO: VIENE SCELTO UN INTERVALLO RANDOM DI SPAWND DEI BARILI
        int contatore = 0;
        int randomIntervallo;
        //SE SI VINCE O SI MUORE, SI TERMINA L'ESECUZIONE DEL THREAD
        while(vinto!=1 && vinto != 2){
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //COMANDI DA TASTIERA
            //MovementController.getInstance().update();
            //DISEGNO DELLA GRAFICA
            Graphics.getInstance().repaint();

            //SE IL GIOCATORE SALTA, SI SPOSTA DI 1 IN ALTO
            if (player.isJumping){
                player.moveUp();
            }

            //QUANDO IL PLAYER NON E' SU UNA SCALA, VIENE COLPITO DALLA GRAVITA
            if (!player.isJumping){
                if (worldTable[player.posX][player.posY+1].type != FERRO && worldTable[player.posX][player.posY+1].type != LADDER) {
                    player.jumpingPos++;
                    player.moveDown();
                }
            }

            //QUADNO IL PLAYER ARRIVA AD UNA CERTA DISTANZA DALLA POSIZIONE DEL SALTO, SMETTE DI SALTARE
            if (player.jumpingPos > player.posY){
                player.isJumping = false;
            }

            //GESTIONE DEL TOTALE MOVIMENTO DEI BARILI
            //BRUTTA MA NECESSARIA PER IL FUNZIONAMENTO CORRETTO SENZA ERRORI NON VOLUTI
            for (int i = 0; i < barili.size(); i++) {
                int scelta = random.nextInt(2);
                if (worldTable[barili.get(i).posX][barili.get(i).posY+1].type==FERRO) {
                    barili.get(i).falling = false;
                    barili.get(i).muoviBarile(i);
                }
                else if (worldTable[barili.get(i).posX][barili.get(i).posY+1].type == LADDER){
                    //random tra 0 e 1 per capire se scendere le scale o no
                    //scelta = random.nextInt(2);
                        //se il barile non è sulla scala quando sotto ho una scala
                        if (!barili.get(i).isOnLadder) {
                            if (scelta == 0)
                                barili.get(i).isOnLadder = true;
                            else
                                barili.get(i).muoviBarile(i);
                        }
                        //quando sono sulla scala, se sotto ho una scala scendo
                        //quando sono sulla scala e sotto non ho una scala. smetti di essere li  e muoviti
                        if (barili.get(i).isOnLadder){
                            if (worldTable[barili.get(i).posX][barili.get(i).posY+1].type==LADDER){
                                barili.get(i).moveDownBarrel(i);
                            }
                        }
                        else if (worldTable[barili.get(i).posX][barili.get(i).posY+1].type!=LADDER){
                            barili.get(i).isOnLadder = false;
                        }
                    }
                    else if (worldTable[barili.get(i).posX][barili.get(i).posY + 1].type != FERRO && worldTable[barili.get(i).posX][barili.get(i).posY + 1].type != LADDER) {
                        barili.get(i).falling = true;
                        barili.get(i).moveDownBarrel(i);
                    }
                }

            //AGGIUNZIONE DEI FATTI
            logicProgram.addFatti(player, barili);
            //FUNZIONE PER GESTIRE IL MOVIMENTO IN BASE ALL'AS
            handleMovimento();

            //INTERVALLO RANDOM DI SPAWN DEI BARILI
            randomIntervallo = random.nextInt(2);
            if (contatore>= intervalli[randomIntervallo])
                contatore=0;
            generaBarili(contatore);
            contatore++;

            //CONTROLLO VITTORIA
            checkWin();

            //GESTIONE DELLE SCALE
            for (int i = 0; i < gameTable.length; i++) {
                for (int j = 0; j < gameTable.length; j++) {
                    if (player.posY - 1 >= 0) {
                        player.canClimb = gameTable[player.posX][player.posY - 1].type == LADDER;
                        break;
                    }
                }
            }

            //QUANDO I BARILI ARRIVANO ALL'ULTIMO PIANO VENGONO DISTRUTTI
            distruzioneBarili();

        }

    }

    public void handleMovimento(){
        Cammina cammina = logicProgram.getAnswerSet();
        //In base ALL'AS si muove
        if (cammina!=null) {

            if (cammina.getSalta() == 1) {
                player.jump();
            }
             if (player.getPosX() < cammina.getColonna()) {
                 player.moveRight();
            }
             if (player.getPosX() > cammina.getColonna()) {
                 player.moveLeft();
            }
             if (player.getPosY() < cammina.getRiga()) {
                if(player.isOnLadder)
                    player.moveDown();
            }
             if (player.getPosY() > cammina.getRiga()) {
                if (worldTable[player.getPosX()][player.getPosY()].type==LADDER) {
                    player.moveUp();
                }
            }
        }
    }



}
