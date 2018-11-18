///**
// * Represents a square on the board. Each Square has an Entity. An Entity
// * is what is on the Square: either there is the shooter, an alien, the projectile,
// * or it is empty.
// */
//public class Square {
//
//    private Entity entity;
//    private int row;
//    private int col;
//
//
//    /**
//     * Construct the Square with nothing on it.
//     *
//     * @param row the row coordinate of the square
//     * @param col the col coordinate of the square
//     */
//    public Square(int row, int col) {
//        this(Entity.Empty, row, col);
//    }
//
//    /**
//     * Construct the Square with a specified entity.
//     *
//     * @param entity the entity that is on the square
//     * @param row      the row coordinate of the square
//     * @param col      the col coordinate of the square
//     */
//    Square(Entity entity, int row, int col) {
//        this.entity = entity;
//        this.row = row;
//        this.col = col;
//    }
//
//    /**
//     * Change what is on the Square.
//     *
//     * @param entity the new entity
//     */
//    void setEntity(Entity entity) {
//
//        this.entity = entity;
//    }
//
//    /**
//     * Get what is on the Square.
//     *
//     * @return the entity on the Square
//     */
//    Entity getEntity() {
//        return entity;
//    }
//
//    int getRow() {
//        return row;
//    }
//
//    int getCol() {
//
//        return col;
//    }
//
//    public void setRow(int row) {
//        this.row = row;
//    }
//
//    public void setCol(int col) {
//        this.col = col;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//
//        if (!(obj instanceof Square)) {
//            return false;
//        }
//
//        Square sq = (Square) obj;
//        return sq.row == row && sq.col == col;
//    }
//
//    @Override
//    public String toString() {
//        return entity + " at (" + row + ", " + col + ")";
//    }
//
//}