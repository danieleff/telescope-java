package telescope;

import telescope.connection.Telescope;

public class Data {

	private int gotoX = 0;
	
	private int gotoY = 0;

	private GUI gui;

	private Telescope telescope;
	
	public void setGui(GUI gui) {
		this.gui = gui;
	}
	
	public void setTelescope(Telescope telescope) {
		this.telescope = telescope;
	}
	
	public Telescope getTelescope() {
		return telescope;
	}

	public void close() {
		if (telescope!=null) {
			telescope.close();
		}
	}

	public void update() {
		if (gui!=null) {
			gui.setLabels();
		}
		
		if (telescope!=null) {
			telescope.sendData();
		}
	}

	public int getGotoX() {
		return gotoX;
	}
	
	public int getGotoY() {
		return gotoY;
	}

	public void setGotoX(int gotoX) {
		this.gotoX = gotoX;
	}
	
	public void setGotoY(int gotoY) {
		this.gotoY = gotoY;
	}
	
}
