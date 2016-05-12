package telescope;

import telescope.connection.SerialUSB;
import telescope.connection.StellariumConnection;


public class Main {

	public static void main(String[] args) throws Exception {
		//new Ps3Controller(data);
		
		SerialUSB telescope = new SerialUSB();
		
		new StellariumConnection(telescope);
		
		GUI gui = new GUI(telescope);
			
	}
	
}
