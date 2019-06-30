package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import game.GameRoom;

public class Server {
	public static final String SERVER_SOCKET_ERROR = "There is a problem with the server socket!";
	public static final int SERVER_PORT = 1234;
	
	public static Set<String> users = new HashSet<>();
	public static Map<String, GameRoom> rooms = new ConcurrentHashMap<>();


	public static void main(String[] args) throws InterruptedException {
		try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT);) {
			while (true) {
				Socket socket = serverSocket.accept();
				new ServerThread(socket).start();
			}
		} catch (IOException e) {
			System.out.println(SERVER_SOCKET_ERROR);
			e.printStackTrace();
		}
	}
}
