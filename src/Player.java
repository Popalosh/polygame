import java.awt.event.KeyEvent;
import java.util.ArrayList;

class Player extends Sprite {

    private int dx;
    private int dy;
    private ArrayList<Missile> missiles;

    private final int PLAYER_SPEED = 3;

    public Player(int x, int y) {
        super(x, y);
        this.initPlayer();
    }

    private void initPlayer() {
        this.missiles = new ArrayList();
        this.loadImage("player.png");
        this.getImageDimensions();
    }

    public void move() {
        this.x += this.dx;
        this.y += this.dy;
    }

    private void fire() {
        this.missiles.add(new Missile(this.x + this.width, this.y + this.height / 2));
    }

    public ArrayList getMissiles() {
        return this.missiles;
    }

    public void getInput() {
        boolean[] keys = KeyManager.getKeys();

        dy = 0;
        dx = 0;

        if (keys[32]) {
            this.fire();
            keys[32] = false;
        }

        if (keys[65] || keys[KeyEvent.VK_LEFT]) { // a
            this.dx = -PLAYER_SPEED;
        }

        if (keys[68] || keys[KeyEvent.VK_RIGHT]) { // d
            this.dx = PLAYER_SPEED;
        }

        if (keys[87] || keys[KeyEvent.VK_UP]) {  // w
            this.dy = -PLAYER_SPEED;
        }

        if (keys[83] || keys[KeyEvent.VK_DOWN]) { // s
            this.dy = PLAYER_SPEED;
        }
    }
}
