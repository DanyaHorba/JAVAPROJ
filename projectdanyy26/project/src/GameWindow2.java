import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class GameWindow2 extends JFrame {
    MenuWindow menu;
    GamePanel panel;
    Chapter2 chapter2;

    GameWindow2(MenuWindow menu) {
        this.menu = menu;

        setTitle("Game");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chapter2 = new Chapter2(menu);
        add(chapter2);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                menu.setVisible(true);
            }
        });

        setVisible(true);
    }
}
