import java.util.ArrayList;


public class Game {
	private int horSize;
	private int verSize;
	private int numMines;
	private int startX;
	private int startY;
	private boolean[][] used;
	private boolean[][] mine;
	private boolean[][] open;
	private square[][] gameBoard;

	private ArrayList<square> grid;
	
	
	public Game(int x, int y, int m, int initX, int initY){// constructor
		grid = new ArrayList<square>();
		used = new boolean[x][y];
		mine = new boolean[x][y];
		open = new boolean[x][y];
		gameBoard = new square[x][y];
		numMines = m;
		horSize = x;
		verSize = y;
		
		setMines(initX, initY);
		
	}
	
	private int[] findNext(int x, int y, int initX, int initY) { //finds next available location
		while(used[x][y] || Math.abs(x-initX) <=1 ||Math.abs(y-initY)<=1) {
			
				
			if(x == horSize-1) {
				y++;
			} 
			else {
				x++;
			}
			
				
		}
		int[] a = {x, y};
		return a;
	}
	
	private void randomize(int initX, int initY) {// randomizing location of all mines
		used[initX][initY] = true;
		startX = initX;
		startY = initY;
		//Deal with next to squares
		for(int i = 0; i< numMines; i++) {
			int xVal = (int)((horSize+1)*Math.random());
			int yVal = (int)((verSize+1)*Math.random());
			int pos[] = findNext(xVal, yVal, initX, initY);
			mine[pos[0]][pos[1]] = true;
			used[pos[0]][pos[1]] = true;

		}
		
	}
	
	private void setMines(int initX, int initY) {// sets all mines in random location using randomize
		randomize(initX, initY);
		for(int i = 0; i<horSize; i++) {
			for(int j = 0; j<verSize; j++) {
				int counter = 0;
				for(int k = -1; k<2; k++) {
					for(int l = -1; l<2; l++) {
						if(k == 0 || l == 0)
							continue;
						if(i+k< horSize && i+k>=0 && j+l< verSize && j+l>=0 && mine[i][j]) {
							counter++;
						}
									
					}
				}
				gameBoard[i][j] = new square(mine[i][j], counter);
			}
		}
	}
	
	private void floodfill(int x, int y) {// method to floodfill to all squares adjacent
		open[x][y] = true;
		gameBoard[x][y].dig();
		
		if(gameBoard[x][y].getDisplay() !=0)
			return;
		
		for(int i = -1; i<2; i++) {
			for(int j = -1; j<2; j++) {
				if(x+i< horSize && x+i>=0 && y+j< verSize && y+j>=0 && gameBoard[x+i][y+j].getDisplay() == -1) {
					floodfill(x+i, y+j);
				}

			}
		}
	}
	
	private void clearSquares(int x, int y) {// deals with opening of squares
		if(gameBoard[x][y].getDisplay() == -1) {
			floodfill(x, y);
		}
	}
	
	private void mark(int x, int y) {// marks square
		gameBoard[x][y].mark();
	}
	
	private boolean win() {// checks if user has won
		int numFlagged = 0;
		for(int i = 0; i<horSize; i++) {
			for(int j = 0; j<verSize; j++) {
				if(gameBoard[i][j].getDisplay() == -1) {
					return false;
				}
				else if(gameBoard[i][j].getDisplay() == 100){
					numFlagged++;
				}
			}
		}
		
		if(numFlagged == numMines)
			return true;
		return false;
	}
	
	public int getNumberAround(int x, int y) {//return the number of mines around square
		return gameBoard[x][y].getDisplay();
	}
	
	public int gameState(int clickX, int clickY, boolean mark) {//method to be used by the GUI in game
		if(!mark) {// if turn is to open new square
			if(mine[clickX][clickY]) {// player loses
				return -1;
			}
			else {
				clearSquares(clickX, clickY);
			}
			
		}
		else {// if turn is to mark mine
			mark(clickX, clickY);
		}
		
		if(win())// win
			return 1;
		
		return 0;
		
		
	}

}
