import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;

public class GameBoardTest {
    GameBoard board = new GameBoard();

    @Test
    public void moveAliensTest() {
        Entity[][] oldBoard = new Entity[board.BOARD_COLS][board.BOARD_ROWS];
        ArrayList<Alien> oldAliens = new ArrayList<>();
        for(Alien alien : board.getAliens()){
            oldAliens.add(alien);
        }
        for (int i = 0; i < board.BOARD_ROWS; i++){
            for(int j = 0; j < board.BOARD_COLS; j++){
                oldBoard[j][i] = board.getGameBoard()[j][i];
            }
        }
        board.moveAliens(Direction.RIGHT);
        for(int num = 0; num < board.getAliens().size(); num ++){
            int oldCol = oldBoard[oldAliens.get(num).getCol()][oldAliens.get(num).getRow()].getCol();
            int newCol = board.getGameBoard()[board.getAliens().get(num).getCol()][board.getAliens().get(num).getRow()].getCol();
            assertTrue(newCol == (oldCol + 1));
        }

    }


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
    public void moveShooterTest(){
        Shooter shooter = board.getShooter();
        int oldCol = shooter.getCol();
        int oldRow = shooter.getRow();
        assertTrue(board.getGameBoard()[oldCol][oldRow] instanceof  Shooter);
        board.movement = Direction.RIGHT;
        board.moveShooter();
        int newCol = shooter.getCol();
        assertTrue(newCol == oldCol + 1);
        int newRow = shooter.getRow();
        assertTrue(oldRow == newRow);

        assertTrue(board.getGameBoard()[newCol][newRow] instanceof  Shooter);
        assertTrue(board.getGameBoard()[oldCol][oldRow] instanceof  Empty);
    }

}
