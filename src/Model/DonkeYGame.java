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
import java.util.Timer;

public  class DonkeYGame implements Runnable {

    private static DonkeYGame donkey = null;

    public GameObj[][] gameTable;
    public GameObj[][] worldTable;
    public final static int PLAYER = 1;
    public final static int BARREL = 2;
    public final static int PRINCESS = 3;
    public final static int FERRO = 4;
    public final static int LADDER = 5;
    public final static int VUOTO = 6;
    public boolean running = true;
    public Player player;
    public Ferro ferro;
    public Vuoto vuoto;
    public Scala scala;
    public int vinto = 0;
    public ArrayList<Barile> barili = new ArrayList<Barile>();

    public final static int dimension = Main.DIM/20;

    public void makeTable() {
        vuoto = new Vuoto();
        player = new Player();
        ferro = new Ferro();
        scala = new Scala();
        barili = new ArrayList<>();
        gameTable = new GameObj[dimension][dimension];
        worldTable = new GameObj[dimension][dimension];

        for (int i = 0; i < gameTable.length; i++) {
            for (int i1 = 0; i1 < gameTable.length; i1++) {
                gameTable[i1][i] = vuoto ;
                worldTable[i1][i] = vuoto;
            }
        }


        // GENERAZIONE MAPPA A MANO CHE DA FILE MI DAVA ERRORI

        //generazione dei ferri
        //ULTIO PIANO
        for (int i = 16; i < gameTable.length; i++)
            worldTable[i][12] = ferro;

       /* for (int i = 15; i < 25; i++)
            worldTable[i][14] = ferro;*/
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
    }

    public void generaBarili(int contatore){

        if (contatore == 0) {
            Barile barilone = new Barile(34,10);
            barili.add(barilone);
            gameTable[34][10] = barilone;
        }
    }

    public void checkDeath(){

        for (int i = 0; i < barili.size(); i++) {
            //SE NON SONO AI BORDI
            if (player.posX+1 <= gameTable.length & player.posX-1 >= 0 &&player.posY+1 <= gameTable.length & player.posY-1 >= 0  )
                //SE HO UN BARILE A DESTRA O SINISTRA O SU O GIU E NON SONO SU UNA SCALA ALTRIMENTI SAREBBE MORTE CERTA, RICOMINCIA
                if ((gameTable[player.posX+1][player.posY] == barili.get(i) || gameTable[player.posX-1][player.posY] == barili.get(i) ||
                        gameTable[player.posX][player.posY+1] == barili.get(i) || gameTable[player.posX][player.posY-1] == barili.get(i) )
                        && worldTable[player.posX][player.posY].type!=LADDER){
                    gameTable[player.posX][player.posY] = vuoto;
                    vinto = 1;
                    Graphics.getInstance().reset();
                }
        }
    }
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
    
    public synchronized void distruzioneBarili(){
        for (int i = 0; i < barili.size(); i++) {
            if (barili.get(i).posX-2<=0 && barili.get(i).posY>=34){
                gameTable[barili.get(i).posX][barili.get(i).posY] = vuoto;
                barili.remove(i);
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
        while(running){
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(player.posX + " -- " + player.posY);
            if (contatore>=50)
                contatore=0;
            generaBarili(contatore);
            contatore++;
            checkDeath();
            checkWin();
            for (int i = 0; i < barili.size(); i++) {
                barili.get(i).muoviBarile(i);
            }
            //GRAVITA' BARILI
            gravitaBarili();
            //PLAYER JUMP

            if (!player.isJumping){
                if (worldTable[player.posX][player.posY+1].type != FERRO && worldTable[player.posX][player.posY+1].type != LADDER) {
                    player.moveDown();
                }
            }
            if (player.isJumping ){
                player.moveUp();
                player.moveUp();
                swap(player.posX,player.posY+1);
            }
            if (player.jumpingPos - 1 > player.posY){
                player.isJumping = false;
            }



            for (int i = 0; i < gameTable.length; i++) {
                for (int j = 0; j < gameTable.length; j++) {
                    if (player.posY - 1 >= 0) {
                        player.canClimb = gameTable[player.posX][player.posY - 1].type == LADDER;
                        break;
                    }
                }
            }
            MovementController.getInstance().update();
            Graphics.getInstance().repaint();
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


}
