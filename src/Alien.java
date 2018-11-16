public class Alien {
    //location
    private int col;
    private int row;



    //dead/alive
    private boolean alive = true;

    public Alien(int row, int col) {
        this.row = row;
        this.col = col;
    }



    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

}