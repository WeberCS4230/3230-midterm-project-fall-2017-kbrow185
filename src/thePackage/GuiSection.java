package thePackage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

import blackjack.Server;
import blackjack.message.LoginMessage;
import blackjack.message.MessageFactory;

public class GuiSection extends JFrame {

	private static final long serialVersionUID = 1L;

	//String ipAddress ="ec2-54-91-0-253.compute-1.amazonaws.com";
	JTextArea chatWindow;
	JTextArea userInputTextBox;
	TheClient client;
	boolean login;
	String userName;

	public GuiSection() {
		login = false;
		userName = "No Name";

		Box theBox = Box.createVerticalBox();
		chatWindow = new JTextArea(40, 10);
		chatWindow.setEditable(false);
		JScrollPane theScroller = new JScrollPane(chatWindow);
		theBox.add(theScroller);

		theBox.add(new JLabel("User Input Below"));
		userInputTextBox = new JTextArea(2, 10);
		theBox.add(userInputTextBox);

		JButton addChatButton = new JButton();
		addChatButton.setText("Submit");
		addChatButton.setAlignmentX(CENTER_ALIGNMENT);
		addChatButton.setMnemonic(KeyEvent.VK_ENTER);
		addChatButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendToServer(userInputTextBox.getText());
				userInputTextBox.setText("");
			}
		});

		theBox.add(addChatButton);
		

		DefaultCaret caret = (DefaultCaret) chatWindow.getCaret();
		caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
		
		this.add(theBox);
		this.setTitle("Chat Window");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 800);
		this.setVisible(true);

	}

	public void startConnection() {
		String ipAddress = "localhost";
		int portID = 8080;	
		
		try {
			new Server(portID);
			client = new TheClient(ipAddress,portID,chatWindow);
			chatWindow.append("Connection Successful");
		} catch (IOException e) {
			//An exception task isnt really needed here. If the test fails it will just return false.
			chatWindow.append("Connection Unsuccessful");
		}
		
	}


	public void defaultAddToChat(String username, String sentence) {
		chatWindow.append(username + ":\t" + sentence + "\n");
	}

	public void sendToServer(String input) {
		//if(!login) {
		LoginMessage message =MessageFactory.getLoginMessage(input);
	//	}

			try {
				client.sendNewObject(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


	}

}
