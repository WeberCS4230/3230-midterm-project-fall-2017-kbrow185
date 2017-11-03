package thePackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import blackjack.message.ChatMessage;
import blackjack.message.MessageFactory;

public class GuiSection extends JFrame {

	public static enum ActionMessages {
		HIT, STAY, START, JOIN
	};

	private static final long serialVersionUID = 1L;

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

		addButtons(theBox);

		DefaultCaret caret = (DefaultCaret) chatWindow.getCaret();
		caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);

		this.add(theBox);
		this.setTitle("BlackJack");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500, 900);
		this.setVisible(true);
	}

	private void addButtons(Box theBox) {
		JButton sendHitButton = new JButton();
		sendHitButton.setText("HIT");
		sendHitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendActionToServer(ActionMessages.HIT);
			}
		});
		JButton sendStayButton = new JButton();
		sendStayButton.setText("STAY");
		sendStayButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendActionToServer(ActionMessages.STAY);
			}
		});
		JButton joinGameButton = new JButton();
		joinGameButton.setText("Join Game");
		joinGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendActionToServer(ActionMessages.JOIN);
			}
		});
		JButton startGameButton = new JButton();
		startGameButton.setText("Start Game");
		startGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendActionToServer(ActionMessages.START);
			}

		});
		JButton addChatButton = new JButton();
		addChatButton.setText("Submit Chat");
		addChatButton.setAlignmentX(LEFT_ALIGNMENT);
		addChatButton.setMnemonic(KeyEvent.VK_ENTER);
		addChatButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendChatToServer(userInputTextBox.getText());
				userInputTextBox.setText("");
			}
		});
		theBox.add(addChatButton);
		theBox.add(joinGameButton);
		theBox.add(sendHitButton);
		theBox.add(sendStayButton);
		theBox.add(startGameButton);

	}

	// The GUI owns the client because the client requires the chatbox to return
	// messages.
	// If the MidTermMain owned the client, there would be dual ownership to pass
	// back and forth the chatbox.
	// The other option would be to have a 'listener' thread to update, but due to
	// my previous assignment and current knowledge,
	// that isn't the best idea for the midterm.
	public void startConnection(String ipAddress, int portID) {
		chatWindow.append("Attempting Connection... \n");
		client = new TheClient(ipAddress, portID, chatWindow);
	}

	private void sendChatToServer(String sentence) {

		ChatMessage message = MessageFactory.getChatMessage(sentence);
		try {
			client.sendNewObject(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void sendActionToServer(ActionMessages am) {

		try {

			switch (am) {
			case HIT: {
				client.sendNewObject(MessageFactory.getHitMessage());
				break;
			}
			case STAY: {
				client.sendNewObject(MessageFactory.getStayMessage());
				break;
			}
			case JOIN: {
				client.sendNewObject(MessageFactory.getJoinMessage());
				break;
			}
			case START: {
				client.sendNewObject(MessageFactory.getStartMessage());
				break;
			}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
