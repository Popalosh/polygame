package polygame;

class Alien extends Sprite {

    private int ALIEN_SPEED = 3;

    public Alien(int x, int y) {
        super(x, y);
        this.initAlien();
    }

    private void initAlien() {
        this.loadImage("alien.png");
        this.getImageDimensions();
    }

    public void move() {
        this.x -= ALIEN_SPEED;
    }
}