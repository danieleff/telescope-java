package telescope.coordinate;

public class Coordinate {

	private RightAscension ra;
	
	private Declination dec;

	public Coordinate(RightAscension ra, Declination dec) {
		this.ra = ra;
		this.dec = dec;
	}
	
	public RightAscension getRightAngle() {
		return ra;
	}
	
	public Declination getDeclination() {
		return dec;
	}
	
	
	
}
