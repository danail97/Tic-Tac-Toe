package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import server.Server;

public class GameClient {
	private static final String MENU_HEADER = "Available commands :";
	private static final String MENU_CREATE_GAME = "create-game <game-name>";
	private static final String MENU_LIST_GAMES = "list-games";
	private static final String MENU_JOIN_GAME = "join-game [<game-name>]";
	private static final String MENU_EXIT = "exit";
	private static final String HOST = "localhost";
	private static final String UNKNOWN_HOST = "Unknown host!";
	private static final String SOCKET_PROBLEM = "There is a problem with the socket!";

	private String name;

	public GameClient(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void printMenu() {
		System.out.println(MENU_HEADER);
		System.out.println(MENU_CREATE_GAME);
		System.out.println(MENU_LIST_GAMES);
		System.out.println(MENU_JOIN_GAME);
		System.out.println(MENU_EXIT);
	}

	public void connect() {
		try (Socket s = new Socket(HOST, Server.SERVER_PORT);
				PrintWriter pw = new PrintWriter(s.getOutputStream());
				BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));) {
			printMenu();
			sendNameToServer(pw);
			MsgReciever msgReciever = new MsgReciever(br);
			msgReciever.start();
			while (msgReciever.isAlive()) {
				sendCommand(pw);
			}
		} catch (UnknownHostException e) {
			System.out.println(UNKNOWN_HOST);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(SOCKET_PROBLEM);
			e.printStackTrace();
		}
	}

	public void sendNameToServer(PrintWriter pw) {
		pw.println(name);
		pw.flush();
	}

	@SuppressWarnings("resource")
	public void sendCommand(PrintWriter pw) {
		Scanner sc = new Scanner(System.in);
		String command = sc.nextLine();
		pw.println(command);
		pw.flush();
	}
}
