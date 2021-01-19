package genesys;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class GameClient {
	public static void main(String[] args) throws Exception {
		ArrayList<String> playerNames = new ArrayList<String>();
		ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
		Scanner scanner;
		Scanner names = new Scanner(System.in);
		String player1Name, player2Name;
		int moves = 0;

		try (Socket socket = new Socket("127.0.0.1", 59898)) {
			System.out.println("Player 1 name: ");
			player1Name = names.next();
			
			System.out.println("Player 2 name: ");
			player2Name = names.next();
			
			playerNames.add(player1Name);
			playerNames.add(player2Name);
			
			System.out.println("Player " + playerNames.get(0) + "'s move: ");
			moves++;
			
			scanner = new Scanner(System.in);
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
//			oos = new ObjectOutputStream(socket.getOutputStream());
//            ois = new ObjectInputStream(socket.getInputStream());
			
			while (scanner.hasNextLine()) {
				// Determine the current player's name
				if (moves % 2 == 0) {
					System.out.println("Player " + playerNames.get(0) + "'s move: ");
					
					moves++;
				} else if(moves % 2 != 0) {
					System.out.println("Player " + playerNames.get(1) + "'s move: ");
					
					moves++;
				}
				
//				FiveInARow fiveInARow = (FiveInARow) ois.readObject();
//				System.out.println(fiveInARow);
				
				printWriter.println(scanner.nextLine() + "\n");
			}
		}
		
		names.close();
		scanner.close();
	} 
}