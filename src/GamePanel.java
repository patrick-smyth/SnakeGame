import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 6;
    int score;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean gameRunning = false;
    Timer timer;
    Random random;


    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        gameRunning = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if(gameRunning) {
            for(int i  = 0; i < SCREEN_HEIGHT; i++) {
                g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE,SCREEN_HEIGHT);
                g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH,i*UNIT_SIZE);
            }

            g.setColor(Color.red);
            g.fillOval(appleX,appleY,UNIT_SIZE,UNIT_SIZE);

            for(int i = 0; i < bodyParts; i++) {
                if(i == 0) g.setColor(Color.green);
                else g.setColor(new Color(45,180,0));

                g.fillRect(x[i],y[i],UNIT_SIZE, UNIT_SIZE);
            }

            g.setColor(Color.red);
            g.setFont(new Font("Arial",Font.BOLD,35));
            FontMetrics fontMetrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + score, (SCREEN_WIDTH - fontMetrics.stringWidth("Score: " + score))/2,g.getFont().getSize());
        } else gameOver(g);

    }

    public void newApple() {
        appleX = random.nextInt((SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if((x[0] == appleX) && (y[0] == appleY)) {
            score++;
            bodyParts++;
            newApple();
        }
    }

    public void checkCollisions() {
        // Check if head collides with body
        for(int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                gameRunning = false;
                break;
            }
        }

        // Check if head collides with left border
        if(x[0] < 0) gameRunning = false;

        // Check if head collides with right border
        if(x[0] > SCREEN_WIDTH) gameRunning = false;

        // Check if head collides with top border
        if(y[0] < 0) gameRunning = false;

        // Check if head collides with bottom border
        if(y[0] > SCREEN_HEIGHT) gameRunning = false;

        if(!gameRunning) timer.stop();
    }

    public void gameOver(Graphics g) {

        g.setColor(Color.red);
        g.setFont(new Font("Arial",Font.BOLD,35));
        FontMetrics fontMetrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + score, (SCREEN_WIDTH - fontMetrics1.stringWidth("Score: " + score))/2,g.getFont().getSize());

        g.setColor(Color.red);
        g.setFont(new Font("Arial",Font.BOLD,75));
        FontMetrics fontMetrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_WIDTH - fontMetrics2.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') direction = 'D';
                    break;
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if(gameRunning) {
            move();
            checkApple();
            checkCollisions();

        }

        repaint();
    }
}
