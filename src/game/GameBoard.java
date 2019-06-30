package game;

import java.io.PrintWriter;

public class GameBoard {
	private static final char EMPTY_FIELD = '_';
	private static final char  GRID = '|';
	private static final char CHAR_ZERO = '0';
	private static final char CHAR_THREE = '3';
	private static final char LETTER_A = 'A';
	private static final char LETTER_B = 'B';
	private static final char LETTER_C = 'C';
	
	private char[][] board;

	public GameBoard() {
		board = new char[5][9];
		buildBoard();
	}

	private void buildBoard() {
		for (int j = 3; j < 8; j += 2) {
			board[0][j] = (char) (j / 2 + CHAR_ZERO);
			board[j / 2 + 1][0] = (char) (j / 2 - 1 + LETTER_A);
		}
		for (int i = 2; i < 5; i++) {
			for (int j = 2; j < 8; j += 2) {
				board[i][j] = GRID;
				board[i - 1][j + 1] = EMPTY_FIELD;
			}
		}
		for (int i = 2; i < 5; i++) {
			board[i][8] = GRID;
		}
		for (int i = 3; i < 8; i += 2) {
			board[4][i] = EMPTY_FIELD;
		}
	}

	public void printBoard(PrintWriter pw) {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 9; j++) {
				pw.print(board[i][j]);
			}
			pw.println();
		}
		pw.flush();
	}
	
	public char[][] getBoard(){
		return board;
	}

	public int validateFieldVerticaly(String field) {
		if (field.length() > 1) {
			switch (field.charAt(0)) {
			case LETTER_A: {
				return 2;
			}
			case LETTER_B: {
				return 3;
			}
			case LETTER_C: {
				return 4;
			}
			default: {
				return -1;
			}
			}
		} else {
			return -1;
		}
	}

	public int validateFieldHorizontaly(String field) {
		if (field.length() == 2 && field.charAt(1) > CHAR_ZERO && field.charAt(1) <= CHAR_THREE) {
			return (field.charAt(1) - CHAR_ZERO) * 2 + 1;
		} else {
			return -1;
		}
	}

	public boolean isFieldValid(String field) {
		int x = validateFieldVerticaly(field);
		int y = validateFieldHorizontaly(field);
		return (x != -1 && y != -1 && board[x][y] == EMPTY_FIELD);
	}

	public void placeSymbol(String field, char symbol) {
		int x = validateFieldVerticaly(field);
		int y = validateFieldHorizontaly(field);
		board[x][y] = symbol;
	}

	public boolean hasLine(char symbol) {
		return hasVerticalLine(symbol) || hasHorizontalLine(symbol) || hasDiagonalLine(symbol);
	}

	public boolean hasVerticalLine(char symbol) {
		for (int j = 3; j < 8; j += 2) {
			if (board[2][j] ==symbol && board[2][j] == board[3][j] && board[3][j] == board[4][j]) {
				return true;
			}
		}
		return false;
	}

	public boolean hasHorizontalLine(char symbol) {
		for (int i = 2; i < 5; i++) {
			if (board[i][3] == symbol && board[i][3] == board[i][5] && board[i][5] == board[i][7]) {
				return true;
			}
		}
		return false;
	}

	public boolean hasDiagonalLine(char symbol) {
		if (board[3][5] == symbol && (board[2][3] == board[3][5] && board[3][5] == board[4][7]
				|| board[2][7] == board[3][5] && board[3][5] == board[4][3])) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isFull() {
		for (int i = 2; i < 5; i++) {
			for (int j = 3; j < 8; j += 2) {
				if (board[i][j] == EMPTY_FIELD) {
					return false;
				}
			}
		}
		return true;
	}

}
