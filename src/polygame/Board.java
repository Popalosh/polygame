package polygame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private Timer timer;
    private Player player;
    private ArrayList<Alien> aliens;
    private boolean ingame;
    private int ICRAFT_X = 40;
    private int ICRAFT_Y = 60;
    private int B_WIDTH = 640;
    private int B_HEIGHT = 480;
    private int DELAY = 7;
    private int dead = 0;
    private int health = 30;
    private int cont = 30;
    private int incAlien = 20;
    private int countOfAlien = 10;


    public Board() {
        this.initBoard();
    }

    private void initBoard() {
        this.addKeyListener(new KeyManager());
        this.setFocusable(true);
        this.setBackground(Color.BLACK);
        this.ingame = true;
        this.setPreferredSize(new Dimension(this.B_WIDTH, this.B_HEIGHT));
        this.player = new Player(this.ICRAFT_X, this.ICRAFT_Y);
        this.initAliens();
        this.timer = new Timer(this.DELAY, this);
        this.timer.start();
    }


    private void initAliens() {
        this.aliens = new ArrayList();

        for (int i = 0; i < this.countOfAlien; ++i) {
            int randX = this.B_WIDTH + (int) (Math.random() * 800.0D);
            int randY = 20 + (int) (Math.random() * 440.0D);
            this.aliens.add(new Alien(randX, randY));
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.ingame) {
            this.drawObjects(g);
        } else {
            this.drawGameOver(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawObjects(Graphics g) {
        if (this.player.isVisible()) {
            g.drawImage(this.player.getImage(), this.player.getX(), this.player.getY(), this);
        }

        ArrayList<Missile> ms = this.player.getMissiles();
        Iterator var3 = ms.iterator();

        while (var3.hasNext()) {
            Missile m = (Missile) var3.next();
            if (m.isVisible()) {
                g.drawImage(m.getImage(), m.getX(), m.getY(), this);
            }
        }

        var3 = this.aliens.iterator();

        while (var3.hasNext()) {
            Alien a = (Alien) var3.next();
            if (a.isVisible()) {
                g.drawImage(a.getImage(), a.getX(), a.getY(), this);
            }
        }

        g.setColor(Color.WHITE);
        g.drawString("Aliens killed: " + this.dead, 5, 15);
        g.drawString("Health: " + this.health, 580, 15);
    }

    private void drawGameOver(Graphics g) {
        String msg = "Game Over";
        String score = "Your score: " + (this.dead - 1);
        Font small = new Font("Helvetica", 1, 14);
        FontMetrics fm = this.getFontMetrics(small);
        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(msg, (this.B_WIDTH - fm.stringWidth(msg)) / 2, this.B_HEIGHT / 2);
        g.drawString(score, (int) ((double) (this.B_WIDTH - fm.stringWidth(msg)) / 2), (int) ((double) this.B_HEIGHT / 1.7D));
    }

    public void actionPerformed(ActionEvent e) {
        this.inGame();
        this.updatePlayer();
        this.updateMissiles();
        this.updateAliens();
        this.increaseHealth();
        this.increaseAlien();
        this.checkCollision();
        this.repaint();
    }

    private void increaseHealth() {
        if (this.dead == this.cont) {
            this.cont += 30;
            this.health += 5;
        }

    }

    private void increaseAlien() {
        if (this.dead == this.incAlien) {
            this.incAlien += 20;
            this.countOfAlien += 5;
        }

    }

    private void inGame() {
        if (!this.ingame) {
            this.timer.stop();
        }
    }

    private void updatePlayer() {
        if (this.player.isVisible()) {
            this.player.getInput();
            this.player.move();
        }
    }

    private void updateMissiles() {
        ArrayList<Missile> ms = this.player.getMissiles();

        for (int i = 0; i < ms.size(); ++i) {
            Missile m = ms.get(i);
            if (m.isVisible()) {
                m.move();
            } else {
                ms.remove(i);
            }
        }
    }

    private void updateAliens() {
        if (this.aliens.isEmpty()) {
            this.initAliens();
        }

        for (int i = 0; i < this.aliens.size(); ++i) {

            Alien a = this.aliens.get(i);

            if (a.getX() < 0) {
                this.aliens.remove(i);
                --this.health;
            } else if (a.isVisible()) {
                a.move();
            } else {
                this.aliens.remove(i);
                ++this.dead;
            }
        }
    }

    private void checkCollision() {
        Rectangle r3 = this.player.getBounds();
        Iterator var2 = this.aliens.iterator();

        while (var2.hasNext()) {
            Alien alien = (Alien) var2.next();
            Rectangle r2 = alien.getBounds();

            if (this.health == 0) {
                this.player.setVisible(false);
                alien.setVisible(false);
                this.ingame = false;
            }

            if (r3.intersects(r2)) {
                this.player.setVisible(false);
                alien.setVisible(false);
                this.ingame = false;
            }
        }

        ArrayList ms = this.player.getMissiles();
        Iterator var10 = ms.iterator();

        while (var10.hasNext()) {
            Missile m = (Missile) var10.next();
            Rectangle r1 = m.getBounds();
            Iterator var6 = this.aliens.iterator();

            while (var6.hasNext()) {
                Alien alien = (Alien) var6.next();
                Rectangle r2 = alien.getBounds();
                if (r1.intersects(r2)) {
                    m.setVisible(false);
                    alien.setVisible(false);
                }
            }
        }
    }
}