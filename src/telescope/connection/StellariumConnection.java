package telescope.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class StellariumConnection {

	private OutputStream outputStream;
	
	private SerialUSB telescope;

	public StellariumConnection(SerialUSB telescope) throws IOException {
		this.telescope = telescope;
		
		ServerSocket server = new ServerSocket(12345);
		
		Thread t = new Thread() {
			public void run() {
				Socket socket;
				try {
					while((socket = server.accept())!=null) {
						newConnection(socket);
					}
				} catch (IOException e) {
					throw new Error(e);
				}
			}
		};
		t.setDaemon(true);
		t.start();
	}
	
	private void newConnection(Socket socket) throws IOException {
		System.out.println("Stellarium connected");
		
		outputStream = socket.getOutputStream();
		
		Thread readThread = new Thread() {
			@Override
			public void run() {
				try {
					read(socket.getInputStream());
				} catch (IOException e) {
					throw new Error(e);
				}
			}
		};
		readThread.setDaemon(true);
		readThread.start();
		
		Thread writeThread = new Thread() {
			@Override
			public void run() {
				try {
					write(socket.getOutputStream());
				} catch (Exception e) {
					throw new Error(e);
				}
			}
		};
		writeThread.setDaemon(true);
		writeThread.start();
	}
	
	private void read(InputStream in) throws IOException {
		//size
		in.read();
		in.read();
		
		//type
		in.read();
		in.read();
		
		//time
		in.read();
		in.read();
		in.read();
		in.read();
		in.read();
		in.read();
		in.read();
		in.read();
		
		int raInt = 0;
		raInt += in.read();
		raInt += in.read() << 8;
		raInt += in.read() << 16;
		raInt += in.read() << 24;
		
		int decInt = 0;
		decInt += in.read();
		decInt += in.read() << 8;
		decInt += in.read() << 16;
		decInt += in.read() << 24;
		
		double ra = raInt / ((double)Integer.MAX_VALUE / 12);
		double dec = decInt / ((double)Integer.MAX_VALUE / 180);
		
		System.out.println(raInt+" "+decInt);
		
		telescope.setGoto((float)ra, (float)dec);
		
		telescope.sendData();
	}
	
	double prevRa = -1;
	
	double prevDec = -1;

	public void write(OutputStream out) throws Exception {
		while(true) {
			Thread.sleep(50);
			double ra = telescope.getRa();
			double dec = telescope.getDec();
			
			if (Math.abs(prevRa - ra) < 0.00001 && Math.abs(prevDec - dec) < 0.00001) {
				continue;
			}
			
			prevRa = ra;
			prevDec = dec;
			
			out.write(24);
			out.write(0);
			
			//type
			out.write(0);
			out.write(0);
			
			//time
			out.write(0);
			out.write(0);
			out.write(0);
			out.write(0);
			out.write(0);
			out.write(0);
			out.write(0);
			out.write(0);
			
			
			//telescope...s
			int raInt = (int) (ra * Integer.MAX_VALUE / 12);
			//ra
			out.write((raInt >>  0) & 0xff);
			out.write((raInt >>  8) & 0xff);
			out.write((raInt >> 16) & 0xff);
			out.write((raInt >> 24) & 0xff);
			
			//dec
			int decInt = (int) (dec * Integer.MAX_VALUE / 180);
			//ra
			out.write((decInt >>  0) & 0xff);
			out.write((decInt >>  8) & 0xff);
			out.write((decInt >> 16) & 0xff);
			out.write((decInt >> 24) & 0xff);
			
			//status
			out.write(0);
			out.write(0);
			out.write(0);
			out.write(0);
			
			out.flush();
			
			
		}
	}

}
