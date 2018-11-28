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
    private Entity shooter;
    private Entity projectile;
    private int score = 0;
    private ArrayList<Entity> gameBoard;
    private ArrayList<Alien> aliens;
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
        this.cellSize = cellSize;
        alienPic = createAlienPic();
        shooterPic = createShooterPic();
        gameBoard = new ArrayList<>();
        generateGameBoard();
        generateAliens();
        generateShooter();
    }

    private void generateGameBoard() {
        for (int col = 0; col < BOARD_COLS; col++) {
            for (int row = 0; row < BOARD_ROWS; row++) {
                Entity entity = new Empty(col, row);
                gameBoard.add(entity);
            }
        }
    }

    private void generateAliens() {
        aliens = new ArrayList<>();
        for (int col = 2; col < BOARD_COLS - 3; col++) {
            for (int row = 0; row < 5; row++) {
                int i = getSquareIndex(col, row);
                Alien alien = new Alien(col, row);
                aliens.add(alien);
                gameBoard.set(i, alien);
                allAliens++;
            }
        }
    }

    public void moveAliens(Direction dir){
        HashSet<Integer> columns = new HashSet<>();
        for(int i = 0; i < aliens.size(); i++){
            columns.add(aliens.get(i).getCol());
        }
        if(dir == Direction.LEFT){
            if(!columns.contains(BOARD_COLS)) {
                for (int j = 0; j < aliens.size(); j++) {
                    int current = aliens.get(j).getCol();
                    aliens.get(j).setCol(current + 1);
                }
            }else{
                for(int l = 0; l < aliens.size(); l++){
                    int current = aliens.get(l).getRow();
                    aliens.get(l).setRow(current + 1);
                }
            }
        }
        if(!columns.contains(0) && dir == Direction.RIGHT){
            for(int k = 0; k < aliens.size(); k++){
                int current = aliens.get(k).getCol();
                aliens.get(k).setCol(current - 1);
            }
        }
    }

    private void generateShooter() {
        shooter = new Shooter((BOARD_COLS - 1) / 2, BOARD_ROWS - 1);
        gameBoard.set(getSquareIndex(shooter.getCol(), shooter.getRow()), shooter);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        g = (Graphics2D) graphics;
        g.setColor(Color.black);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Entity entity : gameBoard) {
            if (entity instanceof Alien) {
                paintAliens(g, entity);
            } else if (entity instanceof Shooter) {
                paintShooter(g);
            }else if(entity instanceof Empty){
                g.fillRect(entity.getCol() * cellSize, entity.getRow() * cellSize, cellSize, cellSize);
            }
        }
        if (shooting) {
            paintShot(g);
        }
    }

    private void paintAliens(Graphics2D g, Entity alien) {
        g.drawImage(alienPic, alien.getCol() * cellSize, alien.getRow() * cellSize, null);

}


    private void paintShooter(Graphics2D g) {
        g.drawImage(shooterPic, shooter.getCol() * cellSize, shooter.getRow() * cellSize, null);

    }

    public void moveShooter() {
        int oldLoc = getSquareIndex(shooter.getCol(),shooter.getRow());
        Entity empty = new Empty(shooter.getCol(),shooter.getRow());
        int newLoc;

        if (movement == Direction.LEFT && !atLeftBounds()) {
            newLoc = getSquareIndex(shooter.getCol() - 1, shooter.getRow());
            gameBoard.set(newLoc, shooter);
            shooter.setCol(shooter.getCol()-1);
            gameBoard.set(oldLoc, empty);
        } else if (movement == Direction.RIGHT && !atRightBounds()) {
            newLoc = getSquareIndex(shooter.getCol() + 1, shooter.getRow());
            gameBoard.set(newLoc, shooter);
            shooter.setCol(shooter.getCol()+1);
            gameBoard.set(oldLoc, empty);
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
        for (Entity entity : gameBoard) {
            if (entity instanceof Alien) {
                Alien alien = (Alien) entity;
                shooting = true;
                if (projectile.getCol() == alien.getCol() && projectile.getRow() == alien.getRow()) {
                    alien.setAlive(false);
                    deadAliens++;
                    aliens.remove(alien);
                    score += 10;
                    int i = getSquareIndex(projectile.getCol(), projectile.getRow());
                    gameBoard.set(i, new Empty(projectile.getCol(), projectile.getRow()));
                    repaint();
                    return true;
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
        for(int i = 0; i < aliens.size(); i++){
            rows.add(aliens.get(i).getRow());
        }
        if(rows.contains(BOARD_ROWS - 1) || allAliensDead()){
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



    private int getSquareIndex(int col, int row) {
        for (int i = 0; i < gameBoard.size(); i++) {
            if (gameBoard.get(i).getCol() == col) {
                if (gameBoard.get(i).getRow() == row) {
                    return i;
                }
            }
        }
        return -1;
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
}