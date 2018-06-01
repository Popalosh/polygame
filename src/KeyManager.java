import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyManager extends KeyAdapter {

    private static boolean[] keys;
    private boolean[] pressed;

    public KeyManager() {
        keys = new boolean[256];
        pressed = new boolean[keys.length];
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() != 32) {
            keys[e.getKeyCode()] = true;
        } else {
            if (!pressed[32]) {
                keys[32] = true;
                pressed[32] = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
        pressed[e.getKeyCode()] = false;
    }

    public static boolean[] getKeys() {
        return keys;
    }
}