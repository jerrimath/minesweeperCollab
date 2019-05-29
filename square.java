/**
 * Represents a general square on the game board.
 *
 * This is the Github remote version of code.
 * @author Jerry Xu
 * @author 20jxu@westfordk12.us
 * @version 1.0
 * @since 1.0
 */
public class square {
	private int minesAround;
	private boolean isMine;
	private boolean markMine;
	private boolean dugUp;
	private boolean quickCleared;


	/**
	 * Creates a square with the mine status
	 * This should be true if it is a mine, false if it is not a mine
	 *
	 * @param mine   The status of the mine
	 * @param around The number of mines surrounding this given square
	 */
	public square(boolean mine, int around) {
		isMine = mine;
		minesAround = around;
		markMine = false;
		dugUp = false;
		quickCleared = false;
	}

	/**
	 * Flips the status of the marked mine
	 * I.e. a marked mine becomes unmarked, and an unmarked mine becomes marked.
	 *
	 * @return A boolean signaling the new status of the marking of the square.
	 */
	public boolean mark() {
		markMine = !markMine;
		return markMine;
	}

	/**
	 * "Digs up" a square
	 * This method can only be invoked once on a given square, safety checks within the game
	 * will make sure of that.
	 */
	public void dig() {
		dugUp = true;
	}

	/**
	 * "Digs up" a square
	 * This method can only be invoked once on a given square, safety checks within the game
	 * will make sure of that.
	 * @param marked Number of marked mines around given square
	 * @return Returns whether the quick clear operation should be run or not
	 */
	public boolean quickClear(int marked) {
		if (marked > minesAround && !quickCleared) {
			quickCleared = true;
			return true;
		}
		return false;
	}

	/** Chooses image for display
	 * This method tells the game what image should be displayed on the square.
	 * @return The number of mines surrounding for if it isn't a mine (0-8), or undug up (-1) or a mine (100)
	 */
	public int getDisplay(){
		if (markMine) return 100;
		if (!dugUp) return -1;
		return minesAround;

	}


}