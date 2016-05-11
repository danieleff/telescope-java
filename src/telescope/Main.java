package telescope;

import telescope.connection.SerialUSB;


public class Main {

	public static void main(String[] args) throws Exception {
        Data data = new Data();

		//new Ps3Controller(data);
		
		SerialUSB telescope = new SerialUSB(data);
		data.setTelescope(telescope);
		
		GUI gui = new GUI(data, telescope);
		data.setGui(gui);
		
	}
	
}
