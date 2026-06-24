import javax.swing.*;
import java.awt.*;

class MenuWindow extends JFrame {

    public static boolean isChapter2Unlocked = false;
    public static boolean isChapter3Unlocked = false;

    private JButton startButton2;
    private JButton startButton3;

    MenuWindow() {
        setTitle("Menu");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JLabel title = new JLabel("Menu", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(Color.WHITE);

        JButton startButton = new JButton("CHAPTER 1");
        startButton2 = new JButton("CHAPTER 2");
        startButton3 = new JButton("CHAPTER 3");
        JButton exitButton = new JButton("Exit");

        startButton.setFont(new Font("Arial", Font.BOLD, 22));
        startButton2.setFont(new Font("Arial", Font.BOLD, 22));
        startButton3.setFont(new Font("Arial", Font.BOLD, 22));
        exitButton.setFont(new Font("Arial", Font.BOLD, 22));

        startButton.setBackground(new Color(220, 70, 70));
        startButton.setForeground(Color.WHITE);
        exitButton.setBackground(Color.GRAY);
        exitButton.setForeground(Color.WHITE);

        updateButtonsState();

        JPanel panel = new JPanel();
        panel.setBackground(new Color(40, 50, 80));
        panel.setLayout(new GridLayout(5, 1, 15, 15));

        panel.add(title);
        panel.add(startButton);
        panel.add(startButton2);
        panel.add(startButton3);
        panel.add(exitButton);

        add(panel);

        startButton.addActionListener(e -> {
            setVisible(false);
            new GameWindow(this);
        });

        startButton2.addActionListener(e -> {
            setVisible(false);
            new GameWindow2(this);
        });
        startButton3.addActionListener(e -> {
            setVisible(false);
            new GameWindow3(this);
        });

        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    public void updateButtonsState() {
        if (isChapter2Unlocked) {
            startButton2.setEnabled(true);
            startButton2.setBackground(new Color(220, 70, 70));
            startButton2.setForeground(Color.WHITE);
        } else {
            startButton2.setEnabled(false);
            startButton2.setBackground(Color.DARK_GRAY);
            startButton2.setForeground(Color.LIGHT_GRAY);
        }

        if (isChapter3Unlocked) {
            startButton3.setEnabled(true);
            startButton3.setBackground(new Color(220, 70, 70));
            startButton3.setForeground(Color.WHITE);
        } else {
            startButton3.setEnabled(false);
            startButton3.setBackground(Color.DARK_GRAY);
            startButton3.setForeground(Color.LIGHT_GRAY);
        }
    }

    public void refreshAndShow() {
        updateButtonsState();
        setVisible(true);
    }
}
