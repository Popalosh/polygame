package polygame;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.io.File;
import java.net.MalformedURLException;
import javax.swing.ImageIcon;

class Sprite {
    int x;
    int y;
    int width;
    int height;
    boolean visibility;
    private Image image;

    Sprite(int x, int y) {
        this.x = x;
        this.y = y;
        this.visibility = true;
    }

    void getImageDimensions() {
        this.width = this.image.getWidth((ImageObserver) null);
        this.height = this.image.getHeight((ImageObserver) null);
    }

    void loadImage(String imageName) {
        try {
            File url = new File(imageName);
            ImageIcon ii = new ImageIcon(url.toURI().toURL());
            this.image = ii.getImage();
        } catch (MalformedURLException e){};
    }

    Image getImage() {
        return this.image;
    }

    int getX() {
        return this.x;
    }

    int getY() {
        return this.y;
    }

    boolean isVisible() {
        return this.visibility;
    }

    void setVisible(Boolean visible) {
        this.visibility = visible;
    }

    Rectangle getBounds() {
        return new Rectangle(this.x, this.y, this.width, this.height);
    }
}
