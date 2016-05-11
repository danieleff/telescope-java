package telescope.connection;

import telescope.Data;

public abstract class Telescope {

	private int currentX;
	
	private int currentY;

	protected final Data data;
	
	public Telescope(Data data) {
		this.data = data;
	}
	
	public synchronized int getCurrentX() {
		return currentX;
	}
	
	public synchronized int getCurrentY() {
		return currentY;
	}
	
	protected synchronized void setPosition(int currentX, int currentY) {
		this.currentX = currentX;
		this.currentY = currentY;
		data.sendToTelescope();
	}
	
	public abstract void sendData();

	public abstract void close();

}
