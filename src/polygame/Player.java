package polygame;

import java.util.ArrayList;

class Player extends Sprite {
    private int dx;
    private int dy;
    private ArrayList<Missile> missiles;

    Player(int x, int y) {
        super(x, y);
        this.initPlayer();
    }

    private void initPlayer() {
        this.missiles = new ArrayList();
        this.loadImage("res/player.png");
        this.getImageDimensions();
    }

    void move() {
        this.x += this.dx;
        this.y += this.dy;
    }

    private void fire() {
        this.missiles.add(new Missile(this.x + this.width, this.y + this.height / 2));
    }

    ArrayList getMissiles() {
        return this.missiles;
    }

    void getInput() {
        boolean[] keys = KeyManager.getKeys();

        dy = 0;
        dx = 0;

        if (keys[32]) { //space
            this.fire();
            keys[32] = false;
        }

        if (keys[65]) { // a
            this.dx = -4;
        }

        if (keys[68]) { // d
            this.dx = 4;
        }

        if (keys[87]) {  // w
            this.dy = -4;
        }

        if (keys[83]) { // s
            this.dy = 4;
        }
    }
}
