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

		int throwAway = gameState(initX, initY, false);
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
			if (x >= xSize - 1) {
				x = 0;
				if (y >= ySize - 1) {
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
			int xVal = (int) (((double)(xSize)) * Math.random());
			int yVal = (int) (((double)(ySize)) * Math.random());
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
						if (xOffset == 0 && yOffset == 0)
							continue;
						if (x + xOffset < xSize && x + xOffset >= 0 && y + yOffset < ySize && y + yOffset >= 0 && mine[x + xOffset][y + yOffset]) {
							counter++;
						}

					}
				}

				//Dump info into info hiding
				gameBoard[x][y] = new square(mine[x][y], counter);
			}
		}
	}

	/**
	 * Floodfill opens up all the adjacent squares which are empty.
	 *
	 * @param x The initial x-position clicked
	 * @param y The initial y-position clicked
	 */
	private void floodfill(int x, int y) {
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

	/** Clear Square helper
	 * @param x x-location of square to dig up
	 * @param y y-location of square to dig up
	 * @return Game status
	 */
	private int clearSquares(int x, int y) {
		if (gameBoard[x][y].getDisplay() == -1) {
			floodfill(x, y);
			if (gameBoard[x][y].getDisplay() == 1000) return -1;
			return 0;
		}
		//quickClear determination
		int surround = 0;
		for(int xOffset = -1; xOffset <= 1; xOffset++){
			for(int yOffset = -1; yOffset <= 1; yOffset++){
				int posX = x+xOffset;
				int posY = y+yOffset;
				if(posX < 0 || posX >= xSize) continue;
				if(posY < 0 || posY >= ySize) continue;

				if (gameBoard[posX][posY].getDisplay() == 100) surround++;
			}
		}
		if(gameBoard[x][y].quickClear(surround)){
			for(int xOffset = -1; xOffset <= 1; xOffset++){
				for(int yOffset = -1; yOffset <= 1; yOffset++){
					int posX = x+xOffset;
					int posY = y+yOffset;
					if(posX < 0 || posX >= xSize) continue;
					if(posY < 0 || posY >= ySize) continue;

					floodfill(posX, posY);
					if (gameBoard[posX][posY].getDisplay() == -1){
						if(gameState(posX, posY, false) == -1) return -1;
						if(gameState(posX, posY, false) == 1) return 1;
					}
				}
			}
		}
		return 0;
	}

	/** Mark Square helper
	 * @param x x-location of square to mark
	 * @param y y-location of square to mark
	 * @deprecated
	 */
	private void mark(int x, int y) {
		gameBoard[x][y].mark();
	}


	/** Win checker
	 * Checks for the win-condition based on if all square have been dug up
	 * or if all mines have been flagged
	 */
	private boolean win() {
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

	/** Display v1
	 * @param x the x-coordinate of the position display to be retrieved
	 * @param y the y-coordinate of the position display to be retrieved
	 * @return the display value for the given square.
	 * @deprecated
	 */
	public int getNumberAround(int x, int y) {
		return gameBoard[x][y].getDisplay();
	}

	/** Display v2
	 * Values: The number of mines surrounding for if it isn't a mine (0-8), or undug up (-1) or a marked mine (100) If exploded, return 1000
	 * @return a two-dimensional array representing the display values for the entire gameboard.
	 */
	public int[][] getDisplay(){
		int[][] retVal = new int[xSize][ySize];
		for(int xCoord = 0; xCoord < xSize; xCoord++){
			for(int yCoord = 0; yCoord < ySize; yCoord++){
				retVal[xCoord][yCoord] = gameBoard[xCoord][yCoord].getDisplay();
			}
		}
		return retVal;
	}

	/** Get Game State
	 * @param clickX x-coordinate of the clicked square
	 * @param clickY y-coordinate of the clicked square
	 * @param mark   digging up a square or marking a square?
	 * @return -1 if the game has been lost; 0 if the game is ongoing, 1 if the game wins.
	 */
	public int gameState(int clickX, int clickY, boolean mark) {
		//dig up new square
		if (!mark) {
			// player loses
			int status  = clearSquares(clickX, clickY);
			
			if (win())// win
				return 1;
			return status;

			// if turn is to mark mine
		} else {
			gameBoard[clickX][clickY].mark();
		}

		if (win())// win
			return 1;

		return 0;

	}

}