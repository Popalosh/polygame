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
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private Timer timer;
    private Player player;
    private ArrayList<Alien> aliens;
    private boolean ingame;
    private final int ICRAFT_X = 60;
    private final int ICRAFT_Y = 60;
    public static final int B_WIDTH = 800;
    private final int B_HEIGHT = 500;
    private final int DELAY = 7;
    private int dead;
    private int health;
    private int cont;
    private int incAlien;
    private int countOfAlien;
    private ArrayList<Explosion> explosions;
    private ArrayList<Counter> counters = new ArrayList<>();

    public Board() {
        this.initBoard();
    }

    private void initBoard() {
        this.addKeyListener(new KeyManager());
        this.setFocusable(true);
        this.setBackground(Color.BLACK);
        this.ingame = true;
        this.setPreferredSize(new Dimension(B_WIDTH, this.B_HEIGHT));
        this.player = new Player(this.ICRAFT_X, this.ICRAFT_Y);
        this.initAliens();
        this.explosions = new ArrayList<>();
        this.timer = new Timer(this.DELAY, this);
        this.timer.start();
        dead = 0;
        health = 30;
        cont = 30;
        incAlien = 20;
        countOfAlien = 10;
    }


    private void initAliens() {

        this.aliens = new ArrayList();

        for (int i = 0; i < this.countOfAlien; ++i) {
            int randX = this.B_WIDTH + (int) (Math.random() * 800);
            int randY = (int) (Math.random() * 440);
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

        var3 = this.explosions.iterator();

        while (var3.hasNext()) {
            Explosion boom = (Explosion) var3.next();
            if (boom.isVisible()) {
                g.drawImage(boom.getImage(), boom.getX(), boom.getY(), this);
            }
        }

        g.setColor(Color.WHITE);
        g.drawString("Aliens killed: " + this.dead, 5, 15);
        g.drawString("Health: " + this.health, 730, 15);
    }

    private void drawGameOver(Graphics g) {
        String msg = "Game Over";
        String res = "Press Enter to restart";
        String score = "Your score: " + (this.dead);
        Font small = new Font("Helvetica", 1, 14);
        FontMetrics fm = this.getFontMetrics(small);
        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(msg, (this.B_WIDTH - fm.stringWidth(msg)) / 2, this.B_HEIGHT / 2);
        g.drawString(res, (this.B_WIDTH - fm.stringWidth(res)) / 2, (int) ((double) this.B_HEIGHT / 1.5D));
        g.drawString(score, (int) ((double) (this.B_WIDTH - fm.stringWidth(res)) / 1.79D), (int) ((double) this.B_HEIGHT / 1.7D));
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

    public void actionPerformed(ActionEvent e) {
        if (!this.ingame) {
            this.restart();
        } else {
            this.updatePlayer();
            this.updateMissiles();
            this.updateAliens();
            this.increaseHealth();
            this.increaseAlien();
            this.checkCollision();
            this.repaint();
            this.updateCounters();
            this.checkExplosions();
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
                if (ingame) {
                    ++this.dead;
                }
            }
        }
    }

    private void checkCollision() {
        Rectangle r3 = this.player.getBounds();

        for (Alien alien : this.aliens) {
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

        for (Object m1 : ms) {
            Missile m = (Missile) m1;
            Rectangle r1 = m.getBounds();

            for (Alien alien : this.aliens) {
                Rectangle r2 = alien.getBounds();
                if (r1.intersects(r2)) {
                    m.setVisible(false);
                    alien.setVisible(false);
                    Explosion boom = new Explosion(alien.getX(), alien.getY());
                    this.counters.add(new Counter());
                    this.explosions.add(boom);
                }
            }
        }
    }

    public void restart() {
        if (KeyManager.getKeys()[KeyEvent.VK_ENTER]) {
            this.timer.stop();
            this.initBoard();
        }
    }

    public void checkExplosions (){
        Iterator it = this.counters.iterator();
        while (it.hasNext()) {
            Counter counter = (Counter) it.next();
            if (counter.getCounter() % 90 == 0) {
                this.explosions.get(this.counters.indexOf(counter)).setVisible(false);
            }
        }
    }

    public void updateCounters () {
        for( Counter counter : counters ) {
            counter.counter++;
        }
    }

}