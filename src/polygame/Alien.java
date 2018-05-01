package polygame;

class Alien extends Sprite {

    Alien(int x, int y) {
        super(x, y);
        this.initAlien();
    }

    private void initAlien() {
        this.loadImage("res/alien.png");
        this.getImageDimensions();
    }

    void move() {
        this.x -= 2;
    }
}