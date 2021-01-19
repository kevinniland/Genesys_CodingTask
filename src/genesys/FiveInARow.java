package genesys;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Kevin Niland
 * @category Game Logic & Server
 * 
 *           FiveInARow.java - Contains the logic for the game and server code
 *
 */
class FiveInARow implements Runnable, Serializable {
	private static final long serialVersionUID = -1039174727272205844L;
	private Socket socket;
	private int width, height; // Board dimensions
	private static final char[] PLAYERS = { 'X', 'O' }; // Board pieces
	private char[][] gameBoard; // Game board
	private int lastCol = -1, lastRow = -1; // Last moves made by the player

	public FiveInARow() {

	}

	public FiveInARow(Socket socket) {
		this.socket = socket;
	}

	/**
	 * Constructor to initialise the board
	 * 
	 * @param w - Width
	 * @param h - Height
	 */
	public FiveInARow(int w, int h) {
		width = w;
		height = h;
		gameBoard = new char[h][];

		// Initialise the game board with a blank cell
		for (int i = 0; i < h; i++) {
			Arrays.fill(gameBoard[i] = new char[w], '.');
		}
	}

	/**
	 * Display the game board - For that, we define a toString(). For the sake of
	 * brevity, we use the new features introduced by Java 8, including the famous
	 * Streams
	 */
	public String toString() {
		return IntStream.range(0, width).mapToObj(Integer::toString).collect(Collectors.joining("")) + "\n"
				+ Arrays.stream(gameBoard).map(String::new).collect(Collectors.joining("\n"));
	}

	/**
	 * To check if a player has won after placing a disc we need to be able to get a
	 * representation of a line horizontally or vertically but also diagonally. In
	 * order to get these representations, we will use the variables lastCol and
	 * lastTop containing the last position played by a player. To get the
	 * representation of a horizontal line, we have to build a String from the
	 * lastTop line of the grid array.
	 */

	// Get string representation of the row containing the last play of the user
	public String horizontal() {
		return new String(gameBoard[lastRow]);
	}

	// Get string representation of the column containing the last play of the user
	public String vertical() {
		StringBuilder stringBuilder = new StringBuilder(height);

		for (int h = 0; h < height; h++) {
			stringBuilder.append(gameBoard[h][lastCol]);
		}

		return stringBuilder.toString();
	}

	/**
	 * Get string representation of the "/" diagonal containing the last play of the
	 * user
	 * 
	 * @return stringBuilder
	 */
	public String forwardSlashDiagonal() {
		StringBuilder stringBuilder = new StringBuilder(height);

		for (int h = 0; h < height; h++) {
			int w = lastCol + lastRow - h;

			if (w >= 0 && w < width) {
				stringBuilder.append(gameBoard[h][w]);
			}
		}

		return stringBuilder.toString();
	}

	/**
	 * Get string representation of the "\" diagonal containing the last play of the
	 * user
	 * 
	 * @return stringBuilder
	 */
	public String backSlashDiagonal() {
		StringBuilder stringBuilder = new StringBuilder(height);

		for (int h = 0; h < height; h++) {
			int w = lastCol - lastRow + h;

			if (0 <= w && w < width) {
				stringBuilder.append(gameBoard[h][w]);
			}
		}

		return stringBuilder.toString();
	}

	/**
	 * 
	 * @param str
	 * @param subString
	 * @return
	 */
	public static boolean contains(String str, String subString) {
		return str.indexOf(subString) >= 0;
	}

	/**
	 * Determine if a game as been won
	 * 
	 * @return
	 */
	public boolean hasWon() {
		if (lastCol == -1) {
			System.err.println("No move has been made yet");

			return false;
		}

		char symbol = gameBoard[lastRow][lastCol];

		/**
		 * Streak is used determine if a game has been won
		 */
		String streak = String.format("%c%c%c%c%c", symbol, symbol, symbol, symbol, symbol);

		/**
		 * Check if a the same character is in consecutive order horizontally,
		 * vertically, or diagonally
		 */
		return contains(horizontal(), streak) || contains(vertical(), streak)
				|| contains(forwardSlashDiagonal(), streak) || contains(backSlashDiagonal(), streak);
	}

	/**
	 * Play a move on the board
	 * 
	 * @param symbol  - Symbol/piece to be played
	 * @param scanner - Input
	 */
	public void playMove(char symbol, Scanner scanner) {
		do {
			int col = scanner.nextInt();

			// Check if input is valid
			if (!(0 <= col && col < width)) {
				System.out.println("Column must be between 0 and " + (width - 1));
				continue;
			}

			for (int h = height - 1; h >= 0; h--) {
				if (gameBoard[h][col] == '.') {
					gameBoard[lastRow = h][lastCol = col] = symbol;

					return;
				}
			}

			// If column has already been filled, we need to ask for a new input
			System.out.println("Column " + col + " is full");
		} while (true);
	}

	@Override
	public synchronized void run() {
		int height = 6;
		int width = 9;
		int moves = height * width;

		System.out.println("Connected: " + socket);

		try {
			FiveInARow fiveInARow = new FiveInARow(width, height);
			Scanner scanner = new Scanner(socket.getInputStream());
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
//			ObjectOutputStream oos = null;
//	        ObjectInputStream ois = null;
//	        oos = new ObjectOutputStream(socket.getOutputStream());
//          ois = new ObjectInputStream(socket.getInputStream());

//            oos.writeObject(fiveInARow);
			System.out.println(fiveInARow);

			for (int player = 0; moves-- > 0; player = 1 - player) {
				char symbol = PLAYERS[player];

				fiveInARow.playMove(symbol, scanner);

				System.out.println(fiveInARow);
				printWriter.println(fiveInARow);
//				oos.writeObject(fiveInARow);

				if (fiveInARow.hasWon()) {
					System.out.println("\nPlayer " + symbol + " wins!");

					return;
				}
			}
		} catch (Exception exception) {
			System.out.println("Error: " + socket);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
			}

			System.out.println("Closed: " + socket);
		}
	}

	/**
	 * Main method for Server class
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		try (ServerSocket serverSocket = new ServerSocket(59898)) {
			System.out.println("The game server is running...");

			// Create a thread pool for the client
			ExecutorService pool = Executors.newFixedThreadPool(20);

			while (true) {
				pool.execute(new FiveInARow(serverSocket.accept()));
			}
		}
	}
}