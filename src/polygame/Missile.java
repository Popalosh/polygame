package polygame;


class Missile extends Sprite {
    private final int BOARD_WIDTH = 640;
    private final int MISSILE_SPEED = 8;

    Missile(int x, int y) {
        super(x, y);
        this.initMissile();
    }

    private void initMissile() {
        this.loadImage("res/misle.png");
        this.getImageDimensions();
    }

    void move() {
        this.x += 8;
        if (this.x > 640) {
            this.visibility = false;
        }

    }
}