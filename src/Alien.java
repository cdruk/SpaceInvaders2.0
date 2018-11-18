public class Alien extends Entity{

    private boolean alive = true;

    public Alien(int row, int col) {
        setRow(row);
        setCol(col);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

}