package Model;

import Controller.MovementController;
import View.Graphics;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.BindException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.ThreadLocalRandom;

public  class DonkeYGame implements Runnable {

    private static DonkeYGame donkey = null;

    public GameObj[][] gameTable;
    public GameObj[][] worldTable;
    public final static int PLAYER = 1;
    public final static int BARREL = 2;
    public final static int FERRO = 4;
    public final static int LADDER = 5;
    public final static int VUOTO = 6;
    public boolean intervaloCambiato = true;
    public int [] intervalli;
    public Player player;
    public Ferro ferro;
    public Vuoto vuoto;
    public Scala scala;
    public int vinto = 0;
    public ArrayList<Barile> barili = new ArrayList<Barile>();
    public Random random;
    public LogicProgram logicProgram;
    public String encoding = "encoding/regoleLogiche.txt";

    public final static int dimension = Main.DIM/20;

    public void makeTable() {
        vuoto = new Vuoto();
        player = new Player();
        ferro = new Ferro();
        scala = new Scala();
        barili = new ArrayList<>();
        gameTable = new GameObj[dimension][dimension];
        worldTable = new GameObj[dimension][dimension];
        intervalli = new int [3];

        intervalli[0] = 30;
        intervalli[1] = 50;
        intervalli[2] = 70;

        for (int i = 0; i < gameTable.length; i++) {
            for (int i1 = 0; i1 < gameTable.length; i1++) {
                gameTable[i1][i] = vuoto ;
                worldTable[i1][i] = vuoto;
            }
        }


        // GENERAZIONE MAPPA A MANO CHE DA FILE MI DAVA ERRORI
        //generazione dei ferri
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

        //generazione della scala
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

    public static DonkeYGame getInstance(){
        if (donkey == null){
            donkey = new DonkeYGame();
        }
        return donkey;
    }
    private DonkeYGame() {
        makeTable();
        random = new Random();
        logicProgram = new LogicProgram(encoding);
    }

    public void generaBarili(int contatore){
        if (contatore == 0) {
            int posizione = random.nextInt(2);
            if (posizione==0) {
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

    public void checkDeath(){

        for (int i = 0; i < barili.size(); i++) {
            //SE NON SONO AI BORDI
            if (player.posX+1 <= gameTable.length & player.posX-1 >= 0 &&player.posY+1 <= gameTable.length & player.posY-1 >= 0  )
                //SE HO UN BARILE A DESTRA O SINISTRA O SU O GIU E NON SONO SU UNA SCALA ALTRIMENTI SAREBBE MORTE CERTA, RICOMINCIA
                //evitare di morire wireless
                if (player.posX == barili.get(i).posX && player.posY == barili.get(i).posY) {
                    gameTable[player.posX][player.posY] = vuoto;
                    vinto = 1;
                    Graphics.getInstance().reset();
                }
                /*if ((gameTable[player.posX+1][player.posY] == barili.get(i) || gameTable[player.posX-1][player.posY] == barili.get(i) ||
                        gameTable[player.posX][player.posY+1] == barili.get(i) || gameTable[player.posX][player.posY-1] == barili.get(i) )
                        *//*&& (worldTable[player.posX][player.posY].type!=LADDER && player.posY != barili.get(i).posY-1)*//*)*/

        }
    }

    //lasciare perdere sta cosa funziona e basta
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

    //se sotto i barili non c'è ferro o scala, cadi verticalmente
    public void gravitaBarili() {
        for (int i = 0; i < barili.size(); i++) {
            if (worldTable[barili.get(i).posX][barili.get(i).posY + 1].type != FERRO && worldTable[barili.get(i).posX][barili.get(i).posY + 1].type != LADDER) {
                barili.get(i).falling = true;
                barili.get(i).moveDownBarrel(i);
            }
            else {
                barili.get(i).falling = false;
            }
        }
    }
    //quando un barile arriva all'ultimo bordo sinistro sul livello 1 allora dsitruggilo
    public synchronized void distruzioneBarili(){
        for (int i = 0; i < barili.size(); i++) {
            if (barili.get(i).posX-2<=0 && barili.get(i).posY>=34){
                gameTable[barili.get(i).posX][barili.get(i).posY] = vuoto;
                barili.remove(i);
            }
        }
    }

    //fa muovere i barili sulle scale
    public void bariliSulleScale(){
        int scelta = 0;
        for (int i = 0; i < barili.size(); i++) {
            //se la posizione verticale + 1 è una scala per il barile
            if (worldTable[barili.get(i).posX][barili.get(i).posY+1].type == LADDER){
                //random tra 0 e 1 per capire se scendere le scale o no
                scelta = random.nextInt(2);
                //se il barile non è sulla scala quando sotto ho una scala
                if (!barili.get(i).isOnLadder) {
                    if (scelta == 0)
                        barili.get(i).isOnLadder = true;
                }
                //quando sono sulla scala, se sotto ho una scala scendo
                //quando sono sulla scala e sotto non ho una scala. smetti di essere li  e muoviti
                if (barili.get(i).isOnLadder){
                    if (worldTable[barili.get(i).posX][barili.get(i).posY+1].type==LADDER){
                        barili.get(i).moveDownBarrel(i);
                    }
                    if (worldTable[barili.get(i).posX][barili.get(i).posY+1].type!=LADDER){
                        barili.get(i).isOnLadder = false;
                    }
                }
            }
        }

    }

    public void checkWin(){
        if (player.posX == 38 && player.posY==11){
            vinto = 2;
        }
    }

    @Override
    public void run() {
        int contatore = 0;
        int randomIntervallo;
        while(vinto!=1 && vinto != 2){
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //checkDeath();
            MovementController.getInstance().update();
            Graphics.getInstance().repaint();

            if (player.isJumping){
                player.moveUp();
                //non penso serva
                //swap(player.posX,player.posY+1);
            }

            if (!player.isJumping){
                if (worldTable[player.posX][player.posY+1].type != FERRO && worldTable[player.posX][player.posY+1].type != LADDER) {
                    player.jumpingPos++;
                    player.moveDown();
                }
            }

            if (player.jumpingPos > player.posY){
                player.isJumping = false;
            }



            //facciamo muovere i barili prima
            for (int i = 0; i < barili.size(); i++) {
                if (!barili.get(i).isOnLadder)
                    barili.get(i).muoviBarile(i);
            }
            //GRAVITA' BARILI
            gravitaBarili();
            //PROVA BARILI SULLE SCALE
            bariliSulleScale();
            //PLAYER JUMP
            //aggiungere i fatti
            logicProgram.addFatti(player, barili);
            //gestione del movimento
            handleMovimento();

            randomIntervallo = random.nextInt(3);

            if (contatore>= intervalli[randomIntervallo])
                contatore=0;

            generaBarili(contatore);
            contatore++;
            checkWin();


            for (int i = 0; i < gameTable.length; i++) {
                for (int j = 0; j < gameTable.length; j++) {
                    if (player.posY - 1 >= 0) {
                        player.canClimb = gameTable[player.posX][player.posY - 1].type == LADDER;
                        break;
                    }
                }
            }

            distruzioneBarili();

        }
        /*if (!running){
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            System.exit(0);
                        }
                    },
                    500
            );
        }*/
    }

    public void handleMovimento(){
        Cammina cammina = logicProgram.getAnswerSet();
        //Gestione del movimento totale del personaggio
        if (cammina!=null) {

            if (cammina.getSalta() == 1) {
                //NON RIESCE A PRENDERE L'INPUT IN TEMPO DATI IL TEMPO DI ATTESA
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
