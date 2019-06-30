package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import game.GameRoom;

public class ServerThread extends Thread {

	private static final String DISCONNECTED = " disconnected";
	private static final String READING_EXCEPTION = "An error occurred while reading!";
	private static final String USER_ALLREADY_CONNECTED = "User already connected!";
	private static final String CREATE_GAME = "create-game";
	private static final String LIST_GAMES = "list-games";
	private static final String JOIN_GAME = "join-game";
	private static final String EXIT = "exit";
	private static final String INVALID_COMMAND = "Invalid command!";
	private static final String GAME_CREATED = "Created game ";
	private static final String WAITING_FOR_SECOUND_PLAYER = "Waiting for secound player...";
	private static final String THREAD_INTERUPTED = "Thread interupted!";
	private static final String ROOM_ALLREADY_EXISTS = "There is a room with this name!";
	private static final String NO_SUCH_ROOM = "This room doesn't exist!";
	private static final String NO_GAMES_AVAILABLE = "No games available.";
	private static final String GOODBYE = "Goodbye.";

	private Socket socket;
	private String player;
	private String roomName;
	private boolean inGame;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;

	public ServerThread(Socket socket) {
		this.socket = socket;
		addUser();
	}

	public synchronized void addUser() {
		inGame = true;
		try{
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printWriter = new PrintWriter(socket.getOutputStream());
			player = bufferedReader.readLine();
			if (Server.users.contains(player)) {
				printWriter.println(USER_ALLREADY_CONNECTED);
				return;
			} else {
				Server.users.add(player);
			}
		} catch (IOException e) {
			System.out.println(player + DISCONNECTED);
			e.printStackTrace();
		}finally {
			cleanUpAfterUser();
		}
	}

	public void run() {
		while (inGame) {
			try {
				String clientMsg = bufferedReader.readLine();
				String[] words = clientMsg.split(" ");
				String command = words[0];
				if (words.length > 1) {
					roomName = clientMsg.substring(words[0].length() + 1);
				}
				runCommand(command);
			} catch (IOException e) {
				System.out.println(READING_EXCEPTION);
				e.printStackTrace();
			} finally {
				cleanUpAfterUser();
			}

		}
	}

	public void runCommand(String command) {
		switch (command) {
		case CREATE_GAME: {
			createGame();
			break;
		}
		case LIST_GAMES: {
			listGames();
			break;
		}

		case JOIN_GAME: {
			joinGame();
			break;
		}
		case EXIT: {
			exit();
			break;
		}
		default: {
			printWriter.println(INVALID_COMMAND);
			printWriter.flush();
		}
		}
	}

	public boolean createGame() {
		if (roomName != null && !Server.rooms.containsKey(roomName)) {
			Server.rooms.put(roomName, new GameRoom(roomName, printWriter, bufferedReader));
			printWriter.println(GAME_CREATED + roomName);
			printWriter.println(WAITING_FOR_SECOUND_PLAYER);
			printWriter.flush();
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					System.out.println(THREAD_INTERUPTED);
					e.printStackTrace();
				}
			}
			return true;
		} else {
			printWriter.println(ROOM_ALLREADY_EXISTS);
			printWriter.flush();
			return false;
		}
	}

	public void joinGame() {

		if (!Server.rooms.containsKey(roomName)) {
			printWriter.println(NO_SUCH_ROOM);
			printWriter.flush();
		}
		Server.rooms.get(roomName).joinRoom(printWriter, bufferedReader);
	}

	public void listGames() {
		if (!Server.rooms.isEmpty()) {
			for (GameRoom room : Server.rooms.values()) {
				printWriter.println(room.getRoomName());
			}
		} else {
			printWriter.println(NO_GAMES_AVAILABLE);
		}
		printWriter.flush();
	}

	public void exit() {
		inGame = false;
		printWriter.println(GOODBYE);
		printWriter.flush();
	}

	public void deletePlayer(String player) {
		if (Server.users.contains(player)) {
			Server.users.remove(player);
		}
	}

	public void deleteRoom(String currentGame) {
		if (!currentGame.equals(null)) {
			if (Server.rooms.containsKey(currentGame)) {
				Server.rooms.remove(currentGame);
			}
		}
	}
	
	public void cleanUpAfterUser() {
		deletePlayer(player);
		if (roomName != null) {
			deleteRoom(roomName);
		}
	}

}
