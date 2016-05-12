package telescope.connection;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

public class SerialUSB extends Telescope {

	private SerialPort serialPort;

	private OutputStream output;

	private InputStream inputStream;
	
	public SerialUSB() throws Exception {
		super();
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
					if (line.startsWith("Pos ")) {
						String[] split = line.split(" ");
						
						try{
							setPosition(Float.parseFloat(split[1]), Float.parseFloat(split[2]));
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
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
		String string = "Move "+getGotoRa()+" "+getGotoDec()+"\n";
		
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
