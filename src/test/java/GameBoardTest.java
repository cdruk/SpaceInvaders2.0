
import org.junit.Test;
import spaceinvaders.*;
import spaceinvaders.entities.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class GameBoardTest {
    private GameBoard board = new GameBoard(40);


    @Test
    public void generateAliensTest() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < board.BOARD_COLS - 5; col++) {
                Entity entity = board.getGameBoard()[col][row];
                assertTrue(entity instanceof Alien);
                assertEquals(entity.getCol(), col);
                assertEquals(entity.getRow(), row);
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
        assertEquals(newCol, oldCol + 1);
        int newRow = shooter.getRow();
        assertEquals(oldRow, newRow);
        assertTrue(board.getGameBoard()[newCol][newRow] instanceof Shooter);
        assertTrue(board.getGameBoard()[oldCol][oldRow] instanceof Empty);
    }

}
