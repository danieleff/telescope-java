package telescope.coordinate;

public class CoordinateAtTime {

	private Coordinate coordinate;
	
	private int timeSeconds;

	public CoordinateAtTime(Coordinate coordinate, int timeSeconds) {
		this.coordinate = coordinate;
		this.timeSeconds = timeSeconds;
	}
	
	public Coordinate getCoordinate() {
		return coordinate;
	}
	
	public int getTimeSeconds() {
		return timeSeconds;
	}
	
}
