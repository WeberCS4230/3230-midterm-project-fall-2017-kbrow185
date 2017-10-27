package thePackage;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JTextArea;

import blackjack.message.Message;
import blackjack.message.MessageFactory;
import blackjack.message.StatusMessage;

public class TheClient extends Thread {
	Socket clientSocket;
	String serverLocation;
	int thePort;
	String newLine;
	Boolean serverStart;
	ObjectOutputStream dataOut;
	ObjectInputStream dataIn;
	JTextArea theChatWindow;

	public TheClient(String serverName, int port, JTextArea chatWindow) {
		thePort = port;
		serverLocation = serverName;
		newLine = "";
		theChatWindow = chatWindow;
		serverStart = false;
		new Thread(this).start();
	}

	public void run() {
		try {
			clientSocket = new Socket(serverLocation, thePort);
			dataIn = new ObjectInputStream(clientSocket.getInputStream());
			dataOut = new ObjectOutputStream(clientSocket.getOutputStream());
			dataOut.writeObject(MessageFactory.getLoginMessage("Kenyon Rules"));
			dataOut.flush();
			theChatWindow.append("Connecting to:  " + clientSocket.getRemoteSocketAddress() + "\n");
			
			
			serverStart = true;
			while (true) {

				try {
				String message =((Message)dataIn.readObject()).toString();
				theChatWindow.append( message + "\n");
				}catch (IOException | ClassNotFoundException e) {
					//e.printStackTrace();
					//This line is throwing tons of errors due to no username.
				}

			}
			

		} catch (UnknownHostException e) {
			//If theres an issue I can't do much with it here. There is a connection issue and will likely boot client.
			e.printStackTrace();
		} catch (IOException e) {
			//If theres an issue I can't do much with it here. There is a connection issue and will likely boot client.
			e.printStackTrace();
		}

	}

	public void sendNewObject(Message theResponse) throws IOException {
		
		
		if (serverStart) {
			theChatWindow.append("Sending message..");
			dataOut.writeObject(theResponse);
		
		
		}
		dataOut.flush();

	}

}

