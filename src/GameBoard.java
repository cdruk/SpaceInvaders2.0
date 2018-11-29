import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

class GameBoard extends JComponent {
    private int cellSize;
    private Shooter shooter;
    private Projectile shooterProjectile;
    private int score = 0;
    private ArrayList<Alien> aliens;
    private Graphics2D g;

    Entity[][] getGameBoard() {
        return gameBoard;
    }

    private Entity[][] gameBoard;
    final int BOARD_ROWS = 12;
    final int BOARD_COLS = 15;
    Direction movement;
    private boolean shooterShooting;
    private Image alienPic;
    private Image shooterPic;
    private int allAliens;
    private int deadAliens;

    GameBoard(int cellSize) {
        gameBoard = new Entity[BOARD_COLS][BOARD_ROWS];
        this.cellSize = cellSize;
        alienPic = createAlienPic();
        shooterPic = createShooterPic();
        generateGameBoard();
        generateAliens();
        generateShooter();
    }

    GameBoard() {
        gameBoard = new Entity[BOARD_COLS][BOARD_ROWS];
        generateGameBoard();
        generateAliens();
        generateShooter();
    }

    private void generateGameBoard() {
        for (int col = 0; col < BOARD_COLS; col++) {
            for (int row = 0; row < BOARD_ROWS; row++) {
                Empty empty = new Empty(col, row);
                gameBoard[col][row] = empty;
            }
        }
    }

