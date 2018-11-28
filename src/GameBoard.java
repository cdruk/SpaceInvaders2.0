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
    private Projectile projectile;
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
        for (Alien alien : aliens) {
            columns.add(alien.getCol());
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
                            int index = getAlienInt(currentCol, currentRow);
                            aliens.get(index).setCol(currentCol -1);
                            newGrid[currentCol - 1][currentRow] = aliens.get(index);
                        }

                    if (columns.contains(1)) {
                        newDir = Direction.RIGHT;
                    }
                } else {
                        if (!columns.contains(BOARD_COLS - 1)) {
                            int currentCol = current.getCol();
                            int currentRow = current.getRow();
                            int index = getAlienInt(currentCol, currentRow);
                            aliens.get(index).setCol(currentCol + 1);
                            newGrid[currentCol + 1][currentRow] = aliens.get(index);

                    } else {
                            int currentRow = current.getRow();
                            int currentCol = current.getCol();
                            int index = getAlienInt(currentCol, currentRow);
                            aliens.get(index).setRow(currentRow + 1);
                            newGrid[currentCol][currentRow + 1] = aliens.get(index);
                            newDir = Direction.LEFT;
                        }
                    }

                }else if(current instanceof Projectile){
                    Projectile proj = new Projectile(current.getCol(), current.getRow());
                    newGrid[j][i] = proj;
                }else if(current instanceof Shooter){
                    Shooter shoot = new Shooter(current.getCol(), current.getRow());
                    newGrid[j][i] = shoot;
                }
            }
            this.gameBoard = newGrid;

        }
        return newDir;
    }

    private int getAlienInt(int currentCol, int currentRow) {
        int index = 0;
        for(int i = 0; i < aliens.size(); i++){
            if(aliens.get(i).getCol() == currentCol && aliens.get(i).getRow() == currentRow) {
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

    void shoot() {
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

    private boolean isGameOver() {
        HashSet<Integer> rows = new HashSet<>();
        boolean over = false;
        for (Alien alien : aliens) {
            rows.add(alien.getRow());
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

    Shooter getShooter() {
        return shooter;
    }

    ArrayList<Alien> getAliens() {
        return aliens;
    }
}