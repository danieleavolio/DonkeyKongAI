package Model;

public class Player extends GameObj {

    int jumpingPos;
    public boolean canClimb = false;
    boolean isJumping = false;

    public Player() {
        type = DonkeYGame.PLAYER;
        oldObj = new Vuoto();
        posX = 0;
        posY = 35;
    }

    public void jump() {
        if (DonkeYGame.getInstance().worldTable[posX][posY + 1].type != DonkeYGame.VUOTO && !isJumping || posY + 1 >= DonkeYGame.dimension - 2) {
            isJumping = true;
            jumpingPos = posY;
            moveUp();
        }
    }


}

