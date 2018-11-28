import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

class GameBoard extends JComponent {
    protected int cellSize;
    private Shooter shooter;
    private Entity projectile;
    private int score = 0;
    private ArrayList<Alien> aliens;

    public Entity[][] getGameBoard() {
        return gameBoard;
    }

    private Entity[][] gameBoard;
    public final int BOARD_ROWS = 12;
    public final int BOARD_COLS = 15;
    public Direction movement;
    Graphics2D g;
    public boolean shooting;
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

    GameBoard(){
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

    public Direction moveAliens(Direction dir) {
        Direction newDir = dir;
        HashSet<Integer> columns = new HashSet<>();
        ArrayList<Alien> old = aliens;
        for (int i = 0; i < aliens.size(); i++) {
            columns.add(aliens.get(i).getCol());
        }
        if (dir == Direction.LEFT) {
            if (!columns.contains(0)) {
                for (int j = 0; j < aliens.size(); j++) {
                    int currentCol = old.get(j).getCol();
                    int currentRow = old.get(j).getRow();
                    aliens.get(j).setCol(currentCol - 1);
                    gameBoard[currentCol - 1][currentRow] = aliens.get(j);
                    gameBoard[currentCol][currentRow] = new Empty(currentCol, currentRow);
                }
                if(columns.contains(1)){
                    newDir = Direction.RIGHT;
                }
            }
        } else {
            if (!columns.contains(BOARD_COLS - 1)) {
                for (int k = 0; k < aliens.size(); k++) {
                    int currentCol = old.get(k).getCol();
                    int currentRow = old.get(k).getRow();
                    aliens.get(k).setCol(currentCol + 1);
                    gameBoard[currentCol + 1][currentRow] = aliens.get(k);
                    gameBoard[currentCol][currentRow] = new Empty(currentCol, currentRow);
                }
            } else {
                for (int l = 0; l < aliens.size(); l++) {
                    int currentRow = old.get(l).getRow();
                    int currentCol = old.get(l).getCol();
                    aliens.get(l).setRow(currentRow + 1);
                    gameBoard[currentCol][currentRow + 1] = aliens.get(l);
                    gameBoard[currentCol][currentRow] = new Empty(currentCol, currentRow);
                    newDir = Direction.LEFT;
                }
            }
        }
        return  newDir;
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
        if (shooting) {
            paintShot(g);
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

    public void moveShooter() {
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


    private void exit() {
        System.out.println("Final Score: " + getScore());
        System.exit(0);
    }

    int getScore() {
        return score;
    }


    private boolean removeAlienIfShot() {
        for (int col = 0; col < BOARD_COLS; col++) {
            for (int row = 0; row < BOARD_ROWS; row++) {
                Entity entity = gameBoard[col][row];
                if (entity instanceof Alien) {
                    Alien alien = (Alien) entity;
                    shooting = true;
                    if (projectile.getCol() == alien.getCol() && projectile.getRow() == alien.getRow()) {
                        alien.setAlive(false);
                        deadAliens++;
                        aliens.remove(alien);
                        score += 10;
                        gameBoard[col][row] = new Empty(col, row);
                        repaint();
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private void paintShot(Graphics2D g) {
        g.setColor(Color.GREEN);
        g.drawLine((shooter.getCol() * cellSize) + cellSize / 2,
                (shooter.getRow() * cellSize) + cellSize / 2,
                (projectile.getCol() * cellSize) + cellSize / 2,
                (projectile.getRow() * cellSize) + cellSize / 2);
        sleep();

    }

    public void shoot() {
        for (int loc = BOARD_ROWS - 1; loc >= 0; loc--) {
            projectile = new Projectile(shooter.getCol(), loc);
            if (removeAlienIfShot()) {
                if (isGameOver()) {
                    exit();
                }
                break;
            }
        }
    }

    public boolean isGameOver() {
        HashSet<Integer> rows = new HashSet<>();
        boolean over = false;
        for (int i = 0; i < aliens.size(); i++) {
            rows.add(aliens.get(i).getRow());
        }
        if (rows.contains(BOARD_ROWS - 1) || allAliensDead()) {
            over = true;
        }
        return over;
    }

    private boolean allAliensDead() {
        return allAliens == deadAliens;
    }

    private void sleep() {
        try {
            Thread.sleep(10);
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

    public Shooter getShooter() {
        return shooter;
    }
}