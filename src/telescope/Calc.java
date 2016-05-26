package telescope;

public class Calc {

	public static void main(String[] args) {
		
		//http://www.geocities.jp/toshimi_taki/aim/aim.htm
		
		int time1 = 1023;
        float telescope1Ra = 40.5f; // Altitude Telescope
        float telescope1Dec = 341.1f; // Azimuth Telescope
        float real1Ra = 5.25f * 15; // Real
        float real1Dec = 46f; // Real

        System.out.println(Math.cos(Math.toRadians(real1Dec)) * Math.cos(Math.toRadians(6.7f * 15 - real1Ra)));
        System.out.println(Math.sin(Math.toRadians(telescope1Ra)));
		
		int time2 = 1023;
        float relescope2Ra = 175.6f;
        float telescope2Dec = 73.25f;
        float real2Ra = 6.75f * 15;
        float real2Dec = -16.75f;
		
        telescope1Ra = (float) Math.toRadians(telescope1Ra);
        telescope1Dec = (float) Math.toRadians(telescope1Dec);
        real1Ra = (float) Math.toRadians(real1Ra);
        real1Dec = (float) Math.toRadians(real1Dec);

        relescope2Ra = (float) Math.toRadians(relescope2Ra);
        telescope2Dec = (float) Math.toRadians(telescope2Dec);
        real2Ra = (float) Math.toRadians(real2Ra);
        real2Dec = (float) Math.toRadians(real2Dec);

		float [][] lmn = new float[3][3];
		float [][] LMN = new float[3][3];
		
        lmn[0][0] = (float) (Math.cos(telescope1Dec) * Math.cos(telescope1Ra));
        lmn[0][1] = (float) (Math.cos(telescope1Dec) * Math.sin(telescope1Ra));
        lmn[0][2] = (float) (Math.sin(telescope1Dec));
		
        LMN[0][0] = (float) (Math.cos(real1Dec) * Math.cos(real1Ra));
        LMN[0][1] = (float) (Math.cos(real1Dec) * Math.sin(real1Ra));
        LMN[0][2] = (float) (Math.sin(real1Dec));
		
        lmn[1][0] = (float) (Math.cos(telescope2Dec) * Math.cos(relescope2Ra));
        lmn[1][1] = (float) (Math.cos(telescope2Dec) * Math.sin(relescope2Ra));
        lmn[1][2] = (float) (Math.sin(telescope2Dec));
		
        LMN[1][0] = (float) (Math.cos(real2Dec) * Math.cos(real2Ra));
        LMN[1][1] = (float) (Math.cos(real2Dec) * Math.sin(real2Ra));
        LMN[1][2] = (float) (Math.sin(real2Dec));
		
		float a = (float) (1 / 
				Math.sqrt(
						(lmn[0][1]*lmn[1][2]-lmn[0][2]*lmn[1][1]) * (lmn[0][1]*lmn[1][2]-lmn[0][2]*lmn[1][1])
						+
						(lmn[0][2]*lmn[1][0]-lmn[0][0]*lmn[1][2]) * (lmn[0][2]*lmn[1][0]-lmn[0][0]*lmn[1][2])
						+
						(lmn[0][0]*lmn[1][1]-lmn[0][1]*lmn[1][0]) * (lmn[0][0]*lmn[1][1]-lmn[0][1]*lmn[1][0])
						)
				);
		
		lmn[2][0] = a * (lmn[0][1]*lmn[1][2]-lmn[0][2]*lmn[1][1]);
		lmn[2][1] = a * (lmn[0][2]*lmn[1][0]-lmn[0][0]*lmn[1][2]);
		lmn[2][2] = a * (lmn[0][0]*lmn[1][1]-lmn[0][1]*lmn[1][0]);
		
		float b = (float) (1 / 
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

		LMN = inverse(LMN);
		
		float[][] t=new float[3][3];

		t[0][0] = lmn[0][0] * LMN[0][0] + lmn[1][0] * LMN[0][1] + lmn[2][0] * LMN[0][2];
		t[0][1] = lmn[0][1] * LMN[0][0] + lmn[1][1] * LMN[0][1] + lmn[2][1] * LMN[0][2];
		t[0][2] = lmn[0][2] * LMN[0][0] + lmn[1][2] * LMN[0][1] + lmn[2][2] * LMN[0][2];
		
		t[1][0] = lmn[0][0] * LMN[1][0] + lmn[1][0] * LMN[1][1] + lmn[2][0] * LMN[1][2];
		t[1][1] = lmn[0][1] * LMN[1][0] + lmn[1][1] * LMN[1][1] + lmn[2][1] * LMN[1][2];
		t[1][2] = lmn[0][2] * LMN[1][0] + lmn[1][2] * LMN[1][1] + lmn[2][2] * LMN[1][2];
		
		t[2][0] = lmn[0][0] * LMN[2][0] + lmn[1][0] * LMN[2][1] + lmn[2][0] * LMN[2][2];
		t[2][1] = lmn[0][1] * LMN[2][0] + lmn[1][1] * LMN[2][1] + lmn[2][1] * LMN[2][2];
		t[2][2] = lmn[0][2] * LMN[2][0] + lmn[1][2] * LMN[2][1] + lmn[2][2] * LMN[2][2];
		
		System.out.println(lmn[0][0] + "\t" + lmn[1][0] + "\t" + lmn[2][0]);
		System.out.println(lmn[0][1] + "\t" + lmn[1][1] + "\t" + lmn[2][1]);
		System.out.println(lmn[0][2] + "\t" + lmn[1][2] + "\t" + lmn[2][2]);
		
		System.out.println();
		
		System.out.println(LMN[0][0] + "\t" + LMN[1][0] + "\t" + LMN[2][0]);
		System.out.println(LMN[0][1] + "\t" + LMN[1][1] + "\t" + LMN[2][1]);
		System.out.println(LMN[0][2] + "\t" + LMN[1][2] + "\t" + LMN[2][2]);
		
		System.out.println();
		
		System.out.println(t[0][0] + "\t" + t[1][0] + "\t" + t[2][0]);
		System.out.println(t[0][1] + "\t" + t[1][1] + "\t" + t[2][1]);
		System.out.println(t[0][2] + "\t" + t[1][2] + "\t" + t[2][2]);

        System.out.println();

        System.out.println(Math.sin(real1Ra));
	}

	private static float[][] inverse(float[][] m) {
		float det = m[0][0] * (m[1][1] * m[2][2] - m[2][1] * m[1][2]) -
	             m[0][1] * (m[1][0] * m[2][2] - m[1][2] * m[2][0]) +
	             m[0][2] * (m[1][0] * m[2][1] - m[1][1] * m[2][0]);

		float invdet = 1 / det;
	
		float[][] inv=new float[3][3];
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
