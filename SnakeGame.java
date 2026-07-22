import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    // Window Size
    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    static final int DELAY = 300;

    LinkedList<Point> snake = new LinkedList<>();
    Point frog;

    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random = new Random();

    SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        startGame();
    }

    void startGame() {
        snake.clear();
        snake.add(new Point(5, 5));
        snake.add(new Point(4, 5));
        snake.add(new Point(3, 5));

        spawnFrog();

        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    void spawnFrog() {
        while (true) {
            Point p = new Point(
                    random.nextInt(WIDTH / UNIT_SIZE),
                    random.nextInt(HEIGHT / UNIT_SIZE)
            );
            if (!snake.contains(p)) {
                frog = p;
                break;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    void draw(Graphics g) {

        if (!running) {
            gameOver(g);
            return;
        }

        // Grid (Optional)
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < WIDTH; i += UNIT_SIZE) {
            g.drawLine(i, 0, i, HEIGHT);
            g.drawLine(0, i, WIDTH, i);
        }

        // Frog (Red)
        g.setColor(Color.RED);
        g.fillOval(frog.x * UNIT_SIZE, frog.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);

        
       // Snake (Different Colors)
int i = 0;

for (Point p : snake) {

    switch (i % 6) {

        case 0:
            g.setColor(Color.BLUE);
            break;

        case 1:
            g.setColor(Color.CYAN);
            break;

        case 2:
            g.setColor(Color.GREEN);
            break;

        case 3:
            g.setColor(Color.YELLOW);
            break;

        case 4:
            g.setColor(Color.ORANGE);
            break;

        default:
            g.setColor(Color.MAGENTA);
            break;
    }

    g.fillOval(
            p.x * UNIT_SIZE,
            p.y * UNIT_SIZE,
            UNIT_SIZE,
            UNIT_SIZE
    );

    i++;
}
    }

    void move() {
        Point head = new Point(snake.getFirst());

        switch (direction) {
    case 'U':
        head.y--;
        break;

    case 'D':
        head.y++;
        break;

    case 'L':
        head.x--;
        break;

    case 'R':
        head.x++;
        break;
}

        snake.addFirst(head);

        if (head.equals(frog)) {
            spawnFrog();
        } else {
            snake.removeLast();
        }
    }

    void checkCollision() {

    Point head = snake.getFirst();

    if (head.x < 0 || head.y < 0 ||
        head.x >= WIDTH / UNIT_SIZE ||
        head.y >= HEIGHT / UNIT_SIZE) {

        running = false;
        timer.stop();
        repaint();
        return;
    }

    for (int i = 1; i < snake.size(); i++) {
        if (head.equals(snake.get(i))) {
            running = false;
            timer.stop();
            repaint();
            return;
        }
    }
}
    void gameOver(Graphics g) {

    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", Font.BOLD, 40));
    g.drawString("GAME OVER", 150, 220);

    g.setFont(new Font("Arial", Font.PLAIN, 25));
    g.drawString("Score : " + (snake.size() - 3), 235, 270);

    JButton restartButton = new JButton("Restart");

    restartButton.setBounds(240, 320, 120, 40);

    restartButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            remove(restartButton);

            direction = 'R';
            running = true;

            snake.clear();
            snake.add(new Point(5, 5));
            snake.add(new Point(4, 5));
            snake.add(new Point(3, 5));

            spawnFrog();

            timer.start();

            repaint();
        }
    });

    setLayout(null);

    boolean exists = false;
    for (Component c : getComponents()) {
        if (c instanceof JButton) {
            exists = true;
            break;
        }
    }

    if (!exists) {
        add(restartButton);
    }
}
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollision();
        }
        repaint();
    }
@Override
public void keyPressed(KeyEvent e) {

    switch (e.getKeyCode()) {

        case KeyEvent.VK_LEFT:
            if (direction != 'R')
                direction = 'L';
            break;

        case KeyEvent.VK_RIGHT:
            if (direction != 'L')
                direction = 'R';
            break;

        case KeyEvent.VK_UP:
            if (direction != 'D')
                direction = 'U';
            break;

        case KeyEvent.VK_DOWN:
            if (direction != 'U')
                direction = 'D';
            break;

        case KeyEvent.VK_Q:
            timer.stop();
            System.exit(0);
            break;
    }
}
   
    

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {

        JFrame frame = new JFrame("Snake Game");

        SnakeGame game = new SnakeGame();

        frame.add(game);
        frame.pack();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}