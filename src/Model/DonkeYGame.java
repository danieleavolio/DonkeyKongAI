package Model;

import Controller.MovementController;
import View.Graphics;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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

    public Player player;
    public Ferro ferro;
    public Vuoto vuoto;
    public Scala scala;
    public final static int dimension = Main.DIM/20;

    public void makeTable() {
        vuoto = new Vuoto();
        player = new Player();
        ferro = new Ferro();
        scala = new Scala();
        gameTable = new GameObj[dimension][dimension];
        worldTable = new GameObj[dimension][dimension];

        for (int i = 0; i < gameTable.length; i++) {
            for (int i1 = 0; i1 < gameTable.length; i1++) {
                gameTable[i1][i] = vuoto ;
                worldTable[i1][i] = vuoto;


            }
        }

        // GENERAZIONE MAPPA A MANO CHE DA FILE MI DAVA ERRORI
        for (int i = 20; i < gameTable.length; i++)
            worldTable[i][12] = ferro;

        for (int i = 15; i < 25; i++)
            worldTable[i][14] = ferro;

        for (int i = 0; i < gameTable.length - 15; i++)
            worldTable[i][16] = ferro;
        for (int i = 0; i < gameTable.length - 20; i++)
            worldTable[i][22] = ferro;
        for (int i = 10; i < gameTable.length ; i++)
            worldTable[i][26] = ferro;
        for (int i = 0; i < gameTable.length - 10; i++)
            worldTable[i][32] = ferro;
        for (int i = 0; i < gameTable.length; i++)
            worldTable[i][37] = ferro;

        for (int i = 32; i < 37; i++){
            worldTable[30][i] = scala;
        }

        for (int i = 26; i < 32; i++){
            worldTable[10][i] = scala;
        }

        for (int i = 22; i < 26; i++){
            worldTable[20][i] = scala;
        }
        for (int i = 16; i < 22; i++){
            worldTable[5][i] = scala;
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



    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //PLAYER JUMP

            if (!player.isJumping){
                if (worldTable[player.posX][player.posY+1].type != FERRO && worldTable[player.posX][player.posY+1].type != LADDER)
                    player.moveDown();
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
        }
    }
}
