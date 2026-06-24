import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class GamePanel extends JPanel {
    int playerX = 40;
    int playerY = 40;
    int portalX = 290;
    int portalY = 190;
    int score = 0;
    int isgameover = 0;

    int level = 1;
    private Timer cutsceneTimer;
    private MenuWindow menu;

    GamePanel(MenuWindow menu) {
        this.menu = menu;
        setFocusable(true);
        setBackground(new Color(230, 245, 255));

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Q) {
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(GamePanel.this);
                    if (frame != null) {
                        frame.dispose();
                    }
                    if (menu != null) {
                        menu.refreshAndShow();
                    }
                    return;
                }

                if (isgameover == 3) return;

                if (isgameover == 1 || isgameover == 2) {
                    if (e.getKeyCode() == KeyEvent.VK_R) {
                        level = 1;
                        resetPlayerPosition();
                        score = 0;
                        isgameover = 0;
                        repaint();
                    }
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(GamePanel.this);
                        if (frame != null) frame.dispose();
                    }
                    return;
                }

                int speed = 10;
                if (e.getKeyCode() == KeyEvent.VK_LEFT)  playerX -= speed;
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) playerX += speed;
                if (e.getKeyCode() == KeyEvent.VK_UP)    playerY -= speed;
                if (e.getKeyCode() == KeyEvent.VK_DOWN)  playerY += speed;

                if (playerX < 0) playerX = 0;
                if (playerY < 0) playerY = 0;
                if (playerX > getWidth() - 20) playerX = getWidth() - 20;
                if (playerY > getHeight() - 20) playerY = getHeight() - 20;

                Rectangle player = new Rectangle(playerX, playerY, 20, 20);
                Rectangle portal = new Rectangle(portalX, portalY, 25, 25);

                if (player.intersects(portal)) {
                    score++;
                    if (level < 5) {
                        level++;
                        resetPlayerPosition();
                        player = new Rectangle(playerX, playerY, 20, 20);
                    } else {
                        startEndCutscene();
                    }
                }

                if (isgameover == 0 && check(player)) {
                    isgameover = 1;
                }
                repaint();
            }
        });
    }

    private void startEndCutscene() {
        isgameover = 3;
        MenuWindow.isChapter2Unlocked = true;

        cutsceneTimer = new Timer(30, e -> {
            playerX += 2;
            if (playerX > getWidth()) {
                cutsceneTimer.stop();
                isgameover = 2;
            }
            repaint();
        });
        cutsceneTimer.start();
    }

    private void resetPlayerPosition() {
        if (level == 1) { playerX = 40; playerY = 40; portalX = 290; portalY = 190; }
        if (level == 2) { playerX = 40; playerY = 320; portalX = 520; portalY = 40; }
        if (level == 3) { playerX = 520; playerY = 40; portalX = 50; portalY = 320; }
        if (level == 4) { playerX = 290; playerY = 40; portalX = 290; portalY = 330; }
        if (level == 5) { playerX = 10; playerY = 320; portalX = 260; portalY = 160; }
    }

    private boolean check(Rectangle player) {
        if (level == 1) {
            if (player.intersects(new Rectangle(120, 0, 30, 60))) return true;
            if (player.intersects(new Rectangle(120, 120, 30, 280))) return true;
            if (player.intersects(new Rectangle(0, 260, 60, 30))) return true;
            if (player.intersects(new Rectangle(240, 120, 150, 30))) return true;
            if (player.intersects(new Rectangle(240, 260, 30, 140))) return true;
            if (player.intersects(new Rectangle(360, 0, 30, 60))) return true;
            if (player.intersects(new Rectangle(360, 120, 150, 30))) return true;
            if (player.intersects(new Rectangle(480, 120, 30, 280))) return true;
        }
        if (level == 2) {
            if (player.intersects(new Rectangle(0, 100, 450, 30))) return true;
            if (player.intersects(new Rectangle(150, 220, 450, 30))) return true;
        }
        if (level == 3) {
            if (player.intersects(new Rectangle(100, 0, 40, 300))) return true;
            if (player.intersects(new Rectangle(250, 100, 40, 300))) return true;
            if (player.intersects(new Rectangle(400, 0, 40, 300))) return true;
        }
        if (level == 4) {
            if (player.intersects(new Rectangle(200, 100, 200, 40))) return true;
            if (player.intersects(new Rectangle(100, 220, 160, 40))) return true;
            if (player.intersects(new Rectangle(340, 220, 160, 40))) return true;
        }
        if(level == 5) {
            if (player.intersects(new Rectangle(0, 50, 165, 250))) return true;
            if (player.intersects(new Rectangle(445, 50, 85, 250))) return true;
            if (player.intersects(new Rectangle(245, 310, 195, 55))) return true;
            if (player.intersects(new Rectangle(355, 5, 85, 45))) return true;
            if (player.intersects(new Rectangle(240, 50, 60, 42))) return true;
            if (player.intersects(new Rectangle(240, 97, 60, 45))) return true;
            if (player.intersects(new Rectangle(310, 97, 50, 90))) return true;
            if (player.intersects(new Rectangle(180, 195, 180, 65))) return true;
        }
        return false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(70, 130, 180));
        g.fillRect(playerX, playerY, 20, 20);

        if (isgameover == 3) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.ITALIC, 16));
            g.drawString("I'll be at the finish line soon!", playerX - 40, playerY - 15);
            return;
        }

        g.setColor(Color.RED);
        if (level == 1) {
            g.fillRect(120, 0, 30, 60); g.fillRect(120, 120, 30, 280); g.fillRect(0, 260, 60, 30);
            g.fillRect(240, 120, 150, 30); g.fillRect(240, 260, 30, 140); g.fillRect(360, 0, 30, 60);
            g.fillRect(360, 120, 150, 30); g.fillRect(480, 120, 30, 280);
        }
        if (level == 2) { g.fillRect(0, 100, 450, 30); g.fillRect(150, 220, 450, 30); }
        if (level == 3) { g.fillRect(100, 0, 40, 300); g.fillRect(250, 100, 40, 300); g.fillRect(400, 0, 40, 300); }
        if (level == 4) { g.fillRect(200, 100, 200, 40); g.fillRect(100, 220, 160, 40); g.fillRect(340, 220, 160, 40); }
        if (level == 5) {
            g.fillRect(0, 50, 165, 250); g.fillRect(445, 50, 85, 250); g.fillRect(245, 310, 195, 55); g.fillRect(355, 5, 85, 45);
            g.fillRect(240, 50, 60, 42); g.fillRect(240, 97, 60, 45); g.fillRect(310, 97, 50, 90); g.fillRect(180, 195, 180, 65);
        }

        g.setColor(new Color(155, 89, 182));
        g.fillOval(portalX, portalY, 25, 25);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("Level: " + level, 20, 30);

        if (isgameover == 2) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("YOU WIN!", 180, 150);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.BLACK);
            g.drawString("Press R to Restart Game", 210, 200);
            g.drawString("Press Q to Go to Menu", 210, 250);
        }
        else if (isgameover == 1) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("GAME OVER", 160, 150);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.BLACK);
            g.drawString("Press R to Try Again", 210, 200);
            g.drawString("Press Q to Go to Menu", 210, 250);
        }
    }
}
