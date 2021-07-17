package Controller;

import Model.DonkeYGame;
import Model.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MovementController implements KeyListener {


    Boolean keys[];
    private static MovementController movementController;
    Player player = DonkeYGame.getInstance().player;

    private MovementController() {
        keys = new Boolean[100];
        keys[KeyEvent.VK_RIGHT] = false;
        keys[KeyEvent.VK_LEFT] = false;
        keys[KeyEvent.VK_DOWN] = false;
        keys[KeyEvent.VK_UP] = false;
    }

    public static MovementController getInstance() {
        if (movementController == null) {
            movementController = new MovementController();
        }
        return movementController;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }


    public void update() {
        if (keys[KeyEvent.VK_RIGHT]) {
            if (DonkeYGame.getInstance().worldTable[player.posX + 1][player.posY].type != DonkeYGame.FERRO && player.posX + 1 < DonkeYGame.getInstance().worldTable.length)
                DonkeYGame.getInstance().player.moveRight();

        }
        if (keys[KeyEvent.VK_LEFT]) {
            if (DonkeYGame.getInstance().worldTable[player.posX - 1][player.posY].type != DonkeYGame.FERRO && player.posX-1 > 0)
                DonkeYGame.getInstance().player.moveLeft();

        }
        if (keys[KeyEvent.VK_UP]) {
            if (player.posY - 1 > 0 && DonkeYGame.getInstance().worldTable[player.posX][player.posY-1].type != DonkeYGame.FERRO) {
                if (player.canClimb)
                    player.moveUp();
                DonkeYGame.getInstance().player.jump();
            }
        }
        if (keys[KeyEvent.VK_DOWN]) {
            if (player.canClimb)
                player.moveDown();
            if ((DonkeYGame.getInstance().worldTable[player.posX][player.posY].type == DonkeYGame.LADDER ||
                    DonkeYGame.getInstance().worldTable[player.posX][player.posY+1].type == DonkeYGame.LADDER) && DonkeYGame.getInstance().worldTable[player.posX][player.posY+1].type != DonkeYGame.FERRO )
                DonkeYGame.getInstance().player.moveDown();
        }

    }

}
