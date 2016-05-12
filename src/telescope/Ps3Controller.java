package telescope;

import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import telescope.connection.Telescope;

public class Ps3Controller {

	private Controller controller;
	
	private Telescope telescope;

	public Ps3Controller(Telescope telescope) {
		this.telescope = telescope;
		
		ControllerEnvironment e = ControllerEnvironment.getDefaultEnvironment();
		for (Controller c : e.getControllers()) {
			if ("Controller (Xbox 360 Wireless Receiver for Windows)".equals(c.getName())) {
				controller = c;
			}
		}
		if (controller != null) {
			new Thread(new Runnable() {
				public void run() {
					checkController();
				}
			}).start();
		} 
		
	}
	
	private void checkController() {
		while(true) {
			if (controller.poll()) {
				float deltaX = controller.getComponent(Identifier.Axis.X).getPollData();
				float deltaY = controller.getComponent(Identifier.Axis.Y).getPollData();
				boolean changed = false;
				if (Math.abs(deltaX) > 0.3f) {
					telescope.setGoto((int) (telescope.getGotoRa() + deltaX * 40), telescope.getGotoDec());
					changed=true;
				}
				if (Math.abs(deltaY) > 0.3f) {
					telescope.setGoto(telescope.getGotoRa(), ((int)telescope.getGotoDec() + deltaY * 40));
					changed=true;
				}
				
				if (changed) {
					telescope.sendData();
				}
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				throw new Error(e);
			}
		}
	}
	
}
