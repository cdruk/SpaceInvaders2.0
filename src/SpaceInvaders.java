import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class SpaceInvaders extends JFrame {

    private static final int SQUARE_SIZE = 40;
    GameBoard gameBoard;
    Direction alienDir;

    private SpaceInvaders() {
        gameBoard = new GameBoard(SQUARE_SIZE);
        gameBoard.setBackground(Color.black);
        int canvasWidth = SQUARE_SIZE * gameBoard.BOARD_COLS;
        int canvasHeight = SQUARE_SIZE * gameBoard.BOARD_ROWS;
        setWindowProperties(canvasWidth, canvasHeight + 10);
        JComponent mainPanel = gameBoard;
        add(mainPanel);
        addKeyListener(new MyKeyAdapter());
        runGame();
    }

    private void runGame() {
        alienDir = Direction.RIGHT;
        ActionListener moveListener = e -> {
            alienDir = gameBoard.moveAliens(alienDir);
            if (gameBoard.isGameOver()) {
                gameBoard.exit();
            }
            repaint();
        };
        Timer timer = new Timer(1000, moveListener);
        timer.setRepeats(true);
        timer.start();
    }


    @Override
    public void paint(Graphics g) {

        gameBoard.repaint();
    }

    private void setWindowProperties(int width, int height) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Space Invaders - Score: " + gameBoard.getScore());
        setSize(width, height);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent keyEvent) {

            if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                gameBoard.movement = Direction.LEFT;
                gameBoard.moveShooter();
                repaint();
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                gameBoard.movement = Direction.RIGHT;
                gameBoard.moveShooter();
                repaint();
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
                gameBoard.shoot();
                repaint();
                setTitle("Space Invaders - Score: " + gameBoard.getScore());
            }

        }
    }

    public static void main(String[] args) {
        new SpaceInvaders().setVisible(true);
    }
}