package spaceinvaders;

import spaceinvaders.GameBoard;
import spaceinvaders.entities.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class SpaceInvaders extends JFrame {

    private static final int SQUARE_SIZE = 40;
    private GameBoard gameBoard;
    private Direction alienDir;
    final private String TITLE;
    final private String LIVES;
    private Timer timer;
    private Timer warTimer;

    private SpaceInvaders() {
        TITLE = "Space Invaders - Score: ";
        LIVES = "               Lives: ";
        gameBoard = new GameBoard(SQUARE_SIZE);
        gameBoard.setBackground(Color.black);
        final int CANVAS_WIDTH = SQUARE_SIZE * gameBoard.BOARD_COLS;
        final int CANVAS_HEIGHT = SQUARE_SIZE * gameBoard.BOARD_ROWS;
        setWindowProperties(CANVAS_WIDTH, CANVAS_HEIGHT + 10);
        JComponent mainPanel = gameBoard;
        add(mainPanel);
        addKeyListener(new buttonPressAdapter());
        runGame();
        if (gameBoard.isGameOver()) {
            gameBoard = null;
        }
    }


    private void runGame() {
        alienDir = Direction.RIGHT;
        ActionListener moveListener = e -> {
            alienDir = gameBoard.moveAliens(alienDir);
            if (gameBoard.isGameOver()) {
                stopGame();
            }
            else
            {
                gameBoard.repaint();
            }
        };

        timer = new Timer(950, moveListener);

        timer.setRepeats(true);
        timer.start();

        startWar();
    }

    private void startWar() {

        if (gameBoard.isGameOver()) {
            stopGame();
        }

        ActionListener warListener = e -> {
            gameBoard.war();
            setTitle(TITLE + gameBoard.getScore() + LIVES + gameBoard.getShooter().getLives());
            alienDir = gameBoard.moveAliens(alienDir);

        };

        warTimer = new Timer(1000, warListener);
        warTimer.setRepeats(true);
        warTimer.start();

    }

    private void stopGame() {
        setTitle(TITLE + gameBoard.getScore() + LIVES + gameBoard.getShooter().getLives());
        timer.stop();
        warTimer.stop();
        gameBoard = null;

    }

    @Override
    public void paint(Graphics g) {
        if (gameBoard != null) {
            gameBoard.repaint();
        }
    }

    private void setWindowProperties(int width, int height) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(TITLE + gameBoard.getScore() + LIVES + gameBoard.getShooter().getLives());
        setSize(width, height);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private class buttonPressAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if (gameBoard != null) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                    gameBoard.movement = Direction.LEFT;
                    gameBoard.moveShooter();
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                    gameBoard.movement = Direction.RIGHT;
                    gameBoard.moveShooter();
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
                    gameBoard.shoot(gameBoard.getShooter());
                    repaint();
                    setTitle(TITLE + gameBoard.getScore() + LIVES + gameBoard.getShooter().getLives());
                }
            }
        }
    }

    public static void main(String[] args) {
        new SpaceInvaders().setVisible(true);
    }
}