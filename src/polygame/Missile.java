package polygame;


class Missile extends Sprite {

    private final int BOARD_WIDTH = Board.B_WIDTH;
    private final int MISSILE_SPEED = 4;

    Missile(int x, int y) {
        super(x, y);
        this.initMissile();
    }

    private void initMissile() {
        this.loadImage("missle.gif");
        this.getImageDimensions();
    }

    void move() {
        this.x += MISSILE_SPEED;
        if (this.x > BOARD_WIDTH) {
            this.visibility = false;
        }
    }
}