package telescope;

public class Calc {

	public static void main(String[] args) {
		
		//http://www.geocities.jp/toshimi_taki/aim/aim.htm
		
		int time1 = 1023;
		float φ1 = 62.86517f;   //Telescope Ra
		float θ1 = -13.20578f; //Telescope Dec
		float α1 = 5.934422f;   //Real Ra
		float γ1 = 7.406222f; //Real Dec
		
		int time2 = 1023;
		float φ2 = 166.9501f;  //Telescope Ra
		float θ2 = 11.52625f;  //Telescope Dec
		float α2 = 22.97587f;  //Real Ra
		float γ2 = -29.53397f;  //Real Dec
		
		float [][] lmn = new float[3][3];
		float [][] LMN = new float[3][3];
		
		lmn[0][0] = (float) (Math.cos(θ1) * Math.cos(φ1));
		lmn[0][1] = (float) (Math.cos(θ1) * Math.sin(φ1));
		lmn[0][2] = (float) (Math.sin(θ1));
		
		LMN[0][0] = (float) (Math.cos(γ1) * Math.cos(α1));
		LMN[0][1] = (float) (Math.cos(γ1) * Math.sin(α1));
		LMN[0][2] = (float) (Math.sin(γ1));
		
		lmn[1][0] = (float) (Math.cos(θ2) * Math.cos(φ2));
		lmn[1][1] = (float) (Math.cos(θ2) * Math.sin(φ2));
		lmn[1][2] = (float) (Math.sin(θ2));
		
		LMN[1][0] = (float) (Math.cos(γ2) * Math.cos(α2));
		LMN[1][1] = (float) (Math.cos(γ2) * Math.sin(α2));
		LMN[1][2] = (float) (Math.sin(γ2));
		
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
