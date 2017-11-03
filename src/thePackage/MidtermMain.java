package thePackage;

public class MidtermMain {

	public static void main(String[] args) {

<<<<<<< HEAD
		String ipAddress ="ec2-54-91-0-253.compute-1.amazonaws.com";
=======
		// String ipAddress ="ec2-54-91-0-253.compute-1.amazonaws.com";
		String ipAddress = "localhost";
>>>>>>> HomeWork
		int portID = 8080;
		GuiSection theGui = new GuiSection();
		theGui.startConnection(ipAddress, portID);

	}

}
