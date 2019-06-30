package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class GameRoom {
	private static final String GAME_ALLREADY_STARTED = "This game has already started!";
	private static final String READING_EXCEPTION = "An error occurred while reading!";
	private static final String WAITING = "Waiting for opponent...";
	private static final String ENTER_TURN = "Enter your turn:";
	private static final String INVALID_CELL = "Invalid cell!";
	private static final String NEW_LINE = "\n";
	private static final String WINNER = "You won!";
	private static final String LOSER = "You lost!";
	private static final String EMPTY_STRING = "";
	private static final char X_SIGN = 'X';
	private static final char O_SIGN = 'O';

	private String roomName;
	private PrintWriter playerOnePw;
	private BufferedReader playerOneBr;
	private PrintWriter playerTwoPw;
	private BufferedReader playerTwoBr;
	private GameBoard gameBoard;

	public GameRoom(String roomName, PrintWriter pw, BufferedReader br) {
		this.roomName = roomName;
		this.playerOnePw = pw;
		this.playerOneBr = br;
		gameBoard = new GameBoard();
	}

	public String getRoomName() {
		return roomName;
	}
	
	public GameBoard getGameBoard() {
		return gameBoard;
	}

	public void joinRoom(PrintWriter pw, BufferedReader br) {
		if (playerTwoPw != null) {
			pw.println(GAME_ALLREADY_STARTED);
			pw.flush();
			return;
		} else {
			this.playerTwoPw = pw;
			this.playerTwoBr = br;
			play();
		}
	}

	public void play() {
		while (!gameBoard.isFull()) {
			makeMove(playerOnePw, playerOneBr, playerTwoPw, X_SIGN);
			if (checkIfWon(playerOnePw, playerTwoPw, X_SIGN)) {
				return;
			}
			makeMove(playerTwoPw, playerTwoBr, playerOnePw, O_SIGN);
			if (checkIfWon(playerTwoPw, playerOnePw, O_SIGN)) {
				return;
			}
		}
	}

	public boolean checkIfWon(PrintWriter activePlayerPw, PrintWriter waitingPlayerPw, char symbol) {
		if (gameBoard.hasLine(symbol)) {
			endGameMessage(activePlayerPw, waitingPlayerPw);
			return true;
		}
		return false;
	}

	public void endGameMessage(PrintWriter winnerPw, PrintWriter loserPw) {
		gameBoard.printBoard(winnerPw);
		winnerPw.println(WINNER);
		winnerPw.flush();
		gameBoard.printBoard(loserPw);
		loserPw.println(LOSER);
		loserPw.flush();
	}

	public void makeMove(PrintWriter activePlayerPw, BufferedReader activePlayerBr, PrintWriter waitingPlayerPw,
			char symbol) {
		msgBeforeMakingMove(activePlayerPw, waitingPlayerPw);
		String field = EMPTY_STRING;
		while (!gameBoard.isFieldValid(field)) {
			activePlayerPw.println(INVALID_CELL + NEW_LINE + ENTER_TURN);
			activePlayerPw.flush();
			try {
				field = activePlayerBr.readLine();
			} catch (IOException e) {
				System.out.println(READING_EXCEPTION);
				e.printStackTrace();
			}
		}
		gameBoard.placeSymbol(field, symbol);
	}
	
	private void msgBeforeMakingMove(PrintWriter activePlayerPw, PrintWriter waitingPlayerPw) {
		waitingPlayerPw.println(WAITING);
		waitingPlayerPw.flush();
		gameBoard.printBoard(activePlayerPw);
		activePlayerPw.println(ENTER_TURN);
		activePlayerPw.flush();
	}
	

}
