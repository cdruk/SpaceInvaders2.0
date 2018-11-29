import entities.*;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class GameBoardTest {
    GameBoard board = new GameBoard(40);


    @Test
    public void generateAliensTest() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < board.BOARD_COLS - 5; col++) {
                Entity entity = board.getGameBoard()[col][row];
                assertTrue(entity instanceof Alien);
                assertTrue(entity.getCol() == col);
                assertTrue(entity.getRow() == row);
            }

        }
    }

    @Test
    public void moveShooterTest() {
        Shooter shooter = board.getShooter();
        int oldCol = shooter.getCol();
        int oldRow = shooter.getRow();
        assertTrue(board.getGameBoard()[oldCol][oldRow] instanceof Shooter);
        board.movement = Direction.RIGHT;
        board.moveShooter();
        int newCol = shooter.getCol();
        assertTrue(newCol == oldCol + 1);
        int newRow = shooter.getRow();
        assertTrue(oldRow == newRow);
        assertTrue(board.getGameBoard()[newCol][newRow] instanceof Shooter);
        assertTrue(board.getGameBoard()[oldCol][oldRow] instanceof Empty);
    }

}
