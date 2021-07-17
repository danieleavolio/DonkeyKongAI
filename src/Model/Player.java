package Model;

public class Player extends GameObj {

    int jumpingPos;
    public boolean canClimb;
    boolean isJumping;

    public Player() {
        type = DonkeYGame.PLAYER;
        oldObj = new Vuoto();
        posX = 1;
        posY = 35;
        isJumping = false;
        canClimb = false;
        jumpingPos=35;
    }

    public void jump() {
        if (DonkeYGame.getInstance().worldTable[posX][posY + 1].type != DonkeYGame.VUOTO && !isJumping || posY + 1 >= DonkeYGame.dimension - 2) {
            isJumping = true;
            jumpingPos = posY;
            moveUp();
        }
    }


}

