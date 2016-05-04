package telescope.connection;

import telescope.Data;

public class Dummy extends Telescope {

	private Thread thread;
	
	public Dummy(Data data) {
		super(data);
		
		thread = new Thread(new Runnable() {
			public void run() {
				doInBackground();
			}
		});
		thread.start();
	}
	
	private void doInBackground() {
		while(!Thread.currentThread().isInterrupted()) {
			
			int x=getCurrentX();
			if (x<data.getGotoX()) {
				x++;
			} else if (x>data.getGotoX()) {
				x--;
			}
			int y = getCurrentY();
			if (y<data.getGotoY()) {
				y++;
			} else if (y>data.getGotoY()) {
				y--;
			}
			
			setPosition(x, y);
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
	
	@Override
	public void sendData() {
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
