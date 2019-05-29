import java.util.ArrayList;

/** Need:
 * Randomize bombs
 * Click events/clear space
 * End game
 * 
 * 
 * */
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
	
	
	public Game(int x, int y, int m){
		grid = new ArrayList<square>();
		used = new boolean[x][y];
		mine = new boolean[x][y];
		open = new boolean[x][y];
		gameBoard = new square[x][y];
		numMines = m;
		horSize = x;
		verSize = y;
		
	}
	
	public int[] findNext(int x, int y) {
		while(used[x][y]) {
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
	
	public void randomize(int initX, int initY) {
		used[initX][initY] = true;
		startX = initX;
		startY = initY;
		//Deal with next to squares
		for(int i = 0; i< numMines; i++) {
			int xVal = (int)((horSize+1)*Math.random());
			int yVal = (int)((verSize+1)*Math.random());
			int pos[] = findNext(xVal, yVal);
			mine[pos[0]][pos[1]] = true;
			used[pos[0]][pos[1]] = true;

		}
		
	}
	
	public void setMines(int initX, int initY) {
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
	
	private void floodfill(int x, int y) {
		open[x][y] = true;
		gameBoard[x][y].dig();
		
		for(int i = -1; i<2; i++) {
			for(int j = -1; j<2; j++) {
				if(x+i< horSize && x+i>=0 && y+j< verSize && y+j>=0 && !open[x+i][y+j]) {
					floodfill(x+i, y+j);
				}

			}
		}
	}
	
	public void clearSquares(int x, int y) {
		floodfill(x, y);
	}
	
	public void mark(int x, int y) {
		gameBoard[x][y].mark();
	}
	
	public boolean win() {
		int numFlagged = 0;
		int numDug = 0;
		for(int i = 0; i<horSize; i++) {
			for(int j = 0; j<verSize; j++) {
				if(gameBoard[i][j].getDisplay() == -1) {
					return 0;
				}
				else {
					
				}
			}
		}
	}
	
	public int gameState(int clickX, int clickY, boolean mark) {
		if(!mark) {
			if(mine[clickX][clickY]) {
				return -1;
			}
			else {
				clearSquares(clickX, clickY);
			}
		}
		else {
			mark(clickX, clickY);
		}
		
		if()
	}
	
	
	
	
	

}


