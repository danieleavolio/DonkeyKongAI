package Model;

public class Barile extends GameObj {
    int direzione;
    public boolean falling;
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

    /**
     * Gestisce il movimento del singolo barile, in base alla direzione in cui deve andare.
     * @param index indica il barile presente in un array di barili
     */
    public void muoviBarile(int index){

        if (this.posX-1 < 0)
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

    /**
     * In base a dove si trova, lo sposta a sinistra scambiando il valore della matrice gameTable per non avere problemi.
     * @param index è l'indice del barile in un array di barili
     */
    public void moveLeftBarrel(int index) {
        if (posX - 1 >= 0) {
            posX--;
            oldObj = DonkeYGame.getInstance().gameTable[posX][posY];
            DonkeYGame.getInstance().swapBarile(posX + 1, posY, index);
        }
    }
    /**
     * In base a dove si trova, lo sposta a destra scambiando il valore della matrice gameTable per non avere problemi.
     * @param index è l'indice del barile in un array di barili
     */
    public void moveRightBarrel(int index) {
        if (posX + 1 < DonkeYGame.getInstance().dimension-1) {
            posX++;
            oldObj = DonkeYGame.getInstance().gameTable[posX][posY];
            DonkeYGame.getInstance().swapBarile(posX - 1, posY, index);
        }
    }
    /**
     * In base a dove si trova, lo sposta giù scambiando il valore della matrice gameTable per non avere problemi.
     * @param index è l'indice del barile in un array di barili
     */
    public void moveDownBarrel(int index){
        if (posY + 1 < DonkeYGame.getInstance().dimension-2) {
            posY++;
            oldObj = DonkeYGame.getInstance().gameTable[posX][posY];
            DonkeYGame.getInstance().swapBarile(posX, posY - 1,index);
        }
    }




}
