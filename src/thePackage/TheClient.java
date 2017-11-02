package thePackage;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JTextArea;

import blackjack.game.Card;
import blackjack.message.CardMessage;
import blackjack.message.ChatMessage;
import blackjack.message.GameActionMessage;
import blackjack.message.GameStateMessage;
import blackjack.message.LoginMessage;
import blackjack.message.Message;
import blackjack.message.MessageFactory;
import blackjack.message.Message.MessageType;

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
					translateMessage(((Message) dataIn.readObject()));

				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
					//Will be booted from server if theres an issue. No fix is required here.
				}

			}

		} catch (IOException e) {
			// If theres an issue I can't do much with it here. There is a connection issue
			// and will likely boot client.
			e.printStackTrace();
		}

	}

	public void sendNewObject(Message theResponse) throws IOException {
		if (serverStart) {
			dataOut.writeObject(theResponse);
			theChatWindow.append("Sending message.. \n");
		}
		dataOut.flush();
	}

	private void translateMessage(Message message) {
		String response = "UNKNOWN";
		if (message.getType().equals(MessageType.LOGIN)) {
			response = ((LoginMessage) message).getUsername();
		}
		else if (message.getType().equals(MessageType.ACK)) {
			response = "You've been acknowledged.";
		} else if (message.getType().equals(MessageType.DENY)) {
			response = "You've been denied.";
		} else if (MessageType.CHAT.equals(message.getType())) {
			response = ((ChatMessage) message).getUsername().toString() + ": " +((ChatMessage) message).getText();
		} else if (MessageType.GAME_STATE.equals(message.getType())) {
			response = "Server Game State is: " + ((GameStateMessage) message).getRequestedState().toString();
		} else if (MessageType.GAME_ACTION.equals(message.getType())) {
			response = "Server game action is: " + ((GameActionMessage) message).getAction().toString();
		}
		else if(MessageType.CARD.equals(message.getType())){
			Card theCard = ((CardMessage)message).getCard();
			response = "card value :" + theCard.getValue().toString() + " Suite:" + theCard.getSuite().toString();
		}

		theChatWindow.append(response + "\n");

	}

}
