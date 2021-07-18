package Model;

import java.io.IOException;

public abstract class GameObj {
    public int type;
    public int posX;
    public int posY;

    public GameObj oldObj;
    public Vuoto vuoto;
    public int direzione = 1;
    public void moveUp() {
        /*if (posY - 1 >= 0 && DonkeYGame.getInstance().gameTable[posX][posY+1].type != DonkeYGame.FERRO &&
                DonkeYGame.getInstance().gameTable[posX][posY-1].type != DonkeYGame.FERRO) */
        if (posY -1 >= 0 && DonkeYGame.getInstance().worldTable[posX][posY-1].type!=DonkeYGame.FERRO){
            posY--;
            oldObj = DonkeYGame.getInstance().gameTable[posX][posY];
            DonkeYGame.getInstance().swap(posX, posY+1);
        }
    }

    ;

    public void moveDown() {
        if (posY + 1 < DonkeYGame.getInstance().dimension-2) {
            posY++;
            oldObj = DonkeYGame.getInstance().gameTable[posX][posY];
            DonkeYGame.getInstance().swap(posX, posY - 1);
        }
    }

    ;

    public void moveLeft() {
        if (posX - 1 >= 0) {
            direzione=-1;
            posX--;
            oldObj = DonkeYGame.getInstance().gameTable[posX][posY];
            DonkeYGame.getInstance().swap(posX + 1, posY);
        }
    }

    ;

    public void moveRight() {
        if (posX + 1 < DonkeYGame.getInstance().dimension-1) {
            direzione=1;
            posX++;
            oldObj = DonkeYGame.getInstance().gameTable[posX][posY];
            DonkeYGame.getInstance().swap(posX - 1, posY);
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
