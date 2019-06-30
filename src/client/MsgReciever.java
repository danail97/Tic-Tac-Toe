package client;

import java.io.BufferedReader;
import java.io.IOException;

public class MsgReciever extends Thread {
	private static final String USER_ALLREADY_CONNECTED = "User already connected!";
	private static final String GOODBYE = "Goodbye.";
	
	private BufferedReader br;
	
	public MsgReciever(BufferedReader br) {
		this.br = br;
	}
	
	public void run() {
		String line;
		boolean read = true;
		while (read) {
			try {
				if (br.ready()) {
					while ((line = br.readLine())!=null) {
						System.out.println(line);
						if(line.equals(USER_ALLREADY_CONNECTED)) {
							return;
						}
						if(line.equals(GOODBYE)) {
							return;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
