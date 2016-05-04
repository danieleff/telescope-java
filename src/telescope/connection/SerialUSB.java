package telescope.connection;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import telescope.Data;

public class SerialUSB extends Telescope {

	private SerialPort serialPort;

	private OutputStream output;

	private InputStream inputStream;
	
	public SerialUSB(Data data) throws Exception {
		super(data);
		
		CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier("COM4");
		
		serialPort = (SerialPort) portId.open(this.getClass().getName(), 1000);
		
		serialPort.setSerialPortParams(9600,
				SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);
		
		output = serialPort.getOutputStream();
		inputStream = serialPort.getInputStream();
		
		new Thread(new Runnable() {
			public void run() {
				readInput();
			}
		}).start();
	}
	private void readInput() {
		BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
		
		while(true) {
			try {
				String line = r.readLine();
				String[] split = line.split(" ");
				setPosition(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			
	}

	@Override
	public void sendData() {
		String string = "M"+data.getGotoX()+" "+data.getGotoY();
		
		System.out.println(string);
		
		try {			
			output.write(string.getBytes("UTF-8"));			
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	@Override
	public void close() {
		serialPort.close();
	}

}
