package Model;

public class Barile extends GameObj {
    int jumpingPos;
    public boolean canClimb = false;
    boolean isJumping = false;
    int direzione = SINISTRA;
    boolean falling = false;
    public static final int DESTRA = 1;
    public static final int SINISTRA = -1;
    public Barile(int posx, int posy) {
        type = DonkeYGame.BARREL;
        oldObj = new Vuoto();
        direzione = SINISTRA;
        this.posX = posx;
        this.posY = posy;
        falling = false;

    }

    public void muoviBarile(int index){

        if (this.posX-1 <= 0)
            this.direzione*=-1;

        if (this.posX+1 >=DonkeYGame.getInstance().gameTable.length-2) {
            this.direzione *= -1;
        }
        if (!falling) {
            if (this.direzione == SINISTRA)
                moveLeftBarrel(index);
            else if (this.direzione == DESTRA)
                moveRightBarrel(index);
        }
    }

    public void moveLeftBarrel(int index) {
        if (posX - 1 >= 0) {
            posX--;
            oldObj = DonkeYGame.getInstance().gameTable[posX][posY];
            DonkeYGame.getInstance().swapBarile(posX + 1, posY, index);
        }
    }

    public void moveRightBarrel(int index) {
        if (posX + 1 < DonkeYGame.getInstance().dimension-1) {
            posX++;
            oldObj = DonkeYGame.getInstance().gameTable[posX][posY];
            DonkeYGame.getInstance().swapBarile(posX - 1, posY, index);
        }
    }

    public void moveDownBarrel(int index){
        if (posY + 1 < DonkeYGame.getInstance().dimension-2) {
            posY++;
            oldObj = DonkeYGame.getInstance().gameTable[posX][posY];
            DonkeYGame.getInstance().swapBarile(posX, posY - 1,index);
        }
    }

}
