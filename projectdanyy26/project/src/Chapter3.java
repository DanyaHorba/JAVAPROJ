import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class Chapter3 extends JPanel {
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

    int[] obsX   = new int[10];
    int[] obsY   = new int[10];
    int[] obsDir = new int[10];

    int obsSpeed = 3;
    private int tick = 0;


    private boolean keyCollected = false;
    private int keyX = 0, keyY = 0;

    Timer gameTimer;
    private MenuWindow menu;

    Chapter3(MenuWindow menu) {
        this.menu = menu;
        setFocusable(true);
        setBackground(new Color(230, 245, 255));
        resetPositions();

        gameTimer = new Timer(16, e -> {
            if (isgameover == 0) {
                tick++;
                switch (level) {
                    case 1 -> movePendulums();
                    case 2 -> moveSpiral();
                }
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
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(Chapter3.this);
                    if (frame != null) frame.dispose();
                    if (menu != null) menu.refreshAndShow();
                    return;
                }

                if (isgameover == 1 || isgameover == 2) {
                    if (e.getKeyCode() == KeyEvent.VK_R) {
                        level = 1;
                        tick = 0;
                        keyCollected = false;
                        resetPositions();
                        score = 0;
                        isgameover = 0;
                        repaint();
                    }
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        gameTimer.stop();
                        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(Chapter3.this);
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

                playerX = Math.max(0, Math.min(playerX, getWidth() - 20));
                playerY = Math.max(0, Math.min(playerY, getHeight() - 20));

                Rectangle player = new Rectangle(playerX, playerY, 20, 20);


                if (level == 2 && !keyCollected) {
                    Rectangle keyRect = new Rectangle(keyX, keyY, 20, 20);
                    if (player.intersects(keyRect)) keyCollected = true;
                }

                Rectangle portal = new Rectangle(portalX, portalY, 25, 25);
                boolean canEnterPortal = (level != 2) || keyCollected;

                if (canEnterPortal && player.intersects(portal)) {
                    score++;
                    if (level < 2) {
                        level++;
                        tick = 0;
                        keyCollected = false;
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


    private void movePendulums() {
        int panelWidth = getWidth() > 0 ? getWidth() : 600;
        int cx = panelWidth / 2;
        int amplitude = panelWidth / 2 - 20;
        for (int i = 0; i < obsX.length; i++) {
            double phase = (i % 2 == 0) ? 0 : Math.PI;
            double angle = tick * 0.04 + phase + i * 0.3;
            obsX[i] = cx + (int)(amplitude * Math.sin(angle)) - 20;
        }
    }


    private void moveSpiral() {
        int panelWidth  = getWidth()  > 0 ? getWidth()  : 600;
        int panelHeight = getHeight() > 0 ? getHeight() : 400;
        int cx = panelWidth  / 2;
        int cy = panelHeight / 2;
        int radius = 90;
        int count = 6;
        double step = (2 * Math.PI) / count;
        for (int i = 0; i < count; i++) {
            double angle = tick * 0.025 + i * step;
            obsX[i] = cx + (int)(radius * Math.cos(angle)) - 20;
            obsY[i] = cy + (int)(radius * Math.sin(angle)) - 10;
        }
        for (int i = count; i < obsX.length; i++) {
            obsX[i] = -100;
            obsY[i] = -100;
        }
    }

    private void resetPositions() {
        playerX = 290; playerY = 340;
        portalX = 290; portalY = 30;

        int panelWidth  = getWidth()  > 0 ? getWidth()  : 600;
        int panelHeight = getHeight() > 0 ? getHeight() : 400;

        for (int i = 0; i < obsDir.length; i++) obsDir[i] = (i % 2 == 0) ? 1 : -1;

        switch (level) {
            case 1 -> {
                obsSpeed = 3;
                for (int i = 0; i < obsY.length; i++) {
                    obsX[i] = 0;
                    obsY[i] = 50 + i * 22;
                }
            }
            case 2 -> {
                obsSpeed = 3;
                keyX = panelWidth  / 2 - 10;
                keyY = panelHeight / 2 - 10;
                keyCollected = false;
                for (int i = 0; i < obsX.length; i++) {
                    obsX[i] = -100;
                    obsY[i] = -100;
                }
            }
        }
    }

    private boolean check(Rectangle player) {
        for (int i = 0; i < obsX.length; i++)
            if (player.intersects(new Rectangle(obsX[i], obsY[i], 40, 20))) return true;
        return false;
    }


    private void startEndCutscene() {
        isgameover = 3;
        MenuWindow.isChapter3Unlocked = true;
        cutscenePhase = 0;
        cutsceneTick  = 0;
        playerX = -30;
        playerY = getHeight() / 2 - 10;

        cutsceneTimer = new Timer(16, e -> {
            cutsceneTick++;
            int center = getWidth() / 2 - 10;

            switch (cutscenePhase) {
                case 0 -> {

                    if (playerX < center) playerX += 3;
                    else { playerX = center; cutscenePhase = 1; cutsceneTick = 0; }
                }
                case 1 -> {

                    if (cutsceneTick >= 200) { cutscenePhase = 2; cutsceneTick = 0; }
                }
                case 2 -> {

                    if (cutsceneTick >= 180) { cutscenePhase = 3; cutsceneTick = 0; }
                }
                case 3 -> {

                    playerX += 3;
                    if (playerX > getWidth() + 30) {
                        cutsceneTimer.stop();
                        isgameover = 2;
                    }
                }
            }
            repaint();
        });
        cutsceneTimer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);


        if (isgameover == 3) {
            // Темний фон для атмосфери
            g.setColor(new Color(20, 20, 40));
            g.fillRect(0, 0, getWidth(), getHeight());


            g.setColor(new Color(70, 130, 180));
            g.fillRect(playerX, playerY, 20, 20);


            g.setColor(new Color(255, 255, 200, 180));
            int[][] stars = {{80,60},{200,40},{350,90},{450,30},{520,70},
                    {130,130},{300,20},{410,110},{560,50},{170,80}};
            for (int[] s : stars) g.fillOval(s[0], s[1], 3, 3);

            if (cutscenePhase == 1) {
                drawSpeechBubble(g, playerX, playerY, "Finally... it's over.");
            } else if (cutscenePhase == 2) {
                drawSpeechBubble(g, playerX, playerY, "Time to rest.");
            }
            return;
        }


        Color obsColor = switch (level) {
            case 1 -> new Color(220, 80,  80);
            case 2 -> new Color(80,  160, 220);
            default -> Color.RED;
        };

        g.setColor(obsColor);
        for (int i = 0; i < obsX.length; i++)
            g.fillRect(obsX[i], obsY[i], 40, 20);


        if (level == 2 && !keyCollected) {
            g.setColor(new Color(255, 210, 0));
            g.fillRect(keyX, keyY, 20, 20);
        }

        // Portal
        boolean portalLocked = (level == 2 && !keyCollected);
        g.setColor(portalLocked ? new Color(150, 150, 150) : new Color(155, 89, 182));
        g.fillOval(portalX, portalY, 25, 25);

        // Player
        g.setColor(new Color(70, 130, 180));
        g.fillRect(playerX, playerY, 20, 20);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("Level " + level + ": ", 20, 30);

        if (level == 2) {
            g.setFont(new Font("Arial", Font.ITALIC, 13));
            g.setColor(keyCollected ? new Color(0, 150, 0) : new Color(180, 80, 0));

        }

        // ── Game Over / Win екрани ─────────────────────────────────────────────
        if (isgameover == 1) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("GAME OVER", 150, 150);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.BLACK);
            g.drawString("Press R to Restart", 210, 200);
            g.drawString("Press Q to Quit", 210, 250);
        } else if (isgameover == 2) {
            g.setColor(new Color(30, 160, 30));
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("YOU WIN!", 180, 150);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.BLACK);
            g.drawString("Press R to Restart Chapter", 180, 200);
            g.drawString("Press Q to Go to Menu", 210, 250);
        }
    }

    /** Малює хмарку діалогу над персонажем */
    private void drawSpeechBubble(Graphics g, int px, int py, String text) {
        FontMetrics fm = g.getFontMetrics(new Font("Arial", Font.ITALIC, 14));
        int textW = fm.stringWidth(text);
        int bx = px - textW / 2 - 10;
        int by = py - 55;
        int bw = textW + 20;
        int bh = 28;

        g.setColor(new Color(255, 255, 255, 220));
        g.fillRoundRect(bx, by, bw, bh, 10, 10);
        g.setColor(Color.DARK_GRAY);
        g.drawRoundRect(bx, by, bw, bh, 10, 10);


        int[] txPts = {px + 5, px + 10, px + 14};
        int[] tyPts = {by + bh, py - 5,  by + bh - 8};
        g.setColor(new Color(255, 255, 255, 220));
        g.fillPolygon(txPts, tyPts, 3);
        g.setColor(Color.DARK_GRAY);
        g.drawPolygon(txPts, tyPts, 3);

        g.setFont(new Font("Arial", Font.ITALIC, 14));
        g.setColor(Color.BLACK);
        g.drawString(text, bx + 10, by + 19);
    }
}