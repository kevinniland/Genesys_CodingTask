package genesys;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author kevin
 * @category Testing
 *
 *           FiveInARowTest.java - Test class for various methods for FiveInARow
 *           game
 */
public class FiveInARowTest {
	FiveInARow fiveInARow = new FiveInARow();
	Socket clientSocket;
	InputStream inputStream;
	OutputStream outputStream;
	char[][] gameBoard;

	@Test
	public void testVertical() {
		Assert.assertTrue(fiveInARow.vertical(), true);
	}

	@Test
	public void testForward() {
		Assert.assertTrue(fiveInARow.forwardSlashDiagonal(), true);
	}

	@Test
	public void testBackslash() {
		Assert.assertTrue(fiveInARow.backSlashDiagonal(), true);
	}

	@Test
	public void testGameBoard() {
		Assert.assertNotNull(fiveInARow);
	}
	
	@Test
	public void testConnection() {
		Assert.assertNotNull(new FiveInARow(clientSocket));
	}
	
	
	@Test
	public void testNoMove() {
		Assert.assertFalse(new FiveInARow().hasWon());
	}
}
