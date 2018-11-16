/**
 * Represents a square on the board. Each Square has an Entity. An Entity
 * is what is on the Square: either there is the shooter, an alien, the projectile,
 * or it is empty.
 */
public class Square {

    private Entity entity;
    private int x;
    private int y;


    /**
     * Construct the Square with nothing on it.
     *
     * @param x the x coordinate of the square
     * @param y the y coordinate of the square
     */
    public Square(int x, int y) {
        this(Entity.Empty, x, y);
    }

    /**
     * Construct the Square with a specified entity.
     *
     * @param entity the entity that is on the square
     * @param x      the x coordinate of the square
     * @param y      the y coordinate of the square
     */
    Square(Entity entity, int x, int y) {
        this.entity = entity;
        this.x = x;
        this.y = y;
    }

    /**
     * Change what is on the Square.
     *
     * @param entity the new entity
     */
    void setEntity(Entity entity) {

        this.entity = entity;
    }

    /**
     * Get what is on the Square.
     *
     * @return the entity on the Square
     */
    Entity getEntity() {
        return entity;
    }

    int getX() {
        return x;
    }

    int getY() {

        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Square)) {
            return false;
        }

        Square sq = (Square) obj;
        return sq.x == x && sq.y == y;
    }

    @Override
    public String toString() {
        return entity + " at (" + x + ", " + y + ")";
    }

    /**
     * Represents what is on a particular square.
     */
    protected enum Entity {
        Empty,
        Shooter,
        Alien,
        Projectile
    }
}