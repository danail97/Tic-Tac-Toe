package game;


import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class GameBoardTest {


	public static final String C1 = "C1";
	public static final String B2 = "B2";
	public static final String A3 = "A3";
	public static final char SYMBOL_X = 'X';
	
	private GameBoard gameBoard;
	
	@Before
	public void setUp() {
		gameBoard = new GameBoard();
	}
	
	@Test
	public void isFieldValidShouldReturnTrueWhenTheFieldIsValid() {
		assertTrue("Field " + C1 + "is not valid", gameBoard.isFieldValid(C1));
	}
	
	@Test
	public void placeSymbolShouldPlaceSymbolInGivenField() {
		gameBoard.placeSymbol(B2, SYMBOL_X);
		assertTrue("Field " + B2 + " does not contain symbol " + SYMBOL_X , gameBoard.getBoard()[3][5] == SYMBOL_X);
	}
	
	@Test
	public void hasLineShouldReturnTrueIfThereIsALine() {
		gameBoard.placeSymbol(C1,SYMBOL_X);
		gameBoard.placeSymbol(B2,SYMBOL_X);
		gameBoard.placeSymbol(A3,SYMBOL_X);
		assertTrue("The board does not contain a line", gameBoard.hasLine(SYMBOL_X));
	}
	
	@Test
	public void isFullShouldReturnFalseIfThereIsAnEmptyField() {
		gameBoard.placeSymbol(C1,SYMBOL_X);
		assertTrue("The board is full", !gameBoard.isFull());
	}

}
