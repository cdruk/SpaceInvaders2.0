import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class GameBoard
{
	protected int cellSize;
	private Entity shooter;
	private Entity projectile;
	private int score = 0;
	private ArrayList<Entity> gameBoard;
	private ImageObserver imgObs;
	public final int BOARD_ROWS = 12;
	public final int BOARD_COLS = 15;
	public Direction movement;
	Graphics2D g;
	public boolean shooting;
	private Image alienPic;
	private Image shooterPic;
	private boolean gameOver;
	private int allAliens;
	private int deadAliens;

	GameBoard(int cellSize)
	{
		this.cellSize = cellSize;

		alienPic = createAlienPic();
		shooterPic = createShooterPic();
		gameBoard = new ArrayList<Entity>();
		generateGameBoard();
		generateAliens();
		generateShooter();
	}

	private void generateGameBoard()
	{
		for (int col = 0; col < BOARD_COLS; col++)
		{
			for (int row = 0; row < BOARD_ROWS; row++)
			{
				Entity entity = new Empty(col, row);
				gameBoard.add(entity);
			}
		}
	}

	private void generateAliens()
	{
		for (int col = 0; col < BOARD_COLS - 5; col++)
		{
			for (int row = 0; row < 5; row++)
			{
				int i = getSquareIndex(col, row);
				Entity alien = new Alien(col, row);
				gameBoard.set(i, alien);
				allAliens++;
			}
		}
	}

	public void moveAlienColRight()
	{
		for (int i = gameBoard.size() - 1; i > -1; i--)
		{

					Entity entity = gameBoard.get(i);
					Entity rightNeighbor = null;
					if (!atRightBounds(entity)){
                    rightNeighbor = gameBoard.get(getSquareIndex(entity.getCol() + 1, entity.getRow()));}
                if (entity.getClass() == Alien.class && rightNeighbor.getClass() == Empty.class)
					{
						entity.setCol(entity.getCol() + 1);
					}
				}

	}


	private void generateShooter()
	{
		shooter = new Shooter((BOARD_COLS - 1) / 2, BOARD_ROWS - 1);
		gameBoard.set(getSquareIndex(shooter.getCol(), shooter.getRow()), shooter);
	}

	void paint(Graphics graphics)
	{

		g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		//paintGameboard(g); maybe have each square paint itself?
		for (Entity entity : gameBoard)
		{
			if (entity instanceof Alien)
			{
				paintAliens(g, entity);
			}
			else if (entity instanceof Shooter)
			{
				paintShooter(g);
			}
		}
//        paintAliens(g);
//        paintShooter(g);
		if (shooting)
		{
			paintShot(g);
		}
	}

	private void paintAliens(Graphics2D g, Entity alien)
	{

//        if (isAlive()) {
		g.drawImage(alienPic, alien.getCol() * cellSize, alien.getRow() * cellSize, imgObs);
//        }
	}


	private void paintShooter(Graphics2D g)
	{
		g.drawImage(shooterPic, shooter.getCol() * cellSize, shooter.getRow() * cellSize, imgObs);

	}

	public void moveShooter()
	{
		int oldLoc = getSquareIndex(shooter.getCol(), shooter.getRow());
		Entity empty = new Empty(shooter.getCol(), shooter.getRow());
		int newLoc;

		if (movement == Direction.LEFT && !atLeftBounds(shooter))
		{
			newLoc = getSquareIndex(shooter.getCol() - 1, shooter.getRow());
			gameBoard.set(newLoc, shooter);
			shooter.setCol(shooter.getCol() - 1);
			gameBoard.set(oldLoc, empty);
		}
		else if (movement == Direction.RIGHT && !atRightBounds(shooter))
		{
			newLoc = getSquareIndex(shooter.getCol() + 1, shooter.getRow());
			gameBoard.set(newLoc, shooter);
			shooter.setCol(shooter.getCol() + 1);
			gameBoard.set(oldLoc, empty);
		}
//        Square shot = new Square(Square.Entity.Projectile, shooter.getLocation().getCol(), shooter.getLocation().getRow());
//        projectile = new Projectile(shot);
	}

	private boolean atLeftBounds(Entity entity)
	{
		return entity.getCol() == 0;
	}

	private boolean atRightBounds(Entity entity)
	{
		return entity.getCol() == BOARD_COLS - 1;
	}


	private void exit()
	{
		System.out.println("Final Score: " + getScore());
		System.exit(0);
	}

	int getScore()
	{
		return score;
	}


	private boolean removeAlienIfShot()
	{
		for (Entity entity : gameBoard)
		{
			if (entity instanceof Alien)
			{
				Alien alien = (Alien) entity;
				shooting = true;
				if (projectile.getCol() == alien.getCol() && projectile.getRow() == alien.getRow())
				{
					alien.setAlive(false);
					deadAliens++;
					score += 10;
					int i = getSquareIndex(projectile.getCol(), projectile.getRow());
					gameBoard.set(i, new Empty(projectile.getCol(), projectile.getRow()));
					return true;
				}
			}
		}
		return false;
	}


	private void paintShot(Graphics2D g)
	{
		g.setColor(Color.GREEN);
		g.drawLine((shooter.getCol() * cellSize) + cellSize / 2,
				(shooter.getRow() * cellSize) + cellSize / 2,
				(projectile.getCol() * cellSize) + cellSize / 2,
				(projectile.getRow() * cellSize) + cellSize / 2);
		sleep();

	}

	public void shoot()
	{
		for (int loc = BOARD_ROWS - 1; loc >= 0; loc--)
		{
			projectile = new Projectile(shooter.getCol(), loc);
			if (removeAlienIfShot())
			{
				if (isGameOver())
				{
					exit();
				}
				break;
			}
		}
	}

	public boolean isGameOver()
	{
		return allAliensDead() || gameOver;
	}

	private boolean allAliensDead()
	{
		return allAliens == deadAliens;
	}

	private void sleep()
	{
		try
		{
			Thread.sleep(10);
		} catch (InterruptedException ex)
		{
			ex.printStackTrace();
		}
	}

    /*public void nextRound() {
        Square square;
        for (int i = 0; i < gameBoard.size(); i++) {
            square = gameBoard.get(i);
            square.setCol(square.getCol() + 1);
            int row = aliens.get(i).getRow();
            aliens.get(i).setRow(row + 1);
            if (square.getRow() == BOARD_ROWS - 2 && square.getEntity() == Square.Entity.Alien) {
                gameOver = true;
            }
        }

    }*/

	private int getSquareIndex(int col, int row)
	{
		for (int i = 0; i < gameBoard.size(); i++)
		{
			if (gameBoard.get(i).getCol() == col)
			{
				if (gameBoard.get(i).getRow() == row)
				{
					return i;
				}
			}
		}
		return -1;
	}

	private Image createAlienPic()
	{
		File imageFile = new File("alien.jpg");
		try
		{
			return resize(ImageIO.read(imageFile), cellSize, cellSize);
		} catch (IOException e)
		{
			System.out.println("Image not found.");
		}
		return null;
	}

	private Image createShooterPic()
	{
		File icon = new File("shooter.jpg");
		try
		{
			return ImageIO.read(icon);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private static BufferedImage resize(BufferedImage img, int height, int width)
	{
		Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = resized.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		return resized;
	}
}