import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

class Shooter extends Entity{
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