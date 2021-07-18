package View;

import Controller.MovementController;
import Model.Barile;
import Model.DonkeYGame;
import Model.GameObj;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferDouble;
import java.io.File;
import java.io.IOException;

public class Graphics extends JPanel {

    MovementController mc;
    GameObj[][] grid;
    GameObj[][] worldMap;
    BufferedImage mario;
    BufferedImage mattone;
    BufferedImage scala;
    BufferedImage barileCaduta;
    BufferedImage barileRotola;
    BufferedImage sfondo;
    BufferedImage vittoria;
    BufferedImage sconfitta;
    private static Graphics graphics = null;

    private Graphics() {
        grid = DonkeYGame.getInstance().gameTable;
        worldMap = DonkeYGame.getInstance().worldTable;
        grabFocus();
        this.setBackground(Color.BLACK);
        try {
            mario = ImageIO.read(new File("src/View/Sprites/mario.png"));
            mattone = ImageIO.read(new File("src/View/Sprites/ferro.png"));
            scala = ImageIO.read(new File("src/View/Sprites/scala.png"));
            barileCaduta = ImageIO.read(new File("src/View/Sprites/barileCaduta.png"));
            barileRotola = ImageIO.read(new File("src/View/Sprites/barileRotola.png"));
            sfondo = ImageIO.read(new File("src/View/Sprites/sfondo.png"));
            vittoria = ImageIO.read(new File("src/View/Sprites/vittoria.png"));
            sconfitta = ImageIO.read(new File("src/View/Sprites/sconfitta.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawSfondo(java.awt.Graphics g){
        g.drawImage(sfondo,0,0,800,800,null);
    }

    public static Graphics getInstance(){
        if (graphics == null){
            graphics = new Graphics();
        }
        return graphics;
    }

    public void reset(){
        grid = DonkeYGame.getInstance().gameTable;
        worldMap = DonkeYGame.getInstance().worldTable;
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        drawSfondo(g);

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j].type == DonkeYGame.PLAYER) {
                    //SE LA DIREZIONE E' 1 VA A DESTRA
                    if (DonkeYGame.getInstance().player.direzione == 1)
                        g.drawImage(mario,DonkeYGame.getInstance().player.posX*20,DonkeYGame.getInstance().player.posY*20,20,20,null);
                    else
                        //SE LA DIREZIONE E' -1 ALLORA SOMMO LA DIMENSIONE ALLA POSIZIONE PER RIPOSIZIONARE LA GRAFICA IN MODO CORRETTO
                        g.drawImage(mario,(DonkeYGame.getInstance().player.posX*20)+20,DonkeYGame.getInstance().player.posY*20,20*(-1),20,null);

                }
                //DISEGNA IL MATTONE
                else if (worldMap[i][j].type == DonkeYGame.FERRO)
                    g.drawImage(mattone,i*20,j*20,20,20,null);
                /*else if (worldMap[i][j].type == DonkeYGame.VUOTO)
                    g.drawImage(napoli,0,0,1000,1000,null);*/
                //DISEGNA LA SCALA
                else if (worldMap[i][j].type == DonkeYGame.LADDER)
                    g.drawImage(scala,i*20,j*20,20,20,null);
                //g.drawRect(i * 20, j * 20, 20, 20);

            }
        }
        //SCORRIMENTO DEI BARILI PER AVERE LA POSIZIONE CORRETTA E SAPERE SE STANNO CADENDO
        for (Barile barile : DonkeYGame.getInstance().barili) {
            if (barile.falling){
                g.drawImage(barileCaduta, barile.posX*20, barile.posY*20, 20,20, null);
            }
            else{
                g.drawImage(barileRotola, barile.posX*20, barile.posY*20, 20,20, null);
            }
        }

        if (DonkeYGame.getInstance().vinto == 1){
            g.drawImage(sconfitta,0,0,800,800,null);
        }
        else if(DonkeYGame.getInstance().vinto == 2){
            g.drawImage(vittoria,0,0,800,800,null);

        }

    }

}
