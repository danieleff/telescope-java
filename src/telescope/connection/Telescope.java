package telescope.connection;


public abstract class Telescope {

	private float ra;
	
	private float dec;

	private float gotoRa;
	
	private float gotoDec ;
	
	public synchronized float getRa() {
		return ra;
	}
	
	public synchronized float getDec() {
		return dec;
	}
	
	public synchronized float getGotoRa() {
		return gotoRa;
	}
	
	public synchronized float getGotoDec() {
		return gotoDec;
	}

	public void setGoto(float gotoRa, float gotoDec) {
		this.gotoRa = gotoRa;
		this.gotoDec = gotoDec;
	}
	
	protected synchronized void setPosition(float ra, float dec) {
		this.ra = ra;
		this.dec = dec;
	}
	
	public abstract void sendData();

	public abstract void close();

}
