package minesweeperCollab;

import java.util.Scanner;
//import TerminalIO.KeyboardReader;

/**
 * TerminalIO to verify game functionality.
 *
 * @author Claire Shea
 * @author 20cshea@westfordk12.us
 * @author Jerry Xu
 * @author 20jxu@westfordk12.us
 * @version 1.3
 * @since 1.1
 */
public class MinesweeperTIO {

	public static String printBoard(game Game) {
		int xSize = Game.getDisplay().length;
		int ySize = Game.getDisplay()[0].length;
		String retVal = "\\x|";
		for (int tens = 1; tens <= xSize; tens++) {
			retVal += " " + Integer.toString(tens / 10);
		}
		retVal += "\ny\\|";
		for (int ones = 1; ones <= xSize; ones++) {
			retVal += " " + Integer.toString(ones % 10);
		}
		retVal += "\n--+-";
		for (int xs = 1; xs <= xSize; xs++) {
			retVal += "--";
		}
		for (int ys = 1; ys <= ySize; ys++) {
			retVal += "\n" + Integer.toString(ys / 10) + Integer.toString(ys % 10) + "|";
			for (int xs = 1; xs <= xSize; xs++) {
				retVal += " ";
				int status = Game.getDisplay()[xs - 1][ys - 1];
				if (status == 0) retVal += " ";
				else if (1 <= status && status <= 8) retVal += Integer.toString(status);
				else if (status == -1) retVal += "#";
				else if (status == 100) retVal += "^";
				else if (status == 1000) retVal += "*";
			}
		}

		/*
		\x| 1 2 3 4 5 6 7 8 9 0 1
		y\| 0 0 0 0 0 0 0 0 0 1 1
		--+----------------------
		01|
		02|
		03|
		04|
		05|
		06|
		07|
		08|
		09|
		10|
		 */
		return retVal;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int statusTrigger = 0;
		do {
			try {
				System.out.print("Enter -1 to quit: ");
				statusTrigger = sc.nextInt();
			} catch (Exception e) {
				System.out.println("Invalid number entered.");
				continue;
			}

			int xdim = 0;
			int ydim = 0;
			int numMines = 0;
			boolean firstTime = true;

			do {
				try {
					if (!firstTime) System.out.println("One of your parameters was incorrect. Please try again");
					firstTime = false;

					System.out.print("X-Dimension of Game Board: ");
					xdim = sc.nextInt();
					System.out.print("Y-Dimension of Game Board: ");
					ydim = sc.nextInt();
					System.out.print("Number of mines on Game Board: ");
					numMines = sc.nextInt();

				} catch (Exception e) {
					System.out.println("Invalid number entered.");
					continue;
				}
			} while (xdim <= 0 || xdim >= 100 || ydim <= 0 || ydim >= 100 || numMines <= 0 || numMines >= xdim * ydim);

			int xStart = -1;
			int yStart = -1;
			firstTime = true;
			do {
				try {
					if (!firstTime) System.out.println("One of your parameters was incorrect. Please try again");
					firstTime = false;

					System.out.print("X-Dimension of Start Position: ");
					xStart = sc.nextInt();
					System.out.print("Y-Dimension of Start Position: ");
					yStart = sc.nextInt();

				} catch (Exception e) {
					System.out.println("Invalid number entered.");
					continue;
				}
			} while (xStart < 0 || xStart > xdim || yStart < 0 || yStart > ydim);

			game Game = new game(xdim, ydim, numMines, xStart, yStart);
			int status = 0;
			do {
				firstTime = true;
				int xCoord = -1;
				int yCoord = -1;
				boolean toMark = false;
				do {
					try {
						if (!firstTime) System.out.println("One of your parameters was incorrect. Please try again");
						firstTime = false;

						System.out.print("X-Location of operation: ");
						xCoord = sc.nextInt();
						System.out.print("Y-Location of operation: ");
						yCoord = sc.nextInt();
						System.out.print("Operation (0 to dig, 1 to mark): ");
						int tempMark = sc.nextInt();
						if (tempMark != 0 && tempMark != 1) {
							throw new Exception();
						} else if (tempMark == 0) toMark = false;
						else if (tempMark == 1) toMark = true;

					} catch (Exception e) {
						System.out.println("Invalid number entered.");
						continue;
					}
				} while (xCoord <= 0 || xCoord > xdim || yCoord <= 0 || yCoord > ydim);

				status = Game.gameState(xCoord, yCoord, toMark);
				System.out.println(printBoard(Game));

			} while (status == 0);
			if (status == -1) {
				System.out.println(printBoard(Game));
				System.out.println("You lose :(");
			} else if (status == 1) {
				System.out.println(printBoard(Game));
				System.out.println("You win! :)");
			}

		} while (statusTrigger != -1);
	}

}
