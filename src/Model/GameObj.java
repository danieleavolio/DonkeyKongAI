package Model;

import View.Graphics;


public abstract class GameObj {
    public int type;
    public int posX;
    public int posY;

    public GameObj oldObj;
    public int direzione = 1;
    public boolean isOnLadder = false;
    public void moveUp() {
        if (posY -1 >= 0 && DonkeYGame.getInstance().worldTable[posX][posY-1].type!=DonkeYGame.FERRO){
            posY--;
            controlloMortePreciso(DonkeYGame.getInstance().gameTable);
            oldObj = DonkeYGame.getInstance().gameTable[posX][posY];
            DonkeYGame.getInstance().swap(posX, posY+1);
        }
    }

    public void moveDown() {
        if (posY + 1 < DonkeYGame.getInstance().dimension-2) {

            posY++;
            controlloMortePreciso(DonkeYGame.getInstance().gameTable);
            oldObj = DonkeYGame.getInstance().gameTable[posX][posY];
            DonkeYGame.getInstance().swap(posX, posY - 1);
        }
    }

    public void moveLeft() {
        if (posX - 1 >= 0) {
            direzione=-1;
            posX--;
            controlloMortePreciso(DonkeYGame.getInstance().gameTable);
            oldObj = DonkeYGame.getInstance().gameTable[posX][posY];
            DonkeYGame.getInstance().swap(posX + 1, posY);
        }
    }

    public void moveRight() {
        if (posX + 1 < DonkeYGame.getInstance().dimension-1) {
            direzione=1;
            posX++;
            //Controllo per verificare che il barile non si scambi con mario e che mario muoia
            controlloMortePreciso(DonkeYGame.getInstance().gameTable);
            oldObj = DonkeYGame.getInstance().gameTable[posX][posY];
            DonkeYGame.getInstance().swap(posX - 1, posY);
        }
    }

    public void controlloMortePreciso(GameObj[][] table){
        if (table[posX][posY].type == DonkeYGame.BARREL){
            System.out.println(posX +" - "+posY);
            DonkeYGame.getInstance().vinto = 1;
            Graphics.getInstance().repaint();
        }
    }
    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
}
