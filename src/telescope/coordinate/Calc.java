package telescope.coordinate;

import telescope.Util;


public class Calc {

	private double[][] t;
	
	private Coordinate telescope1;
	
	private CoordinateAtTime star1;
	
	private Coordinate telescope2;
	
	private CoordinateAtTime star2;
	
	public static void main(String[] args) {
		Calc calc = new Calc();
		float hourAngle = calc.getHourAngle(System.currentTimeMillis() / 1000);
		
		System.out.println(hourAngle);
		System.out.println(Util.raToString((double) hourAngle));
	}
	
	public void setCoordinate1(Coordinate telescope, CoordinateAtTime star) {
		this.telescope1 = telescope;
		this.star1 = star;
	}
	
	public void setTelescope2(Coordinate telescope, CoordinateAtTime star) {
		this.telescope2 = telescope;
		this.star2 = star;
	}

	public static float getHourAngle(double timeSeconds) {
		double ut1Seconds = timeSeconds - 946728000L;
		double ut1 = ut1Seconds / 60.0 / 60 / 24;
		
		System.out.println("UT1: " + ut1);
		
		double greenwichMeanSideralTime = 18.697374558 + 24.06570982441908 * ut1;
		
		System.out.println(greenwichMeanSideralTime);
		
		double longitude = 20.584497f;
		double localSideralTime = greenwichMeanSideralTime + longitude / 15;
		
		localSideralTime += 24;
		System.out.println("localSideralTime: "+localSideralTime);
		
		return (float) ((localSideralTime + 24) % 24);
	}

	public void recalculate() {
		/*
        float telescope1Ra = 276.03f;
        float telescope1Dec = 52.5f;
        float real1Ra = 4f * 15; // Hour Angle
        float real1Dec = 46;
        
        float telescope2Ra = 289.9f;
        float telescope2Dec = 48.95f;
        float real2Ra = 4.85f * 15; // Hour Angle
        float real2Dec = 49.8f;
*/
		
		double [][] lmn = raDecMatrix(telescope1.getRightAngle().getRadians(), 
				telescope1.getDeclination().getRadians(), 
				star1.getCoordinate().getRightAngle().getHourAngle(star1.getTimeSeconds(), 0),
				star1.getCoordinate().getDeclination().getRadians());
		
		double [][] LMN = inverse(raDecMatrix(telescope2.getRightAngle().getRadians(), 
				telescope2.getDeclination().getRadians(), 
				star2.getCoordinate().getRightAngle().getHourAngle(star2.getTimeSeconds(), 0),
				star2.getCoordinate().getDeclination().getRadians()));
		
		t=mul(lmn, LMN);
	}
	
	public Coordinate calculate() {
		
		double telescopeRa = Math.toRadians(238.25f);
		double telescopeDec = Math.toRadians(25.1f);
		double realRa = Math.toRadians(3.44f * 15); // Hour Angle
		double realDec = Math.toRadians(7.4f);

		double L = (Math.cos(realDec) * Math.cos(realRa));
		double M = (Math.cos(realDec) * Math.sin(realRa));
		double N = (Math.sin(realDec));
        
        double sinDec = t[0][2] * L + t[1][2] * M + t[2][2] * N;
        double sinRa = ((t[0][1] * L + t[1][1] * M + t[2][1] * N) / Math.cos(Math.asin(sinDec)));
        
        System.out.println(telescopeDec);
        System.out.println(Math.asin(sinDec));
        
        System.out.println(telescopeRa);
        System.out.println(180 - Math.toDegrees(Math.asin(sinRa)));
        System.out.println(-Math.asin(sinRa) + Math.PI);
        
        return null;
	}
	
	private static double[][] mul(double[][] lmn, double[][] LMN) {
		double[][] t=new double[3][3];
		for(int i=0;i<=2;i++) {
			for(int j=0;j<=2;j++) {
				t[i][j] = lmn[0][j] * LMN[i][0] + lmn[1][j] * LMN[i][1] + lmn[2][j] * LMN[i][2];
			}
		}
		return t;
	}

	private static double[][] raDecMatrix(double ra1, double dec1, double ra2, double dec2) {
		double [][] LMN = new double[3][3];
		
        LMN[0][0] = (Math.cos(dec1) * Math.cos(ra1));
        LMN[0][1] = (Math.cos(dec1) * Math.sin(ra1));
        LMN[0][2] = (Math.sin(dec1));
		
        LMN[1][0] = (Math.cos(dec2) * Math.cos(ra2));
        LMN[1][1] = (Math.cos(dec2) * Math.sin(ra2));
        LMN[1][2] = (Math.sin(dec2));
		
        double b = (1 / 
				Math.sqrt(
						(LMN[0][1]*LMN[1][2]-LMN[0][2]*LMN[1][1]) * (LMN[0][1]*LMN[1][2]-LMN[0][2]*LMN[1][1])
						+
						(LMN[0][2]*LMN[1][0]-LMN[0][0]*LMN[1][2]) * (LMN[0][2]*LMN[1][0]-LMN[0][0]*LMN[1][2])
						+
						(LMN[0][0]*LMN[1][1]-LMN[0][1]*LMN[1][0]) * (LMN[0][0]*LMN[1][1]-LMN[0][1]*LMN[1][0])
						)
				);

		LMN[2][0] = b * (LMN[0][1]*LMN[1][2]-LMN[0][2]*LMN[1][1]);
		LMN[2][1] = b * (LMN[0][2]*LMN[1][0]-LMN[0][0]*LMN[1][2]);
		LMN[2][2] = b * (LMN[0][0]*LMN[1][1]-LMN[0][1]*LMN[1][0]);
		return LMN;
	}

	private static double[][] inverse(double[][] m) {
		double det = m[0][0] * (m[1][1] * m[2][2] - m[2][1] * m[1][2]) -
	             m[0][1] * (m[1][0] * m[2][2] - m[1][2] * m[2][0]) +
	             m[0][2] * (m[1][0] * m[2][1] - m[1][1] * m[2][0]);

		double invdet = 1 / det;
	
		double[][] inv=new double[3][3];
		inv[0][0] = (m[1][1] * m[2][2] - m[2][1] * m[1][2]) * invdet;
		inv[0][1] = (m[0][2] * m[2][1] - m[0][1] * m[2][2]) * invdet;
		inv[0][2] = (m[0][1] * m[1][2] - m[0][2] * m[1][1]) * invdet;
		inv[1][0] = (m[1][2] * m[2][0] - m[1][0] * m[2][2]) * invdet;
		inv[1][1] = (m[0][0] * m[2][2] - m[0][2] * m[2][0]) * invdet;
		inv[1][2] = (m[1][0] * m[0][2] - m[0][0] * m[1][2]) * invdet;
		inv[2][0] = (m[1][0] * m[2][1] - m[2][0] * m[1][1]) * invdet;
		inv[2][1] = (m[2][0] * m[0][1] - m[0][0] * m[2][1]) * invdet;
		inv[2][2] = (m[0][0] * m[1][1] - m[1][0] * m[0][1]) * invdet;
		return inv;
	}
	
}
