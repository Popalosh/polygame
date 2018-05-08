package polygame;


public class Explosion extends Sprite {

    public Explosion (int x, int y) {
        super(x,y);
        this.initExplosion();
    }

    public void initExplosion() {
        this.loadImage("explosion.gif");
        this.getImageDimensions();
    }

}