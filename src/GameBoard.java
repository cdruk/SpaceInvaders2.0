import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class GameBoard {
    protected int cellSize;
    private Entity shooter;
    private Projectile projectile;
    private int score = 0;
    private ArrayList<Entity> gameBoard;
    private ImageObserver imgObs;
    public final int BOARD_ROWS = 12;
    public final int BOARD_COLS = 15;
    public Direction movement;
    Graphics2D g;
    public boolean shooting;
    private Image alienPic;
    private Image shooterPic;
    private boolean gameOver;

    GameBoard(int cellSize) {
        this.cellSize = cellSize;

        alienPic = createAlienPic();
        shooterPic = createShooterPic();
        gameBoard = new ArrayList<Entity>();
        generateGameBoard();
        generateAliens();
        generateShooter();
    }

    private void generateGameBoard() {
        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int col = 0; col < BOARD_COLS; col++) {
                Entity entity = new Empty(row, col);
                gameBoard.add(entity);
            }
        }
    }

    private void generateAliens() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < BOARD_COLS - 5; col++) {
                int i = getSquareIndex(row, col);
                Entity alien = new Alien(row, col);
                gameBoard.set(i, alien);
            }
        }
    }


    private void generateShooter() {
        shooter = new Shooter(BOARD_ROWS - 1, (BOARD_COLS - 1) / 2);
        gameBoard.set(getSquareIndex(shooter.getRow(), shooter.getCol()), shooter);

    }

    void paint(Graphics graphics) {

        g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //paintGameboard(g); maybe have each square paint itself?
        for (Entity entity : gameBoard) {
            if (entity instanceof Alien) {
                paintAliens(g, entity);
            } else if (entity instanceof Shooter) {
                paintShooter(g);
            }
        }
//        paintAliens(g);
//        paintShooter(g);
//        if (shooting) {
//            paintShot(g);
//        }
    }

    private void paintAliens(Graphics2D g, Entity alien) {

//        if (isAlive()) {
            g.drawImage(alienPic, alien.getCol() * cellSize, alien.getRow() * cellSize, imgObs);
//        }
    }


    private void paintShooter(Graphics2D g) {
        g.drawImage(shooterPic, shooter.getCol() * cellSize, shooter.getRow() * cellSize, imgObs);

    }

 /*   public void moveShooter() {

        Square current = shooter.getLocation();
        Square newLoc;
        boolean[] bounds = checkBounds();
        if (movement == Direction.LEFT && !bounds[0]) {
            newLoc = gameBoard.get(getSquareIndex(current.getCol() - 1, current.getRow()));
            shooter.setLocation(newLoc);
        } else if (movement == Direction.RIGHT && !bounds[1]) {
            newLoc = gameBoard.get(getSquareIndex(current.getCol() + 1, current.getRow()));
            shooter.setLocation(newLoc);
        }
        shooter.getLocation().setEntity(Square.Entity.Shooter);
        Square shot = new Square(Square.Entity.Projectile, shooter.getLocation().getCol(), shooter.getLocation().getRow());
        projectile = new Projectile(shot);
    }*/


  /*  private boolean[] checkBounds() {
        Square sq = shooter.getLocation();
        boolean tooFarLeft = sq.getCol() == 0;
        boolean tooFarRight = sq.getCol() == BOARD_COLS - 1;
        boolean[] bounds = {tooFarLeft, tooFarRight};
        return bounds;
    }*/


    private void exit() {
        System.out.println("Final Score: " + getScore());
        System.exit(0);
    }

    int getScore() {
        return score;
    }


    /*private boolean removeAlienIfShot() {

        boolean dead = false;
        for (int i = aliens.size() - 1; i >= 0; i--) {
            int squareLoc = getSquareIndex(aliens.get(i).getRow(), aliens.get(i).getCol());

            if (squareLoc != -1) {

                Square alien = gameBoard.get(squareLoc);
                shooting = true;
                if (projectile.getLocation().equals(alien)) {
                    alien.setEntity(Square.Entity.Empty);
                    Alien deadAlien = aliens.get(i);
                    deadAlien.setAlive(false);
                    aliens.remove(i);
                    dead = true;
                    score += 10;
                    break;

                }
            }
        }
        return dead;
    }*/


    private int getSquareIndex(int row, int col) {
        for (int i = 0; i < gameBoard.size(); i++) {
            if (gameBoard.get(i).getCol() == row) {
                if (gameBoard.get(i).getRow() == col) {
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

/*    private void paintShot(Graphics2D g) {
        g.setColor(Color.GREEN);
        g.drawLine((shooter.getLocation().getCol() * cellSize) + cellSize / 2,
                (shooter.getLocation().getRow() * cellSize) + cellSize / 2,
                (projectile.getLocation().getRow() * cellSize) + cellSize / 2,
                (projectile.getLocation().getCol() * cellSize) + cellSize / 2);
        sleep();

    }*/

 /*   public void shoot() {
        for (int loc = BOARD_ROWS - 1; loc >= 0; loc--) {
            Square current = new Square(Square.Entity.Projectile, shooter.getLocation().getCol(), loc);
            projectile = new Projectile(current);

            if (removeAlienIfShot()) {

                if (isGameOver()) {
                    exit();
                }
                break;
            }
        }

    }*/


/*    public boolean isGameOver() {
        return aliens.isEmpty() || gameOver;
    }*/

    private void sleep() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /*public void nextRound() {
        Square square;
        for (int i = 0; i < gameBoard.size(); i++) {
            square = gameBoard.get(i);
            square.setCol(square.getCol() + 1);
            int row = aliens.get(i).getRow();
            aliens.get(i).setRow(row + 1);
            if (square.getRow() == BOARD_ROWS - 2 && square.getEntity() == Square.Entity.Alien) {
                gameOver = true;
            }
        }

    }*/
}