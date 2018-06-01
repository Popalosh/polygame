import java.awt.Image;
import java.awt.Rectangle;
import java.net.URL;
import javax.swing.ImageIcon;

public class Sprite {

    public int x;
    public int y;
    public int width;
    public int height;
    public boolean visibility;
    private Image image;

    public Sprite(int x, int y) {
        this.x = x;
        this.y = y;
        this.visibility = true;
    }

    public void getImageDimensions() {
        this.width = this.image.getWidth(null);
        this.height = this.image.getHeight(null);
    }

    public void loadImage(String imageName) {
        URL url = getClass().getResource("/" + imageName);
        ImageIcon ii = new ImageIcon(url);
        this.image = ii.getImage();
    }

    public Image getImage() {
        return this.image;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isVisible() {
        return this.visibility;
    }

    public void setVisible(Boolean visible) {
        this.visibility = visible;
    }

    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, this.width, this.height);
    }
}
