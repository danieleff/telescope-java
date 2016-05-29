package telescope.coordinate;

import telescope.coordinate.Angle.Type;

public class RightAscension extends Angle {

	public RightAscension() {
		
	}
	
	public RightAscension(Double angle, Type type) {
		super(angle, type);
	}

	public double getHourAngle(int timeSeconds, int timeMillisPart) {
		return Calc.getHourAngle(timeSeconds + timeMillisPart / 1000.0) - getHourMinuteSecond();
	}
	
	
	
}
