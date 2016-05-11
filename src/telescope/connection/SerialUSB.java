package telescope.connection;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import telescope.Data;

public class SerialUSB extends Telescope {

	private SerialPort serialPort;

	private OutputStream output;

	private InputStream inputStream;
	
	public SerialUSB(Data data) throws Exception {
		super(data);
	}
	
	public void connect(String port) throws Exception {
		
		CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(port);
		
		serialPort = (SerialPort) portId.open(this.getClass().getName(), 1000);
		
		serialPort.setSerialPortParams(115200,
				SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);
		
		output = serialPort.getOutputStream();
		inputStream = serialPort.getInputStream();
		
		output.write("\n".getBytes("ASCII"));
		
		output.write(("Time " + System.currentTimeMillis() / 1000+"\n").getBytes("ASCII"));
		
		new Thread(new Runnable() {
			public void run() {
				readInput();
			}
		}).start();
	}
	
	public void disconnect() {
		if (serialPort!=null) {
			serialPort.close();
			serialPort = null;
		}
	}
	
	private void readInput() {
		BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
		
		while(true) {
			try {
				if (r.ready()) {
					String line = r.readLine();
					System.out.println("Got: " + line);
					
					//String[] split = line.split(" ");
					//setPosition(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
				} else {
					Thread.sleep(10);
				}
			} catch (Exception e) {
				throw new Error(e);
			}
		}
			
	}

	@Override
	public void sendData() {
		String string = "Move "+data.getGotoX()+" "+data.getGotoY()+"\n";
		
		System.out.println(string);
		
		try {			
			output.write(string.getBytes("UTF-8"));
			output.flush();
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	@Override
	public void close() {
		serialPort.close();
	}
	
	@SuppressWarnings("unchecked")
	public Enumeration<CommPortIdentifier> getComPorts() {
		return CommPortIdentifier.getPortIdentifiers();
	}

}
