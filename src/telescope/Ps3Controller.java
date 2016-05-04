package telescope;

import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class Ps3Controller {

	private Controller controller;
	
	private Data data;

	public Ps3Controller(Data data) {
		this.data = data;
		
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
					data.setGotoX((int) (data.getGotoX() + deltaX * 40));
					changed=true;
				}
				if (Math.abs(deltaY) > 0.3f) {
					data.setGotoY((int) (data.getGotoY() + deltaY * 40));
					changed=true;
				}
				if (changed) {
					data.update();
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
