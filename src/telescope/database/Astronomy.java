package telescope.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import telescope.database.AstronomicalObject.Type;
import eu.cloudmakers.astronometry.NOVAS;
import eu.cloudmakers.astronometry.NOVAS.CelestialObject;
import eu.cloudmakers.astronometry.NOVAS.DoubleRef;
import eu.cloudmakers.astronometry.NOVAS.PositionOnSurface;
import eu.cloudmakers.astronometry.Utils;

public class Astronomy {

    public static final int MERCURY = 1;
    public static final int VENUS = 2;
    public static final int EARTH = 3;
    public static final int MARS = 4;
    public static final int JUPITER = 5;
    public static final int SATURN = 6;
    public static final int URANUS = 7;
    public static final int NEPTUNE = 8;
    public static final int PLUTO = 9;
    public static final int SUN = 10;
    public static final int MOON = 11;

    private List<AstronomicalObject> astronomicalObjects = new ArrayList<>();

    private PositionOnSurface positionOnSurface;

	public Astronomy() {
		load();

        positionOnSurface = new NOVAS.PositionOnSurface(47.814381, 20.584497, 42, 20, 1000);
	}

    public void setPosition(double lat, double lon, double alt, double temp, double pressure) {

        positionOnSurface = new NOVAS.PositionOnSurface(lat, lon, alt, temp, pressure);
    }

    public List<AstronomicalObject> getAstronomicalObjects() {
        return astronomicalObjects;
    }

    public AstronomicalObject getPlanetPosition(int planet) {
        return getPlanetPosition(new Date(), planet);
    }

    public AstronomicalObject getPlanetPosition(Date date, int planetId) {
        double julian = Utils.UTC2JD(date);

        CelestialObject planet = new NOVAS.CelestialObject((short) 0, (short) planetId, "");

        NOVAS.DoubleRef ra = new DoubleRef();
        NOVAS.DoubleRef dec = new DoubleRef();
        NOVAS.DoubleRef distance = new DoubleRef();

        NOVAS.topoPlanet(julian, planet, Utils.DELTA_T, positionOnSurface, 0, ra, dec, distance);

        AstronomicalObject ret = new AstronomicalObject();
        ret.hip = planetId;
        ret.ra = ra.value;
        ret.dec = dec.value;
        ret.distance = distance.value;
        return ret;
    }

    private void load() {
		try {
			List<String> csvRows = Files.readAllLines(Paths.get("hygdata_v3.csv"));
			for (String csvRow : csvRows) {
				if (csvRow.startsWith("id,")) {
					continue;
				}
				if (csvRow.contains(",Sol,")) {
					continue;
				}
				
				String[] split = csvRow.split(",");
				
				AstronomicalObject object = new AstronomicalObject();
				object.type = Type.STAR;
				object.hip = parseIntOrNull(split[1]);
				object.hd = parseStringOrNull(split[2]);
				object.hr = parseStringOrNull(split[3]);
				object.gl = parseStringOrNull(split[4]);
				object.bf = parseStringOrNull(split[5]);
				
				object.name = parseStringOrNull(split[6]);
				
				object.ra = parseDoubleOrNull(split[7]);
				object.dec = parseDoubleOrNull(split[8]);
				
				object.distance= parseDoubleOrNull(split[9]);
				
				object.pmra = parseDoubleOrNull(split[10]);
				object.pmdec = parseDoubleOrNull(split[11]);
				
				object.rv = parseDoubleOrNull(split[12]);
				
				object.mag = parseDoubleOrNull(split[13]);
				object.absmag = parseDoubleOrNull(split[14]);
				
				object.bayer = parseStringOrNull(split[27]);
				object.flam = parseIntOrNull(split[28]);
				object.con = parseStringOrNull(split[29]);
				
				astronomicalObjects.add(object);
			}
			
			
			csvRows = Files.readAllLines(Paths.get("dso.csv"));
			for (String csvRow : csvRows) {
				if (csvRow.startsWith("ra,")) {
					continue;
				}
				String[] split = csvRow.split(",");

				AstronomicalObject object = new AstronomicalObject();
				
				object.ra = parseDoubleOrNull(split[0]);
				object.dec = parseDoubleOrNull(split[1]);
				
				object.type = Type.DSO; //TODO split[2]
				
				object.con = parseStringOrNull(split[3]);
				object.mag = parseDoubleOrNull(split[4]);
				object.name = parseStringOrNull(split[5]);

				object.r1 = parseDoubleOrNull(split[9]);
				object.r2 = parseDoubleOrNull(split[10]);
				object.angle = parseDoubleOrNull(split[11]);
				
				object.id1 = split[13];
				object.cat1 = split[14];
				if (split.length>15) {
					object.id2 = split[15];
					object.cat2 = split[16];
				}
				
				astronomicalObjects.add(object);
			}
		} catch (IOException e) {
			throw new Error(e);
		}
		
		Collections.sort(astronomicalObjects);
	}

    private static Double parseDoubleOrNull(String string) {
        try {
            return Double.parseDouble(string);
        } catch (Exception e) {
            return null;
        }
    }

    private static String parseStringOrNull(String string) {
        return string.trim().isEmpty() ? null : string;
    }

    private static Integer parseIntOrNull(String string) {
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {
            return null;
        }
    }

	
}
