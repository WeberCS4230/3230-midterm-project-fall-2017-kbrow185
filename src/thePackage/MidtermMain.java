package thePackage;

public class MidtermMain {

	public static void main(String[] args) {

		String ipAddress ="ec2-54-91-0-253.compute-1.amazonaws.com";
		int portID = 8080;
		GuiSection theGui = new GuiSection();
		theGui.startConnection(ipAddress, portID);

	}

}
