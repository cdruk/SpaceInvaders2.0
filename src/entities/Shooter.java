package entities;

import entities.Entity;

public class Shooter extends Entity {
    private int lives = 3;

    public Shooter(int col, int row) {
        setCol(col);
        setRow(row);
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
}