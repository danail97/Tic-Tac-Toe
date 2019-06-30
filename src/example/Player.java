package example;

import java.util.Scanner;

import client.GameClient;

public class Player {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Enter your name: ");
		Scanner sc = new Scanner(System.in);
		String name = sc.nextLine();
		GameClient player = new GameClient(name);
		player.connect();
	}
	
}
