package View;

import Controller.MovementController;
import Model.DonkeYGame;
import Model.GameObj;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Graphics extends JPanel {

    MovementController mc;
    GameObj[][] grid;
    GameObj[][] worldMap;

    private static Graphics graphics = null;

    private Graphics() {
        grid = DonkeYGame.getInstance().gameTable;
        worldMap = DonkeYGame.getInstance().worldTable;
        grabFocus();
    }

    public static Graphics getInstance(){
        if (graphics == null){
            graphics = new Graphics();
        }
        return graphics;
    }
    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j].type == DonkeYGame.PLAYER)
                    g.setColor(Color.BLUE);
                else if (worldMap[i][j].type == DonkeYGame.FERRO)
                    g.setColor(Color.RED);
                else if (worldMap[i][j].type == DonkeYGame.VUOTO)
                    g.setColor(Color.BLACK);
                else if (worldMap[i][j].type == DonkeYGame.LADDER)
                    g.setColor(Color.GREEN);

                g.fillRect(i * 20, j * 20, 20, 20);
            }
        }
    }

}
