package telescope.database;

import telescope.coordinate.Declination;
import telescope.coordinate.RightAscension;


public class AstronomicalObject implements Comparable<AstronomicalObject>{

	public Type type;
	
	public Integer hip; // The star's ID in the Hipparcos catalog, if known.
	public String hd; // The star's ID in the Henry Draper catalog, if known.
	public String hr; //The star's ID in the Harvard Revised catalog, which is the same as its number in the Yale Bright Star Catalog.
	public String gl; //The star's ID in the third edition of the Gliese Catalog of Nearby Stars.
	public String bf; //The Bayer / Flamsteed designation
	
	public String name;
	
	public RightAscension ra; // Right ascension - in 2000 
	
	public Declination dec;// Declination - in 2000
	
	public Double distance; //The star's distance in parsecs
	
	public Double pmra; //The star's proper motion in right ascension, in milliarcseconds per year.  
	public Double pmdec; //The star's proper motion in declination, in milliarcseconds per year.
	
	public Double rv; // The star's radial velocity in km/sec, where known.
	
	public Double mag; // The star's apparent visual magnitude.
	public Double absmag; // The star's absolute visual magnitude.
	
	public String bayer; // The Bayer designation as a distinct value
	public Integer flam; // The Flamsteed designation as a distinct value
	public String con;// Constellation
	
	public Double r1; //DSO: Semi-major axis of the object, in arcminutes.
	public Double r2;//DSO: Semi-minor axis of the object, in arcminutes. If r2 is undefined, r1 is interpreted as the object's radius.

	public Double angle;//DSO: Position angle of the semimajor axis of the object, in degrees
	
	public String id1;// DSO: Primary (most commonly used) ID number/designation and catalog name for this object.
	public String cat1;
	
	public String id2;// DSO: Primary (most commonly used) ID number/designation and catalog name for this object.
	public String cat2;
	
	public String getName() {
		
		
		if (name!=null) {
			return name;
		}
		if (bayer!=null) {
			return bayer+" "+con;
		}
		if (flam!=null) {
			return con+" "+flam;
		}
		if (hip!=null) {
			return "HIP"+hip;
		}
		if (hd!=null) {
			return "HD:"+hd;
		}
		if (hr!=null) {
			return "HR:"+hr;
		}
		if (gl!=null) {
			return "GL:"+gl;
		}
		if (cat1 != null) {
			return cat1 + " " + id1;
		}
		if (cat2 != null) {
			return cat2 + " " + id2;
		}
		
		return "BF:"+bf;
	}
	

	@Override
	public int compareTo(AstronomicalObject o) {
		if (mag == null && o.mag == null) {
			return 0;
		} else if (mag == null) {
			return 1;
		} else if (o.mag == null) {
			return -1;
		}
		return Double.compare(mag, o.mag);
	}
	
	public static enum Type {
		SUN,
		PLANET,
		STAR,
		DSO
		/*
		GALAXY,
		NEBULA,
		PLANETARY_NEBULA,
		GLOBULAR_CLUSTER,
		*/
	}
	
}
