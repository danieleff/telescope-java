package telescope;

public class Calc {

	public static void main(String[] args) {
		
		//http://www.geocities.jp/toshimi_taki/aim/aim.htm
		
		int time1 = 1023;
        float telescope1Ra = 40.5f;
        float telescope1Dec = 341.1f;
        float real1Ra = 5.25f * 15;
        float real1Dec = 46f;
        //float real1Ra = 40.5f;
        //float real1Dec = 341.1f;

		int time2 = 1023;
        float telescope2Ra = 73.25f;
        float telescope2Dec = 175.6f;
        float real2Ra = 6.75f * 15;
        float real2Dec = -16.75f;
        //float real2Ra = 73.25f;
        //float real2Dec = 175.6f;

        telescope1Ra = (float) Math.toRadians(telescope1Ra);
        telescope1Dec = (float) Math.toRadians(telescope1Dec);
        real1Ra = (float) Math.toRadians(real1Ra);
        real1Dec = (float) Math.toRadians(real1Dec);

        telescope2Ra = (float) Math.toRadians(telescope2Ra);
        telescope2Dec = (float) Math.toRadians(telescope2Dec);
        real2Ra = (float) Math.toRadians(real2Ra);
        real2Dec = (float) Math.toRadians(real2Dec);

        //System.out.println(Math.cos(real1Dec) * Math.cos(real1Ra));
        //System.out.println(Math.sin(telescope1Ra));
		
        //System.out.println(Math.cos(real2Dec) * Math.cos(real2Ra));
        //System.out.println(Math.sin(telescope2Ra));
		
		float [][] lmn = lmn(telescope1Ra, telescope1Dec, telescope2Ra, telescope2Dec);
		float [][] LMN = lmn(real1Ra, real1Dec, real2Ra, real2Dec);
		
		LMN = inverse(LMN);
		
		float[][] t=new float[3][3];

		for(int i=0;i<=2;i++) {
			for(int j=0;j<=2;j++) {
				t[i][j] = lmn[0][j] * LMN[i][0] + lmn[1][j] * LMN[i][1] + lmn[2][j] * LMN[i][2];
			}
		}
		
		System.out.println("lmn");
		
		System.out.println(lmn[0][0] + "\t" + lmn[1][0] + "\t" + lmn[2][0]);
		System.out.println(lmn[0][1] + "\t" + lmn[1][1] + "\t" + lmn[2][1]);
		System.out.println(lmn[0][2] + "\t" + lmn[1][2] + "\t" + lmn[2][2]);
		
		System.out.println("LMN");
		
		System.out.println(LMN[0][0] + "\t" + LMN[1][0] + "\t" + LMN[2][0]);
		System.out.println(LMN[0][1] + "\t" + LMN[1][1] + "\t" + LMN[2][1]);
		System.out.println(LMN[0][2] + "\t" + LMN[1][2] + "\t" + LMN[2][2]);
		
		System.out.println("T");
		
		System.out.println(t[0][0] + "\t" + t[1][0] + "\t" + t[2][0]);
		System.out.println(t[0][1] + "\t" + t[1][1] + "\t" + t[2][1]);
		System.out.println(t[0][2] + "\t" + t[1][2] + "\t" + t[2][2]);

		t = inverse(t);
		System.out.println("Inverse T");
		System.out.println(t[0][0] + "\t" + t[1][0] + "\t" + t[2][0]);
		System.out.println(t[0][1] + "\t" + t[1][1] + "\t" + t[2][1]);
		System.out.println(t[0][2] + "\t" + t[1][2] + "\t" + t[2][2]);
		
        System.out.println();

        System.out.println(Math.sin(telescope1Dec));
        
        float L = (float) (Math.cos(real1Dec) * Math.cos(real1Ra));
        float M = (float) (Math.cos(real1Dec) * Math.sin(real1Ra));
        float N = (float) (Math.sin(real1Dec));
        
        float x = t[0][2] * L + t[1][2] * M + t[2][2] * N;
        
        System.out.println(x);
	}
	
	private static float[][] lmn(float ra1, float dec1, float ra2, float dec2) {
		float [][] LMN = new float[3][3];
		
        LMN[0][0] = (float) (Math.cos(dec1) * Math.cos(ra1));
        LMN[0][1] = (float) (Math.cos(dec1) * Math.sin(ra1));
        LMN[0][2] = (float) (Math.sin(dec1));
		
        LMN[1][0] = (float) (Math.cos(dec2) * Math.cos(ra2));
        LMN[1][1] = (float) (Math.cos(dec2) * Math.sin(ra2));
        LMN[1][2] = (float) (Math.sin(dec2));
		
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
		return LMN;
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