    private void generateAliens() {
        aliens = new ArrayList<>();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < BOARD_COLS - 5; col++) {
                Alien alien = new Alien(col, row);
                aliens.add(alien);
                gameBoard[col][row] = alien;
                allAliens++;
            }
        }
    }

    Direction moveAliens(Direction dir) {
        Direction newDir = dir;
        HashSet<Integer> columns = new HashSet<>();
        ArrayList<Alien> oldAliens = new ArrayList<>();
        for (Alien alien : aliens) {
            columns.add(alien.getCol());
            oldAliens.add(alien);
        }

        Entity[][] oldGrid = gameBoard;
        Entity[][] newGrid = new Entity[BOARD_COLS][BOARD_ROWS];
        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int col = 0; col < BOARD_COLS; col++) {
                Empty empty = new Empty(col, row);
                newGrid[col][row] = empty;
            }
        }
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
                Entity current = oldGrid[j][i];
                if (current instanceof Alien) {
                    if (dir == Direction.LEFT) {
                        if (!columns.contains(0)) {
                            int currentCol = current.getCol();
                            int currentRow = current.getRow();
                            int index = getAlienInt(oldAliens, currentCol, currentRow);
                            aliens.get(index).setCol(currentCol - 1);
                            newGrid[currentCol - 1][currentRow] = aliens.get(index);
                        }

                        if (columns.contains(1)) {
                            newDir = Direction.RIGHT;
                        }
                    } else {
                        if (!columns.contains(BOARD_COLS - 1)) {
                            int currentCol = current.getCol();
                            int currentRow = current.getRow();
                            int index = getAlienInt(oldAliens, currentCol, currentRow);
                            aliens.get(index).setCol(currentCol + 1);
                            newGrid[currentCol + 1][currentRow] = aliens.get(index);

                        } else {
                            int currentRow = current.getRow();
                            int currentCol = current.getCol();
                            int index = getAlienInt(oldAliens, currentCol, currentRow);
                            aliens.get(index).setRow(currentRow + 1);
                            newGrid[currentCol][currentRow + 1] = aliens.get(index);
                            newDir = Direction.LEFT;
                        }
                    }

                } else if (current instanceof Projectile) {
                    Projectile proj = new Projectile(current.getCol(), current.getRow());
                    newGrid[j][i] = proj;
                } else if (current instanceof Shooter) {
                    Shooter shoot = new Shooter(current.getCol(), current.getRow());
                    newGrid[j][i] = shoot;
                }
            }
            this.gameBoard = newGrid;

        }
        return newDir;
    }

    private int getAlienInt(ArrayList<Alien> oldAliens, int currentCol, int currentRow) {
        int index = 0;
        for (int i = 0; i < oldAliens.size(); i++) {
            if (oldAliens.get(i).getCol() == currentCol && oldAliens.get(i).getRow() == currentRow) {
                index = i;
            }
        }
        return index;
    }


    private void generateShooter() {
        shooter = new Shooter((BOARD_COLS - 1) / 2, BOARD_ROWS - 1);
        gameBoard[(BOARD_COLS - 1) / 2][BOARD_ROWS - 1] = shooter;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        g = (Graphics2D) graphics;
        g.setColor(Color.black);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintAliens(g);
        paintShooter(g);
        for (int col = 0; col < BOARD_COLS; col++) {
            for (int row = 0; row < BOARD_ROWS; row++) {
                Entity entity = gameBoard[col][row];
                if (entity instanceof Empty) {
                    g.fillRect(entity.getCol() * cellSize, entity.getRow() * cellSize, cellSize, cellSize);
                }
            }
        }
        if (shooterShooting) {
            paintShot(g, shooter);
        }
    }

    private void paintAliens(Graphics2D g) {
        for (Alien alien : aliens) {
            g.drawImage(alienPic, alien.getCol() * cellSize, alien.getRow() * cellSize, null);
        }
    }


    private void paintShooter(Graphics2D g) {
        g.drawImage(shooterPic, shooter.getCol() * cellSize, shooter.getRow() * cellSize, null);

    }

    void moveShooter() {
        Entity empty = new Empty(shooter.getCol(), shooter.getRow());
        if (movement == Direction.LEFT && !atLeftBounds()) {
            gameBoard[shooter.getCol()][shooter.getRow()] = empty;
            shooter.setCol(shooter.getCol() - 1);
            gameBoard[shooter.getCol()][shooter.getRow()] = shooter;

        } else if (movement == Direction.RIGHT && !atRightBounds()) {
            gameBoard[shooter.getCol()][shooter.getRow()] = empty;
            shooter.setCol(shooter.getCol() + 1);
            gameBoard[shooter.getCol()][shooter.getRow()] = shooter;
        }
        repaint();
    }


    private boolean atLeftBounds() {
        return shooter.getCol() == 0;
    }

    private boolean atRightBounds() {
        return shooter.getCol() == BOARD_COLS - 1;
    }


    void exit() {
        System.out.println("Final Score: " + getScore());
        System.exit(0);
    }

    int getScore() {
        return score;
    }


    private boolean removeAlienIfShot(int col, int row) {
        for (Alien alien : aliens) {
            if (col == alien.getCol() && row == alien.getRow()) {
                shooterProjectile = null;
                shooterShooting = false;
                timer.removeActionListener(advanceProjListener);
                repaint();
                alien.setAlive(false);
                deadAliens++;
                aliens.remove(alien);
                score += 10;
                gameBoard[alien.getCol()][alien.getRow()] = new Empty(alien.getCol(), alien.getRow());
                return true;
            }
        }

        return false;
    }


    void paintShot(Graphics2D g, Entity shooter) {
        g.setColor(Color.GREEN);
        if (shooter instanceof Shooter) {
            g.drawLine((shooterProjectile.getCol() * cellSize) + cellSize / 2,
                    shooterProjectile.getRow() * cellSize,
                    (shooterProjectile.getCol() * cellSize) + cellSize / 2,
                    (shooterProjectile.getRow() - 1) * cellSize);
        }
        if (shooter instanceof Alien) {
            g.drawLine((shooterProjectile.getCol() * cellSize) + cellSize / 2,
                    (shooterProjectile.getRow() * cellSize) + cellSize / 2,
                    (shooterProjectile.getCol() * cellSize) + cellSize / 2,
                    ((shooterProjectile.getRow() + 1) * cellSize) + cellSize / 2);

        }

    }

    int row;
    Timer timer;
    int col;
    ActionListener advanceProjListener;

    void shoot() {
        row = BOARD_ROWS - 1;
        col = shooter.getCol();
        advanceProjListener = e -> {
            row--;
            shooterProjectile = new Projectile(col, row);
            shooterShooting = true;
            repaint();
            if (removeAlienIfShot(col, row)) {
                if (isGameOver()) {
                    exit();
                }
                sleep();
            }
        };
        timer = new Timer(15, advanceProjListener);
        timer.setRepeats(true);
        timer.start();

    }

    boolean isGameOver() {
        HashSet<Integer> rows = new HashSet<>();
        boolean over = false;
        for (Alien alien : aliens) {
            rows.add(alien.getRow());
        }
        if (rows.contains(BOARD_ROWS - 2) || allAliensDead()) {
            over = true;
        }
        return over;
    }

    private boolean allAliensDead() {
        return allAliens == deadAliens;
    }

    void sleep() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }


    private Image createAlienPic() {
        File imageFile = new File("alien.jpg");
        try {
            return resize(ImageIO.read(imageFile), cellSize, cellSize);
        } catch (IOException e) {
            System.out.println("Image not found.");
        }
        return null;
    }

    private Image createShooterPic() {
        File icon = new File("shooter.jpg");
        try {
            return ImageIO.read(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    Shooter getShooter() {
        return shooter;
    }

}