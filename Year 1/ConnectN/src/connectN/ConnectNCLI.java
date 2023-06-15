package connectN;

import java.util.Scanner;

public class ConnectNCLI {

	enum MENU {
		MAINMENU, SETUP, INGAME, ENDGAME, EXITGAME
	}

	private static Game game;

	public static void main(String[] args) {
		MENU state = MENU.MAINMENU;
		Scanner input = new Scanner(System.in);
		System.out.println("Welcome to Heritage Connect-N, made by Mercadud!\n");

		while (state != MENU.EXITGAME) {
			// MAIN MENU
			if (state == MENU.MAINMENU) {
				System.out.print(
						"Please enter N to start a new game or R to resume the game stored in currentGame.txt ->");
				String menuInput = input.next();
				if (menuInput.equalsIgnoreCase("n")) {
					state = MENU.SETUP;
				} else if (menuInput.equalsIgnoreCase("r")) {
					game = new Game();
					if (game.fileExists()) {
						state = MENU.INGAME;
					}
				} else {
					System.err.println("ERR: invalid input");
				}
			}
			// SETUP
			if (state == MENU.SETUP) {
				boolean isTrue;

				int rowSelected = -1;
				do {
					isTrue = true;
					System.out.print("Enter the number of rows on the game board: ");
					String rS = input.next();
					for (int i = 0; i < rS.length(); i++) {
						if (!Character.isDigit(rS.charAt(i))) {
							isTrue = false;
						}
					}
					if (isTrue) {
						rowSelected = Integer.parseInt(rS);
					} else {
						System.err.println("\nNot a number, please enter again\n");
					}
				} while (!isTrue);

				int colSelected = -1;
				do {
					isTrue = true;
					System.out.print("Enter the number of columns on the game board: ");
					String cS = input.next();
					for (int i = 0; i < cS.length(); i++) {
						if (!Character.isDigit(cS.charAt(i))) {
							isTrue = false;
						}
					}
					if (isTrue) {
						colSelected = Integer.parseInt(cS);
					} else {
						System.err.println("\nNot a number, please enter again\n");
					}
				} while (!isTrue);

				int nSelected = -1;
				do {
					isTrue = true;
					System.out.print("Enter the value for N, the number of checkers in a row for a win: ");
					String nS = input.next();
					for (int i = 0; i < nS.length(); i++) {
						if (!Character.isDigit(nS.charAt(i))) {
							isTrue = false;
						}
					}
					if (isTrue) {
						nSelected = Integer.parseInt(nS);
					} else {
						System.err.println("\nNot a number, please enter again\n");
					}
				} while (!isTrue);

				System.out.print("Enter the name for Player 1 (yellow): ");
				String playerOneName = input.next();

				System.out.print("Enter the name for Player 2 (Red): ");
				String playerTwoName = input.next();

				game = new Game(rowSelected, colSelected, nSelected, playerOneName, playerTwoName);
				if (game.checkBoard()) {
					state = MENU.INGAME;
				}
			}
			if (state == MENU.INGAME) {
				System.out
						.println("Type Q at any time to exit the game, S to save the game or U to undo your last move");
				while (state == MENU.INGAME) {
					for (int row = game.getBoard().length - 1, count = game.getBoard().length - 1; row >= 0; row--) {
						for (int col = 0; col < game.getBoard()[1].length; col++) {
							if (row < count) {
								System.out.print("\n");
								count = row;
							}
							System.out.print(" " + game.getBoard()[row][col] + " ");
						}
					}
					System.out
							.print("\n" + game.getPlayerTurn() + ", enter square number (row, column) of your move ->");
					String move = input.next();
					if (move.equalsIgnoreCase("q")) {
						state = MENU.EXITGAME;
					} else if (move.equalsIgnoreCase("s")) {
						game.saveGame();
						System.out.println("saved!");
					} else if (move.equalsIgnoreCase("u")) {
						game.undoMove();
						System.out.println("move undoed!");
					} else {
						game.playTurn(move);
						if (game.checkWinner() != 'E') {
							System.out.println("GAME OVER!");
							state = MENU.ENDGAME;
						}
					}
				}
			}
			if (state == MENU.ENDGAME) {
				for (int row = game.getBoard().length - 1, count = game.getBoard().length - 1; row >= 0; row--) {
					for (int col = 0; col < game.getBoard().length; col++) {
						if (row < count) {
							System.out.print("\n");
							count = row;
						}
						System.out.print(" " + game.getBoard()[row][col] + " ");
					}
				}
				if (game.checkWinner() == ' ') {
					System.out.print("Board Filled, no winner:( play again?");
				} else if (game.checkWinner() == 'R') {
					System.out.println(game.getPlayerTwo() + " won! play again?");
				} else if (game.checkWinner() == 'Y') {
					System.out.println(game.getPlayerOne() + " won! play again?");
				}
				boolean correctInput = true;
				do {
					correctInput = true;
					System.out.println("Enter R to restart and enter Q to quit");
					String exitInput = input.next();
					if (exitInput.equalsIgnoreCase("r")) {
						state = MENU.MAINMENU;
					} else if (exitInput.equalsIgnoreCase("q")) {
						state = MENU.EXITGAME;
					} else {
						System.err.print("ERR: invalid input");
						correctInput = false;
					}
				} while (!correctInput);
			}
		}
		System.out.println("was fun being with ya buddy (⌣́_⌣̀) ");
		input.close();
	}

}
