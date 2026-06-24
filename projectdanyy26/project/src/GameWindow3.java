import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class GameWindow3 extends JFrame {
    MenuWindow menu;
    GamePanel panel;
    Chapter3 chapter3;

    GameWindow3(MenuWindow menu) {
        this.menu = menu;

        setTitle("Game");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chapter3 = new Chapter3(menu);
        add(chapter3);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                menu.setVisible(true);
            }
        });

        setVisible(true);
    }
}
