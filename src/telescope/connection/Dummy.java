package telescope.connection;


public class Dummy extends Telescope {

	private Thread thread;
	
	public Dummy() {
		
		thread = new Thread(new Runnable() {
			public void run() {
				doInBackground();
			}
		});
		thread.start();
	}
	
	private void doInBackground() {
		while(!Thread.currentThread().isInterrupted()) {
			
			float x=getRa();
			if (x<getGotoRa()) {
				x+=0.1;
			} else if (x>getGotoRa()) {
				x-=0.1;
			}
			float y = getDec();
			if (y<getGotoDec()) {
				y+=0.1;
			} else if (y>getGotoDec()) {
				y-=0.1;
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
