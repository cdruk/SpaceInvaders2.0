import entities.Direction;

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
    String title;
    String lives;
    Timer timer;
    Timer warTimer;

    private SpaceInvaders() {
        title = "Space Invaders - Score: ";
        lives = "               Lives: ";
        gameBoard = new GameBoard(SQUARE_SIZE);
        gameBoard.setBackground(Color.black);
        int canvasWidth = SQUARE_SIZE * gameBoard.BOARD_COLS;
        int canvasHeight = SQUARE_SIZE * gameBoard.BOARD_ROWS;
        setWindowProperties(canvasWidth, canvasHeight + 10);
        JComponent mainPanel = gameBoard;
        add(mainPanel);
        addKeyListener(new MyKeyAdapter());
        runGame();
        if (gameBoard.isGameOver()){
            gameBoard = null;
        }
    }



    private void runGame() {
        alienDir = Direction.RIGHT;
        ActionListener moveListener = e -> {
            alienDir = gameBoard.moveAliens(alienDir);
            repaint();
        };
        timer = new Timer(500, moveListener);
        timer.setRepeats(true);
        timer.start();

        startWar();
    }

    private void startWar() {

        ActionListener warListener = e -> {
            gameBoard.war();
            setTitle(title + gameBoard.getScore() + lives + gameBoard.getShooter().getLives());
        };
        warTimer = new Timer(1000, warListener);
        warTimer.setRepeats(true);
        warTimer.start();
    }

    private void stopGame() {
        timer.stop();
        warTimer.stop();
    }

    @Override
    public void paint(Graphics g) {

        gameBoard.repaint();
    }

    private void setWindowProperties(int width, int height) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(title + gameBoard.getScore() + lives + gameBoard.getShooter().getLives());
        setSize(width, height);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent keyEvent) {

            if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                gameBoard.movement = Direction.LEFT;
                gameBoard.moveShooter();
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                gameBoard.movement = Direction.RIGHT;
                gameBoard.moveShooter();
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
                gameBoard.shoot(gameBoard.getShooter());
                repaint();
                setTitle(title + gameBoard.getScore() + lives + gameBoard.getShooter().getLives() );
            }

        }
    }

    public static void main(String[] args) {
        new SpaceInvaders().setVisible(true);
    }
}