

public class Alien extends Entity{

    private boolean alive = true;

    public Alien(int col, int row) {
        setCol(col);
        setRow(row);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

}