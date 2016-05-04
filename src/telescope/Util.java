package telescope;

public class Util {

	public static final int INT_TO_MILIARCSECOND = 1;
	
	public static final int DOUBLE_TO_INT = 60 * 60 * 1000 * INT_TO_MILIARCSECOND;
	
	public static String raToString(Double ra) {
		return toString(ra, false);
	}
	
	public static String decToString(Double dec) {
		return toString(dec, true);
	}
	
	public static String toString(Double value, boolean isDeclincation) {
		if (value == null) {
			return "";
		}
		String ret = "";
		
		if (!isDeclincation && value <= 0) {
			value=-value;
			ret+="-";
		}

	    short degrees = (short) Math.floor(value);
	    double degreesRemainder = value - degrees;
	    
	    short arcminutes = (short) Math.floor(degreesRemainder * 60);
	    double arcminutesRemainder = degreesRemainder * 60 - arcminutes;

	    short arcseconds = (short) Math.floor(arcminutesRemainder * 60);
	    double arcsecondsRemainder = arcminutesRemainder * 60 - arcseconds;

	    short arcmilliseconds = (short) Math.floor(arcsecondsRemainder * 1000);
	    
		ret += degrees;
		ret += !isDeclincation ? "h " : "° ";
		ret += arcminutes;
		ret += !isDeclincation ? "m " : "' ";
		ret += arcseconds;
		ret += ".";
		ret += String.format("%02d", arcmilliseconds / 10);
		ret += !isDeclincation ? "s " : "\"";
		return ret;
	}
	
}
