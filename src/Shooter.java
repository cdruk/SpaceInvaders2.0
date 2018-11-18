import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

class Shooter  extends Entity{
    private boolean exists;

    public Shooter(int row, int col) {
        setRow(row);
        setCol(col);
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

}