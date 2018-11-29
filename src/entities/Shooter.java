package entities;

public class Shooter extends Entity {
    private int lives = 3;

    public Shooter(int col, int row) {
        setCol(col);
        setRow(row);
    }


    public boolean atLeftBounds() {
        return this.getCol() == 0;
    }

    public boolean atRightBounds(int col) {
        return this.getCol() == col - 1;
    }


    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
}