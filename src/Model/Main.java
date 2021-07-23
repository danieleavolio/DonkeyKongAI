package Model;

import Controller.MovementController;
import View.Graphics;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public final static int DIM = 800;
    public static void main(String[] args) throws IOException {

        JFrame frame = new JFrame();
        frame.setSize(DIM,DIM);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setFocusable(true);
        MovementController mc = MovementController.getInstance();
        DonkeYGame game = DonkeYGame.getInstance();
        Graphics gp = Graphics.getInstance();
        frame.addKeyListener(mc);
        frame.add(gp);
        Thread gameLoop = new Thread(game);
        gameLoop.start();

        frame.setVisible(true);
    }
}
