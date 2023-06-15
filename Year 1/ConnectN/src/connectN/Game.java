package connectN;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Game {

	private int playerTurn;
	private String playerOne;
	private String playerTwo;
	private char[][] board;
	private int previousMoveRow = -1;
	private int previousMoveCol = -1;
	private int n;
	private static final String SAVELOCATION = "currentGame.txt";

	public Game(int row, int col, int n, String playerOneName, String playerTwoName) {
		newGame(row, col);
		iniPlayerTurn();
		playerOne = playerOneName;
		playerTwo = playerTwoName;
		this.n = n;
	}

	public Game() {
		loadGame();
	}

	private void iniPlayerTurn() {
		playerTurn = (int) (Math.random() * (2 + 0));
	}

	public void newGame(int r, int c) {
		board = new char[r][c];
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				board[row][col] = 'E';
			}
		}
	}

	public boolean checkBoard() {
		boolean isTrue = true;
		if (n < 4) {
			JOptionPane.showMessageDialog(null, "N MUST be bigger than 4", "ERROR", JOptionPane.ERROR_MESSAGE);
			isTrue = false;
		} else if (n > 14) {
			JOptionPane.showMessageDialog(null, "N MUST be smaller than 15", "ERROR", JOptionPane.ERROR_MESSAGE);
			isTrue = false;
		} else if (board.length > 14) {
			JOptionPane.showMessageDialog(null, "The number of rows MUST be smaller than 15", "ERROR",
					JOptionPane.ERROR_MESSAGE);
			isTrue = false;
		} else if (board[1].length > 14) {
			JOptionPane.showMessageDialog(null, "The number of columns MUST be smaller than 15", "ERROR",
					JOptionPane.ERROR_MESSAGE);
			isTrue = false;
		} else if (board.length < 4) {
			JOptionPane.showMessageDialog(null, "The number of rows MUST be bigger than 4", "ERROR",
					JOptionPane.ERROR_MESSAGE);
			isTrue = false;
		} else if (board[1].length < 4) {
			JOptionPane.showMessageDialog(null, "The number of columns MUST be bigger than 4", "ERROR",
					JOptionPane.ERROR_MESSAGE);
			isTrue = false;
		} else if (n > board.length) {
			JOptionPane.showMessageDialog(null, "N MUST be smaller than the number of rows/columns", "ERROR",
					JOptionPane.ERROR_MESSAGE);
			isTrue = false;
		} else if (n > board[1].length) {
			JOptionPane.showMessageDialog(null, "N MUST be smaller than the number of rows/columns", "ERROR",
					JOptionPane.ERROR_MESSAGE);
			isTrue = false;
		}
		return isTrue;
	}

	public char[][] getBoard() {
		return board;
	}

	public String getPlayerTurn() {
		if (playerTurn == 0) {
			return playerOne;
		} else if (playerTurn == 1) {
			return playerTwo;
		} else {
			return null;
		}
	}

	public String getPlayerOne() {
		return playerOne;
	}

	public String getPlayerTwo() {
		return playerTwo;
	}

	public void playTurn(String move) {
		boolean validMove = true;
		move = move.replaceAll("()", "");
		move = move.replaceAll(" ", "");
		for (int i = 0, specialCharCount = 0; i < move.length() && validMove; i++) {
			if (!Character.isDigit(move.charAt(i))) {
				if (move.charAt(i) == ',' && specialCharCount >= 1) {
					validMove = false;
				} else if (move.charAt(i) == ',' && specialCharCount == 0) {
					specialCharCount += 1;
				} else {
					validMove = false;
				}
			}
		}
		if (!validMove) {
			System.err.println("ERR: " + move + " is NOT a valid move!");
			return;
		}
		if (validMove) {
			String location[] = move.split(",");
			int row = Integer.parseInt(location[0]) - 1;
			int col = Integer.parseInt(location[1]) - 1;
			if (row > board.length || col > board[1].length || row < 0 || row < 0 || board[row][col] == 'Y'
					|| board[row][col] == 'R') {
				validMove = false;
				System.err.println("ERR: move not in range!");
			}
			if (row > 0) {
				if (board[row - 1][col] == 'E') {
					validMove = false;
					System.err.println("ERR: move is not available!");
				}
			}
			if (validMove) {
				if (playerTurn == 0) {
					board[row][col] = 'Y';
					playerTurn = 1;
				} else if (playerTurn == 1) {
					board[row][col] = 'R';
					playerTurn = 0;
				}
				previousMoveCol = col;
				previousMoveRow = row;
			}
		}
	}

	public char checkWinner() {
		if (checkRed()) {
			return 'R';
		}
		if (checkYellow()) {
			return 'Y';
		}
		// check if full
		for (int row = 0, filledSpot = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				if (board[row][col] == 'R' || board[row][col] == 'Y') {
					filledSpot++;
				}
				if (filledSpot == (board.length * board[row].length)) {
					return ' ';
				}
			}
		}
		return 'E';
	}

	private boolean checkRed() {
		boolean win = false;
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				if (board[row][col] == 'R') {
					if (checkRow(row, col) || checkCol(row, col) || checkUTDDiagonal(row, col)
							|| checkDTUDiagonal(row, col)) {
						win = true;
						break;
					}
				}

			}
		}
		return win;
	}

	private boolean checkYellow() {
		boolean win = false;
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				if (board[row][col] == 'Y') {
					if (checkRow(row, col) || checkCol(row, col) || checkUTDDiagonal(row, col)
							|| checkDTUDiagonal(row, col)) {
						win = true;
						break;
					}
				}
			}
		}
		return win;
	}

	private boolean checkRow(int row, int col) {
		boolean isTrue = true;
		if (col <= (board[row].length - n)) {
			for (int streak = 0; streak < n; streak++) {
				if (board[row][col] != board[row][col + streak]) {
					isTrue = false;
				}
			}
		} else {
			isTrue = false;
		}
		return isTrue;
	}

	private boolean checkCol(int row, int col) {
		boolean isTrue = true;
		if (row <= (board.length - n)) {
			for (int streak = 0; streak < n; streak++) {
				if (board[row][col] != board[row + streak][col]) {
					isTrue = false;
				}
			}
		} else {
			isTrue = false;
		}
		return isTrue;
	}

	private boolean checkDTUDiagonal(int row, int col) {
		boolean isTrue = true;
		if (row <= board.length - n && col < board[row].length - n) {
			for (int streak = 0; streak < n; streak++) {
				if (board[row][col] != board[row + streak][col + streak]) {
					isTrue = false;
				}
			}
		} else {
			isTrue = false;

		}
		return isTrue;

	}

	private boolean checkUTDDiagonal(int row, int col) {
		boolean isTrue = true;
		if (row >= n - 1 && col < board[row].length - n) {
			for (int streak = 0; streak < n; streak++) {
				if (board[row][col] != board[row - streak][col + streak]) {
					isTrue = false;
				}
			}
		} else {
			isTrue = false;
		}
		return isTrue;
	}

	public void undoMove() {
		if (previousMoveCol != -1 && previousMoveRow != -1) {
			board[previousMoveRow][previousMoveCol] = 'E';
			if (playerTurn == 1) {
				playerTurn = 0;
			} else if (playerTurn == 0) {
				playerTurn = 1;
			}
		} else {
			JOptionPane.showMessageDialog(null, "Nothing to undo...", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void saveGame() {
		try {
			File file = new File(SAVELOCATION);
			if (file.createNewFile()) {
				JOptionPane.showMessageDialog(null, "File Created!", "Something happened!",
						JOptionPane.INFORMATION_MESSAGE);
			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		try {
			FileWriter writer = new FileWriter(SAVELOCATION);
			writer.write(playerTurn + "\n" + playerOne + "\n" + playerTwo + "\n" + board.length + "\n" + board[1].length
					+ "\n" + previousMoveRow + "\n" + previousMoveCol + "\n" + n + "\n");
			for (int row = 0; row < board.length; row++) {
				for (int col = 0; col < board[row].length; col++) {
					writer.write(board[row][col] + " ");
				}
			}
			writer.close();
		}
		catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	private void loadGame() {
		File file = new File(SAVELOCATION);
		try {
			Scanner reader = new Scanner(file);
			playerTurn = reader.nextInt();
			playerOne = reader.next();
			playerTwo = reader.next();
			board = new char[reader.nextInt()][reader.nextInt()];
			previousMoveRow = reader.nextInt();
			previousMoveCol = reader.nextInt();
			n = reader.nextInt();
			for (int row = 0; row < board.length; row++) {
				for (int col = 0; col < board[row].length; col++) {
					board[row][col] = reader.next().charAt(0);
				}
			}
		}
		catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	public boolean fileExists() {
		File file = new File(SAVELOCATION);
		return file.exists();
	}
}
