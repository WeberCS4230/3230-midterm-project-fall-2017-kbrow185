package thePackage;

public class MidtermMain {

	public static void main(String[] args) {

		String ipAddress ="ec2-54-172-123-164.compute-1.amazonaws.com";
		int portID = 8989;
		GuiSection theGui = new GuiSection();
		theGui.startConnection(ipAddress, portID);

	}

}

///Start sends join to everyone. So you can join game about to start. 
//ack message removed