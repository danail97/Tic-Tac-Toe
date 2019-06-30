package game;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GameRoomTest {

	private GameRoom gameRoom;

	@Mock
	BufferedReader activePlayerBr;

	@Mock
	PrintWriter activePlayerPw;

	@Mock
	PrintWriter waitingPlayerPw;

	@Before
	public void setUp() {
		gameRoom = new GameRoom("room", activePlayerPw, activePlayerBr);
	}

	@Test
	public void makeMoveShouldPlaceSymbolWhereThePlayerChooses() throws IOException {
		// Given
		when(activePlayerBr.readLine()).thenReturn(GameBoardTest.B2);

		// When
		gameRoom.makeMove(activePlayerPw, activePlayerBr, waitingPlayerPw, GameBoardTest.SYMBOL_X);

		// Then
		assertTrue("Field " + GameBoardTest.B2 + " does not contain symbol " + GameBoardTest.SYMBOL_X,
				gameRoom.getGameBoard().getBoard()[3][5] == GameBoardTest.SYMBOL_X);
	}

	@Test
	public void checkIfWonShouldReturnTrueWhenSomeoneWon() {
		gameRoom.getGameBoard().placeSymbol(GameBoardTest.C1, GameBoardTest.SYMBOL_X);
		gameRoom.getGameBoard().placeSymbol(GameBoardTest.B2, GameBoardTest.SYMBOL_X);
		gameRoom.getGameBoard().placeSymbol(GameBoardTest.A3, GameBoardTest.SYMBOL_X);
		gameRoom.checkIfWon(activePlayerPw, waitingPlayerPw, GameBoardTest.SYMBOL_X);

		assertTrue("Noone has won yet", gameRoom.checkIfWon(activePlayerPw, waitingPlayerPw, GameBoardTest.SYMBOL_X));
	}

}
