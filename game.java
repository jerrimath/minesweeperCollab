package minesweeperCollab;

import java.awt.*;

/**
 * Represents and controls all aspects of the game.
 *
 * @author Ethan Mendes
 * @author 20emendes@westfordk12.us
 * @author Jerry Xu
 * @author 20jxu@westfordk12.us
 * @version 1.2
 * @since 1.1
 */
public class game {
	//Dimensions of the game board
	private int xSize;
	private int ySize;

	//Number of mines (should be a default ratio passed through UI
	private int numMines;

	//Only used for construction, purged afterwards
	private boolean[][] used;
	private boolean[][] mine;

	//Data storage
	private square[][] gameBoard;

	/**
	 * Creates a game board with all necessary info and
	 * places locations of mines
	 * Mine number is controlled by UI, should be above 0 but below size of board.
	 *
	 * @param x     The x-dimension of the game board
	 * @param y     The y-dimension of the game board
	 * @param m     The number of mines desired for the game board
	 * @param initX The initial x-position clicked
	 * @param initY The initial y-position clicked
	 */
	public game(int x, int y, int m, int initX, int initY) {
		//Storage copy
		xSize = x;
		ySize = y;
		numMines = m;

		//Generation of board
		used = new boolean[xSize][ySize];
		mine = new boolean[xSize][ySize];

		//Create board
		gameBoard = new square[xSize][ySize];

		setMines(initX, initY);

		//Information hiding
		used = null;
		mine = null;
	}

	/**
	 * Makes sure duplicate locations do not have mines placed there.
	 *
	 * @param x The x-location to check
	 * @param y The y-location to check
	 * @returns A point location where the mine should be placed.   
	 */
	private Point findNext(int x, int y) { //finds next available location
		while (used[x][y]) {
			if (x == xSize - 1) {
				if (y == ySize - 1) {
					x = 0;
					y = 0;
				} else {
					y++;
				}
			} else {
				x++;
			}
		}
		return new Point(x, y);
	}

	/**
	 * Randomly generates locations for placing mines.
	 * Takes care of the 3x3 start location layout.
	 *
	 * @param initX The initial x-position clicked
	 * @param initY The initial y-position clicked
	 */
	private void randomize(int initX, int initY) {
		//This location has been used - cannot be a mine at the start location or surrounding
		for (int xOff = -1; xOff <= 1; xOff++) {
			for (int yOff = -1; yOff <= 1; yOff++) {
				try {
					used[initX + xOff][initY + yOff] = true;
				} catch (Exception e) {
					//Do nothing if at edge
				}
			}
		}

		//Generate square locations
		for (int i = 0; i < numMines; i++) {
			int xVal = (int) ((xSize + 1) * Math.random());
			int yVal = (int) ((ySize + 1) * Math.random());
			Point pos = findNext(xVal, yVal);
			mine[pos.x][pos.y] = true;
			used[pos.x][pos.y] = true;
		}

	}

	/**
	 * Places location of mines on the game board
	 *
	 * @param initX The initial x-position clicked
	 * @param initY The initial y-position clicked
	 */
	private void setMines(int initX, int initY) {
		//Generate random locations
		randomize(initX, initY);
		
		//Calculate the number of mines surrounding 
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {

				//Find mines surrounding it
				int counter = 0;

				//Route through around
				for (int xOffset = -1; xOffset < 2; xOffset++) {
					for (int yOffset = -1; yOffset < 2; yOffset++) {
						//Calculate the mines around it; not consideration if the mine is
						if (xOffset == 0 || yOffset == 0)
							continue;
						if (x + xOffset < xSize && x + xOffset >= 0 && y + yOffset < ySize && y + yOffset >= 0 && mine[x][y]) {
							counter++;
						}

					}
				}

				//Dump info into info hiding
				gameBoard[x][y] = new square(mine[x][y], counter);
			}
		}
	}

	private void floodfill(int x, int y) {// method to floodfill to all squares adjacent
		gameBoard[x][y].dig();

		if (gameBoard[x][y].getDisplay() != 0)
			return;

		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (x + i < xSize && x + i >= 0 && y + j < ySize && y + j >= 0 && gameBoard[x + i][y + j].getDisplay() == -1) {
					floodfill(x + i, y + j);
				}

			}
		}
	}

	private void clearSquares(int x, int y) {// deals with opening of squares
		if (gameBoard[x][y].getDisplay() == -1) {
			floodfill(x, y);
		}
	}

	private void mark(int x, int y) {// marks square
		gameBoard[x][y].mark();
	}

	private boolean win() {// checks if user has won
		int numFlagged = 0;
		for (int i = 0; i < xSize; i++) {
			for (int j = 0; j < ySize; j++) {
				if (gameBoard[i][j].getDisplay() == -1) {
					return false;
				} else if (gameBoard[i][j].getDisplay() == 100) {
					numFlagged++;
				}
			}
		}

		if (numFlagged == numMines)
			return true;
		return false;
	}

	public int getNumberAround(int x, int y) {//return the number of mines around square
		return gameBoard[x][y].getDisplay();
	}

	public int gameState(int clickX, int clickY, boolean mark) {//method to be used by the GUI in game
		if (!mark) {// if turn is to open new square
			if (mine[clickX][clickY]) {// player loses
				return -1;
			} else {
				clearSquares(clickX, clickY);
			}

		} else {// if turn is to mark mine
			mark(clickX, clickY);
		}

		if (win())// win
			return 1;

		return 0;


	}

}
