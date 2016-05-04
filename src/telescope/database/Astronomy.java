package telescope.database;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import telescope.Util;
import telescope.database.AstronomicalObject.Type;
import eu.cloudmakers.astronometry.NOVAS;
import eu.cloudmakers.astronometry.NOVAS.CelestialObject;
import eu.cloudmakers.astronometry.NOVAS.DoubleRef;

public class Astronomy {

	private List<AstronomicalObject> astronomicalObjects = new ArrayList<>();
	
	public Astronomy() {
		load();
	}

	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		Astronomy astronomy = new Astronomy();
		System.out.println(astronomy.astronomicalObjects.size());
		
		//print(astronomy);
		
		//export(astronomy);
		
		DateTime day = new DateTime().withTimeAtStartOfDay();
		
		for(int i=0;i<100;i++) {
			double julian = DateTimeUtils.toJulianDay(day.getMillis());
			
			//System.out.println(julian);
			
			CelestialObject saturnus = new NOVAS.CelestialObject((short)0, (short)6, "Saturnus");
			NOVAS.DoubleRef ra=new DoubleRef();
			NOVAS.DoubleRef dec=new DoubleRef();
			NOVAS.DoubleRef distance=new DoubleRef();
			
			//NOVAS.PositionOnSurface pos = new NOVAS.PositionOnSurface(47.82, 20.58, 116, 20, 1018);
			NOVAS.PositionOnSurface pos = new NOVAS.PositionOnSurface(47.82, 20.58, 0, 0, 0);
			
			NOVAS.appPlanet(julian, saturnus, 0, ra, dec, distance);
			//NOVAS.localPlanet(julian, saturnus, 69, pos, 0, ra, dec, distance);
			
			NOVAS.topoPlanet(julian, saturnus, 69, pos, 0, ra, dec, distance);
			
			System.out.print(day+"\t");
			//System.out.print(ra.value+"\t");
			//System.out.print(dec.value+"\t");
			
			System.out.print(Util.raToString(ra.value)+"\t");
			System.out.print(Util.decToString(dec.value)+"\t");
			
			System.out.print(distance.value);
			System.out.println();
			
			day = day.plusDays(1);
		}
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

	private static void print(Astronomy astronomy) {
		int counter=0;
		
		for (AstronomicalObject object : astronomy.astronomicalObjects) {
			if (object.type == Type.DSO) {
				System.out.println(object.getName()+" "+object.name+" "+object.hip+" " +object.ra+" "+object.dec+" "+object.mag);
				counter++;
			}
			if (counter > 10) {
				break;
			}
		}
	}

	private static void export(Astronomy astronomy) throws UnsupportedEncodingException, IOException {
		
		int starCount=0;
		int dsoCount=0;
		
		StringBuilder starString = new StringBuilder("const AstronomicalObject stars[] PROGMEM = {\n");
		StringBuilder dsoString = new StringBuilder("const AstronomicalObject dsos[] PROGMEM = {\n");
		
		for (AstronomicalObject obj : astronomy.astronomicalObjects) {
			if (obj.type == Type.STAR && starCount < 200) {
				append(starString, obj);
				starCount++;
			}
			
			if (obj.type == Type.DSO && dsoCount < 1000) {
				append(dsoString, obj);
				dsoCount++;
			}
		}
		
		starString.append("};");
		dsoString.append("};");
		
		Files.write(Paths.get("D:", "projects", "arduino", "Telescope", "stars.h"), 
				starString.toString().getBytes("ASCII"), 
				StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		
		Files.write(Paths.get("D:", "projects", "arduino", "Telescope", "dsos.h"), 
				dsoString.toString().getBytes("ASCII"), 
				StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
	}

	private static void append(StringBuilder string, AstronomicalObject obj) {
		string.append("{");
		string.append(String.format("%12f", obj.ra));
		string.append(",");
		string.append(String.format("%12f", obj.dec));
		string.append(",");
		String name = obj.getName();
		for(int i=0;i<Math.min(9, name.length()); i++) {
			String sub = name.substring(i, i+1);
			if (sub.equals("'")) {
				sub="\\'";
			}
			string.append("'"+sub+"', ");
		}
		for(int i = Math.min(9, name.length()); i <= 9; i++) {
			string.append("  0, ");
		}
		
		string.append("},");
		
		string.append("// "+String.format("%12s %4.1f ", obj.getName(), obj.mag));
		if (obj.type == Type.DSO){
			if (obj.cat2!=null) {
				string.append(String.format("%12s %4s", obj.cat2, obj.id2));
			}
			string.append("["+obj.r1+"']");
			if (obj.r2!=null) {
				string.append(" ["+obj.r2+"']");
			}
		}
		
		string.append("\n");
	}
	
	private static Double parseDoubleOrNull(String string) {
		try {
			return Double.parseDouble(string);
		} catch (Exception e){
			return null;
		}
	}
	
	private static String parseStringOrNull(String string) {
		return string.trim().isEmpty()?null:string;
	}

	private static Integer parseIntOrNull(String string) {
		try {
			return Integer.parseInt(string);
		} catch (Exception e){
			return null;
		}
	}
	
	
}
