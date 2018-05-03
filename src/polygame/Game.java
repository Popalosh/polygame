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
        this.setTitle("polygame");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
