import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;

public class Board extends JPanel implements ActionListener {

    private Timer timer;
    private Player player;
    private ArrayList<Alien> aliens;
    private boolean ingame;
    private boolean inmenu;
    private boolean records;
    private boolean recordVisibility = false;
    private final int ICRAFT_X = 60;
    private final int ICRAFT_Y = 60;
    public static final int B_WIDTH = 900;
    private final int B_HEIGHT = 600;
    private final int DELAY = 7;
    private int dead;
    private int health;
    private int cont;
    private int incAlien;
    private int countOfAlien;
    private ArrayList<Explosion> explosions;
    private ArrayList<Counter> counters;
    public static ArrayList<String> lines;
    private Graphics g;
    private boolean isScoreboardExist;
    private File file;


    public Board() {
        this.initBoard();
    }

    private void initBoard() {
        this.addKeyListener(new KeyManager());
        this.setFocusable(true);
        this.setBackground(Color.BLACK);
        this.inmenu = false;
//        this.readName = false;
        this.ingame = false;
        this.records = false;
        this.setPreferredSize(new Dimension(B_WIDTH, this.B_HEIGHT));
        this.player = new Player(this.ICRAFT_X, this.ICRAFT_Y);
        this.initAliens();
        this.explosions = new ArrayList<>();
        this.counters = new ArrayList<>();
        this.timer = new Timer(this.DELAY, this);
        this.timer.start();
        dead = 0;
        health = 30;
        cont = 30;
        incAlien = 20;
        countOfAlien = 10;

        file = new File("hightScores.txt");

        try {
            if (!file.exists()) {
                this.isScoreboardExist = false;
                file.createNewFile();

                BufferedWriter writer = Files.newBufferedWriter(file.toPath());
                for (int i = 0; i < 10; i++) {
                    writer.write(String.valueOf(10 - i));
                    writer.newLine();
                }
                writer.close();

            } else {
                this.isScoreboardExist = true;
                this.inmenu = true;
                Board.lines = (ArrayList<String>) Files.readAllLines(file.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initAliens() {

        this.aliens = new ArrayList();

        for (int i = 0; i < this.countOfAlien; ++i) {
            int randX = B_WIDTH + (int) (Math.random() * 900);
            int randY = 18 + (int) (Math.random() * 512);
            this.aliens.add(new Alien(randX, randY));
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.isScoreboardExist) {
            if (!this.ingame && this.inmenu && !this.records) {
                this.drawMenu(g);
//            } else if (this.readName) {
//                this.drawReadNamePanel(g);
            } else if (!this.ingame && !this.inmenu && this.records && this.recordVisibility) {
                this.drawScoreBoard(g);
            } else if (this.ingame && !this.inmenu && !this.records) {
                this.drawObjects(g);
            } else if (!this.ingame && !this.inmenu && !this.records) {
                this.drawGameOver(g);
            }
        } else {
            this.drawMsg(g);
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
        g.drawString("Aliens killed: " + this.dead, 5, 16);
        g.drawString("Health: " + this.health, 800, 16);
        g.drawLine(0, 18, 900, 18);
    }

    private void drawMsg(Graphics g) {
        String msg = "Scoreboard file not found";
        String msg2 = "Press Enter to create";

        Font small = new Font("Helvetica", 1, 18);
        FontMetrics fm = this.getFontMetrics(small);

        g.setColor(Color.WHITE);
        g.setFont(small);

        g.drawString(msg, (B_WIDTH - fm.stringWidth(msg)) / 2, this.B_HEIGHT / 2);
        g.drawString(msg2, (B_WIDTH - fm.stringWidth(msg2)) / 2, 40 +this.B_HEIGHT / 2);
    }

    private void drawMenu(Graphics g) {
        String start = "Press Enter to start New Game";
        String scores = "Press 'R' to check list of Records";

        Font small = new Font("Helvetica", 1, 18);
        FontMetrics fm = this.getFontMetrics(small);

        g.setColor(Color.WHITE);
        g.setFont(small);

        g.drawString(start, (B_WIDTH - fm.stringWidth(start)) / 2, this.B_HEIGHT / 2);

        if (this.recordVisibility) {
            g.drawString(scores, (B_WIDTH - fm.stringWidth(scores)) / 2, (int) ((double) this.B_HEIGHT / 1.5D));
        }
    }

    public void drawScoreBoard(Graphics g) {

        String msg = "Press Escape to go to Menu";

        Font small = new Font("Helvetica", 1, 12);
        FontMetrics fm = this.getFontMetrics(small);

        g.setColor(Color.WHITE);
        g.setFont(small);


        g.drawString(msg, (B_WIDTH - fm.stringWidth(msg)) / 2, this.B_HEIGHT / 14);
        for (int i = 0; i < lines.size(); i++) {
            if (lines.isEmpty()) {
                g.drawString("", (B_WIDTH - fm.stringWidth("")) / 2, (this.B_HEIGHT / 14) * (i + 2));
            } else {
                if (lines.get(i).isEmpty()) {
                    g.drawString("", (B_WIDTH - fm.stringWidth("")) / 2, (this.B_HEIGHT / 14) * (i + 2));
                } else {
                    g.drawString(lines.get(i), (B_WIDTH - fm.stringWidth(lines.get(i))) / 2, (this.B_HEIGHT / 14) * (i + 2));
                }
            }
        }
    }

    private void drawGameOver(Graphics g) {
        String msg = "Game Over";
        String res = "Press Enter to restart";
        String score = "Your score: " + (this.dead);
        Font small = new Font("Helvetica", 1, 18);
        FontMetrics fm = this.getFontMetrics(small);
        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - fm.stringWidth(msg)) / 2, this.B_HEIGHT / 2);
        g.drawString(score, ((B_WIDTH - fm.stringWidth(score)) / 2), (int) ((double) this.B_HEIGHT / 1.7D));
        g.drawString(res, (B_WIDTH - fm.stringWidth(res)) / 2, (int) ((double) this.B_HEIGHT / 1.5D));
    }


    private void increaseHealth() {
        if (this.dead == this.cont) {
            this.cont += 20;
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
        if (this.inmenu) {
            this.menu();
        } else if (this.records) {
            this.recordsPanel();
        } else if (!this.ingame) {
            this.restart();
        } else {
            this.updatePlayer();
            this.updateMissiles();
            this.updateAliens();
            this.increaseHealth();
            this.increaseAlien();
            this.checkCollision();
            this.updateCounters();
            this.checkExplosions();
        }
        this.repaint();
    }

    private void updatePlayer() {
        if (this.player.isVisible()) {
            this.player.getInput();
            if (this.player.getX() >= 0 &&
                    this.player.getY() >= 0) {
                this.player.move();
            }
            if (this.player.getY() < 18) this.player.setY(18);
            if (this.player.getY() > (int) this.B_HEIGHT - (int) player.getBounds().getHeight())
                this.player.setY((int) this.B_HEIGHT - (int) player.getBounds().getHeight());
            if (this.player.getX() < 0) this.player.setX(0);
            if (this.player.getX() > (int) this.B_WIDTH - (int) this.player.getBounds().getWidth())
                this.player.setX((int) this.B_WIDTH - (int) this.player.getBounds().getWidth());
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

            if (this.health == 0 || r3.intersects(r2)) {
                this.player.setVisible(false);
                alien.setVisible(false);
                this.ingame = false;
                this.recordVisibility = true;
                this.writingRecord();
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

    public void checkExplosions() {
        Iterator it = this.counters.iterator();
        while (it.hasNext()) {
            Counter counter = (Counter) it.next();
            if (counter.getCounter() % 60 == 0) {
                this.explosions.get(this.counters.indexOf(counter)).setVisible(false);
            }
        }
    }

    public void updateCounters() {
        for (Counter counter : counters) {
            counter.counter++;
        }
    }

    public void menu() {
        if (KeyManager.getKeys()[KeyEvent.VK_ENTER]) {
            this.ingame = true;
//            this.readName = true;
            this.inmenu = false;
        }

        if (this.recordVisibility) {
            if (KeyManager.getKeys()[KeyEvent.VK_R]) {
                this.ingame = false;
//                this.readName = false;
                this.records = true;
                this.inmenu = false;
            }
        }
    }

    public void recordsPanel() {
        if (KeyManager.getKeys()[KeyEvent.VK_ESCAPE]) {
            this.ingame = false;
            this.records = false;
            this.inmenu = true;
        }
    }

    public void writingRecord() {
        if (this.dead > Integer.parseInt(lines.get(0))) {
            try {
                BufferedWriter writer = Files.newBufferedWriter(file.toPath());
                writer.write(String.valueOf(this.dead));
                writer.newLine();
                for (int i = 0; i < 9; i++) {
                    writer.write(lines.get(i));
                    writer.newLine();
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            int lineNuber = -1;

            if (this.dead < Integer.parseInt(lines.get(0)) &&
                    this.dead > Integer.parseInt(lines.get(lines.size() - 1))) {
                for (String line : lines) {
                    if (Integer.parseInt(line) < this.dead) {
                        lineNuber = lines.indexOf(line);
                        break;
                    }
                }
            }

            if (lineNuber != -1) {

                ArrayList<String> newLines = new ArrayList<>();

                for (String line : lines) {
                    if (lines.indexOf(line) < lineNuber) {
                        newLines.add(line);
                    }
                }

                newLines.add(String.valueOf(this.dead));

                for (String line : lines) {

                    if (lines.indexOf(line) >= lineNuber && lines.indexOf(line) < lines.indexOf(lines.get(lines.size() - 1))) {
                        newLines.add(line);
                    }
                }

                lines = newLines;
            }

            try {
                BufferedWriter writer = Files.newBufferedWriter(file.toPath());

                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
