package telescope.coordinate;

public class Angle {
	
	private Double angleRad;

	public Angle() {
		
	}
	
	public Angle(Double angle, Type type) {
		if (angle==null) {
			angle = null;
		} else if (type == Type.RADIANS) {
			setRadians(angle);
		} else if (type == Type.DEGREES) {
			setDegrees(angle);
		} else if (type == Type.HOUR_MINUTE_SECOND) {
			setHourMinuteSecond(angle);
		}
	}
	
	public void setDegrees(double angle) {
		this.angleRad = angle;
	}
	
	public void setRadians(double angle) {
		this.angleRad = Math.toRadians(angle);
	}
	
	public void setHourMinuteSecond(double angle) {
		this.angleRad = angle * 15;
	}
	
	public double getDegrees() {
		return angleRad;
	}
	
	public double getRadians() {
		return Math.toRadians(angleRad);
	}
	
	public double getHourMinuteSecond() {
		return angleRad / 15;
	}
	
	public static enum Type {
		RADIANS,
		DEGREES,
		HOUR_MINUTE_SECOND
	}
	
}
