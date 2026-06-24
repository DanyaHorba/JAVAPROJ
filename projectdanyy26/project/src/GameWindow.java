import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class GameWindow extends JFrame {
    MenuWindow menu;
    GamePanel panel;

    GameWindow(MenuWindow menu) {
        this.menu = menu;

        setTitle("Game");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new GamePanel(menu);
        add(panel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                menu.setVisible(true);
            }
        });

        setVisible(true);
    }
}
