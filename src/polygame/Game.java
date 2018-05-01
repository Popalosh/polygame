package polygame;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Game extends JFrame {
    private Game() {
        this.initUI();
    }

    private void initUI() {
        this.add(new Board());
        this.setResizable(false);
        this.pack();
        this.setTitle("Polygame");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(2);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(
                () -> {
                    Game ex = new Game();
                    ex.setVisible(true);
                }
        );
    }
}
