import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class Chapter2 extends JPanel {
    int playerX = 290;
    int playerY = 340;
    int portalX = 290;
    int portalY = 30;

    int score = 0;
    private Timer cutsceneTimer;
    private int cutscenePhase = 0;
    private int cutsceneTick = 0;
    int isgameover = 0;
    int level = 1;

    int[] obsX = {50, 450, 100, 400, 150, 350, 200, 300, 80, 250};
    int[] obsY = {70, 95, 120, 145, 170, 195, 220, 245, 270, 295};
    int[] obsDir = {1, -1, 1, -1, 1, -1, 1, -1, 1, -1};
    int[] obsDirY = {1, 1, -1, -1, 1, -1, 1, -1, 1, -1};

    int blockX, blockY;

    int obsSpeed = 4;

    Timer gameTimer;
    private MenuWindow menu;

    Chapter2(MenuWindow menu) {
        this.menu = menu;
        setFocusable(true);
        setBackground(new Color(230, 245, 255));

        gameTimer = new Timer(16, e -> {
            if (isgameover == 0) {
                if (level == 1) moveConveyor();
                else if (level == 2) moveVertical();
                else if (level == 3) moveRhythmHorizontal();
                else if (level == 4) moveChessHorizontal();
                else if (level == 5) moveDiagonalPhysics();

                Rectangle player = new Rectangle(playerX, playerY, 20, 20);
                if (check(player)) isgameover = 1;
                repaint();
            }
        });
        gameTimer.start();

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Q) {
                    gameTimer.stop();
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(Chapter2.this);
                    if (frame != null) frame.dispose();
                    if (menu != null) menu.refreshAndShow();
                    return;
                }

                if (isgameover == 1 || isgameover == 2) {
                    if (e.getKeyCode() == KeyEvent.VK_R) {
                        level = 1;
                        resetPositions();
                        score = 0;
                        isgameover = 0;
                        repaint();
                    }
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        gameTimer.stop();
                        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(Chapter2.this);
                        if (frame != null) frame.dispose();
                    }
                    return;
                }

                if (isgameover == 3) return;

                int speed = 10;
                if (e.getKeyCode() == KeyEvent.VK_LEFT)  playerX -= speed;
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) playerX += speed;
                if (e.getKeyCode() == KeyEvent.VK_UP)    playerY -= speed;
                if (e.getKeyCode() == KeyEvent.VK_DOWN)  playerY += speed;

                if (playerX < 0) playerX = 0;
                if (playerY < 0) playerY = 0;
                if (playerX > getWidth() - 20)  playerX = getWidth() - 20;
                if (playerY > getHeight() - 20) playerY = getHeight() - 20;

                Rectangle player = new Rectangle(playerX, playerY, 20, 20);
                Rectangle portal = new Rectangle(portalX, portalY, 25, 25);

                if (player.intersects(portal)) {
                    score++;
                    if (level < 5) {
                        level++;
                        resetPositions();
                    } else {
                        startEndCutscene();
                    }
                }

                if (isgameover == 0 && check(player)) isgameover = 1;
                repaint();
            }
        });
    }

    private void startEndCutscene() {
        isgameover = 3;
        MenuWindow.isChapter3Unlocked = true;
        cutscenePhase = 0;
        cutsceneTick = 0;

        playerX = 0;
        playerY = getHeight() / 2 - 10;

        blockX = (int)(getWidth() * 0.65);
        blockY = getHeight() / 2 - 200;

        cutsceneTimer = new Timer(16, e -> {
            cutsceneTick++;
            int midPoint = getWidth() / 2 - 20;

            if (cutscenePhase == 0) {
                if (playerX < midPoint) {
                    playerX += 2;
                } else {
                    cutscenePhase = 1;
                    cutsceneTick = 0;
                }
            } else if (cutscenePhase == 1) {
                if (cutsceneTick >= 188) {
                    cutscenePhase = 2;
                    cutsceneTimer.stop();
                    isgameover = 2;
                }
            }
            repaint();
        });
        cutsceneTimer.start();
    }

    private void moveConveyor() {
        int panelWidth = getWidth() > 0 ? getWidth() : 600;
        for (int i = 0; i < obsX.length; i++) {
            obsX[i] += obsSpeed;
            if (obsX[i] > panelWidth) obsX[i] = -40;
        }
    }

    private void moveVertical() {
        int minY = 50, maxY = 290;
        for (int i = 0; i < obsY.length; i++) {
            obsY[i] += obsSpeed * obsDir[i];
            if (obsY[i] < minY) { obsY[i] = minY; obsDir[i] = 1; }
            else if (obsY[i] > maxY) { obsY[i] = maxY; obsDir[i] = -1; }
        }
    }

    private void moveRhythmHorizontal() {
        int maxX = getWidth() > 0 ? getWidth() - 40 : 560;
        for (int i = 0; i < obsX.length; i++) {
            int currentSpeed = obsSpeed + (i % 3);
            obsX[i] += currentSpeed * obsDir[i];
            if (obsX[i] < 0) { obsX[i] = 0; obsDir[i] = 1; }
            else if (obsX[i] > maxX) { obsX[i] = maxX; obsDir[i] = -1; }
        }
    }

    private void moveChessHorizontal() {
        int maxX = getWidth() > 0 ? getWidth() - 40 : 560;
        for (int i = 0; i < obsX.length; i++) {
            obsX[i] += obsSpeed * obsDir[i];
            if (obsX[i] < 0) { obsX[i] = 0; obsDir[i] = 1; }
            else if (obsX[i] > maxX) { obsX[i] = maxX; obsDir[i] = -1; }
        }
    }

    private void moveDiagonalPhysics() {
        int maxX = getWidth() > 0 ? getWidth() - 40 : 560;
        int maxY = getHeight() > 0 ? getHeight() - 20 : 380;
        for (int i = 0; i < obsX.length; i++) {
            obsX[i] += obsSpeed * obsDir[i];
            obsY[i] += obsSpeed * obsDirY[i];
            if (obsX[i] < 0) { obsX[i] = 0; obsDir[i] = 1; }
            else if (obsX[i] > maxX) { obsX[i] = maxX; obsDir[i] = -1; }
            if (obsY[i] < 50) { obsY[i] = 50; obsDirY[i] = 1; }
            else if (obsY[i] > maxY - 40) { obsY[i] = maxY - 40; obsDirY[i] = -1; }
        }
    }

    private void resetPositions() {
        playerX = 290; playerY = 340;
        portalX = 290; portalY = 30;

        int[] defaultX = {50, 450, 100, 400, 150, 350, 200, 300, 80, 250};
        int[] defaultY = {70, 95, 120, 145, 170, 195, 220, 245, 270, 295};
        System.arraycopy(defaultX, 0, obsX, 0, obsX.length);
        System.arraycopy(defaultY, 0, obsY, 0, obsY.length);
        for (int i = 0; i < obsDir.length; i++) obsDir[i] = (i % 2 == 0) ? 1 : -1;

        int panelWidth = getWidth() > 0 ? getWidth() : 600;

        if (level == 1) {
            obsSpeed = 4;
        } else if (level == 2) {
            obsSpeed = 4;
            for (int i = 0; i < obsX.length; i++)
                obsX[i] = i * ((panelWidth - 40) / (obsX.length - 1));
        } else if (level == 3) {
            obsSpeed = 2;
            for (int i = 0; i < obsX.length; i++)
                obsX[i] = (i * 50) % (panelWidth - 40);
        } else if (level == 4) {
            obsSpeed = 5;
            for (int i = 0; i < obsX.length; i++) {
                if (i % 2 == 0) { obsX[i] = 10;              obsDir[i] = 1;  }
                else             { obsX[i] = panelWidth - 50; obsDir[i] = -1; }
            }
        } else if (level == 5) {
            obsSpeed = 1;
            for (int i = 0; i < obsX.length; i++) {
                obsX[i] = 30 + (i * 50);
                obsY[i] = 70 + (i * 22);
                obsDirY[i] = (i % 2 == 0) ? 1 : -1;
            }
        }
    }

    private boolean check(Rectangle player) {
        for (int i = 0; i < obsX.length; i++) {
            if (player.intersects(new Rectangle(obsX[i], obsY[i], 40, 20)))
                return true;
        }
        return false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isgameover == 3) {

            g.setColor(Color.RED);
            g.fillRect(blockX, blockY, 40, 400);


            g.setColor(new Color(70, 130, 180));
            g.fillRect(playerX, playerY, 20, 20);


            if (cutscenePhase >= 1) {
                g.setFont(new Font("Arial", Font.ITALIC, 13));
                g.setColor(Color.WHITE);
                g.fillRoundRect(playerX - 40, playerY - 45, 160, 25, 8, 8);
                g.setColor(Color.BLACK);
                g.drawRoundRect(playerX - 40, playerY - 45, 160, 25, 8, 8);
                g.drawString("Obstacles on my way...", playerX - 35, playerY - 27);
            }
            return;
        }

        g.setColor(new Color(70, 130, 180));
        g.fillRect(playerX, playerY, 20, 20);

        g.setColor(Color.RED);
        for (int i = 0; i < obsX.length; i++)
            g.fillRect(obsX[i], obsY[i], 40, 20);

        g.setColor(new Color(155, 89, 182));
        g.fillOval(portalX, portalY, 25, 25);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("Level: " + level, 20, 30);

        if (isgameover == 1) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("GAME OVER", 150, 150);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.BLACK);
            g.drawString("Press R to Restart", 210, 200);
            g.drawString("Press Q to Quit", 210, 250);
        } else if (isgameover == 2) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("YOU WIN!", 180, 150);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.BLACK);
            g.drawString("Press R to Restart Chapter", 210, 200);
            g.drawString("Press Q to Go to Menu", 210, 250);
        }
    }
}