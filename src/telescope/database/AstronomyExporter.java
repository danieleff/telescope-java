package telescope.database;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import telescope.Local;
import telescope.Util;
import telescope.database.AstronomicalObject.Type;
import eu.cloudmakers.astronometry.Utils;

public class AstronomyExporter {

    public static void main(String[] args) throws UnsupportedEncodingException, IOException {

        Astronomy astronomy = new Astronomy();
        System.out.println(astronomy.getAstronomicalObjects().size());

        // print(astronomy);

        exportStars(astronomy);
        exportDSOs(astronomy);
        exportPlanets(astronomy);

        System.out.println(Utils.UTC2JD(new Date()));
        
        DateTime now = new DateTime().withZone(DateTimeZone.UTC).withTimeAtStartOfDay();
        
        int planetId = Astronomy.SATURN;
        
        AstronomicalObject start = astronomy.getPlanetPosition(now.toDate(), planetId);
        AstronomicalObject end = astronomy.getPlanetPosition(now.plusHours(24).toDate(), planetId);
        
        AstronomicalObject middle = astronomy.getPlanetPosition(now.plusHours(12).toDate(), planetId);

        float ra = (float) ((start.ra + end.ra) / 2);
        float dec = (float) ((start.dec + end.dec) / 2);
        float dist = (float) ((start.distance+ end.distance) / 2);
        
        System.out.println(now);
        System.out.println(now.plusHours(12));
        System.out.println(now.plusHours(24));
        
        System.out.println("Start     :" + Util.raToString(start.ra) + "\t" + Util.decToString(start.dec) + "\t" + start.distance);
        System.out.println("Start     :" + start.ra + "\t" + start.dec + "\t" + start.distance);
        
        System.out.println("End       :" + Util.raToString(end.ra) + "\t" + Util.decToString(end.dec) + "\t" + end.distance);
        System.out.println("End       :" + ra + "\t" + end.dec + "\t" + end.distance);
        
        System.out.println("Real      :" + Util.raToString(middle.ra) + "\t" + Util.decToString(middle.dec) + "\t" + middle.distance);
        System.out.println("Real      :" + middle.ra + "\t" + middle.dec + "\t" + middle.distance);
        System.out.println("Calculated:" + ra + "\t" + dec + "\t" + dist);
        
        System.out.println("Difference:" + (middle.ra - ra) + "\t" + (middle.dec - dec) + "\t" + (middle.distance - dist));
        
        //System.out.println(Util.raToString(planet.ra) + " " + Util.decToString(planet.dec));
        
        for(int i=0;i<10*24;i++) {
        	AstronomicalObject moon = astronomy.getPlanetPosition(now.plusHours(i).toDate(), Astronomy.MOON);
        	System.out.println(moon.dec);
        }
        System.out.println(Utils.JD_ORIGIN);
        
        AstronomicalObject x = astronomy.getPlanetPosition(Astronomy.MOON);
        System.out.println("Real      :" + x.ra + "\t" + x.dec + "\t" + x.distance);
        System.out.println("Real      :" + Util.raToString(x.ra) + "\t" + Util.decToString(x.dec) + "\t" + x.distance);
        
        System.out.println(Utils.UTC2JD(new DateTime().toDate()) - 2451545.0);
        
        {
	        int ut1_2000_seconds = (int) (new DateTime(2016, 5, 1, 0, 0, 0).getMillis() / 1000 - 946728000);
	        System.out.println("ut1_2000_seconds: "+ut1_2000_seconds);
	        
	        //float ut1_days = (float) ;
	        //System.out.println("ut1_days: "+ut1_days);
	        
	        //double greenwich_mean_sideral_time = 18.697374558 + 24.06570982441908 * (((ut1_2000_seconds / 24.0) / 60.0) / 60);
	        double greenwich_mean_sideral_time_2016_may = (double) (18.697374558 + 0.00027853830815299861 * ut1_2000_seconds);
	        
	        double may_delta = greenwich_mean_sideral_time_2016_may % 24;
	        
			System.out.println(may_delta);
	        System.out.println(Util.raToString(may_delta));
	        
        	System.out.println("Now:" + new DateTime().getMillis() / 1000);
        	int ut1_2000_seconds_minus_2016 = (int) (new DateTime().getMillis() / 1000 - 946728000 - ut1_2000_seconds);
	        System.out.println("ut1_2000_seconds: "+ut1_2000_seconds_minus_2016);
	        
	        //float ut1_days = (float) ;
	        //System.out.println("ut1_days: "+ut1_days);
	        
	        //double greenwich_mean_sideral_time = 18.697374558 + 24.06570982441908 * (((ut1_2000_seconds / 24.0) / 60.0) / 60);
	        float greenwich_mean_sideral_time = (float) (0.00027853830815299861 * ut1_2000_seconds_minus_2016);
	        greenwich_mean_sideral_time += may_delta;
	        System.out.println(greenwich_mean_sideral_time);
	        System.out.println(greenwich_mean_sideral_time % 24);
	        System.out.println(Util.raToString((double)greenwich_mean_sideral_time % 24));
        }
        
    }

    private static void exportPlanets(Astronomy astronomy) throws IOException {
		// TODO Auto-generated method stub
    	List<AstronomicalObject> planets=new ArrayList<AstronomicalObject>();
    	
    	AstronomicalObject moon = astronomy.getPlanetPosition(Astronomy.MOON);
    	moon.name = "Moon";
    	planets.add(moon);
    	
    	AstronomicalObject jupiter = astronomy.getPlanetPosition(Astronomy.JUPITER);
    	jupiter.name = "Jupiter";    	
    	planets.add(jupiter);
    	
    	AstronomicalObject saturn = astronomy.getPlanetPosition(Astronomy.SATURN);
    	saturn.name = "Saturn";    	
    	planets.add(saturn);
    	
    	AstronomicalObject mars = astronomy.getPlanetPosition(Astronomy.MARS);
    	mars.name = "Mars";    	
    	planets.add(mars);
    	
    	
        StringBuilder starString = new StringBuilder("const AstronomicalObject planets[] PROGMEM = {\n");
		for (AstronomicalObject planet : planets) {
			append(starString, planet);			
		}
        starString.append("};\n");
        
        
        starString.append("const AstronomicalObjectTime planetsTime[] PROGMEM = {\n");
        
        String meta = "";
        int timesSum = 0;
        
        DateTime start = new DateTime()./*withZone(DateTimeZone.UTC).*/withTimeAtStartOfDay();
        
        for (AstronomicalObject planet : planets) {
        	
        	int times = 30;
        	int deltaHours = 24;
        	if (planet == moon) {
        		deltaHours = 1;
        		times = 30 * 24;
        	}
        	
        	meta += times + ", " + deltaHours + ", " + timesSum + ", ";
        	timesSum += times;
        	
        	starString.append("//" + planet.name + "\n");
        	for(int i=0; i<times; i++) {
        		DateTime calculatedTime = start.plusHours(deltaHours * i);
        		
                AstronomicalObject planetTime = astronomy.getPlanetPosition(calculatedTime.toDate(), planet.hip);
                starString.append("    {"+planetTime.ra+", "+planetTime.dec+"},\n");        		
        	}
        	   	
        }
        starString.append("};\n");
        
        starString.append("const short planetMeta[] = {" + meta + "};\n");
        
        starString.append("const float planetStartJulian = " + Utils.UTC2JD(start.toDate()) + ";\n");
        
        starString.append("const long planetStartSeconds = " + start.getMillis() / 1000 + ";\n");
        
        
        Path arduinoDir = Paths.get(Local.getOrThrow("arduino.dir"));

        Files.write(arduinoDir.resolve("planets.h"),
                starString.toString().getBytes("ASCII"),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
	}

	private static void print(Astronomy astronomy) {
        int counter = 0;

        for (AstronomicalObject object : astronomy.getAstronomicalObjects()) {
            if (object.type == Type.DSO) {
                System.out.println(object.getName() + " " + object.name + " " + object.hip + " " + object.ra + " "
                        + object.dec + " " + object.mag);
                counter++;
            }
            if (counter > 10) {
                break;
            }
        }
    }

    private static void exportStars(Astronomy astronomy) throws UnsupportedEncodingException, IOException {

        int starCount = 0;

        StringBuilder starString = new StringBuilder("const AstronomicalObject stars[] PROGMEM = {\n");

        for (AstronomicalObject obj : astronomy.getAstronomicalObjects()) {
            if (obj.type == Type.STAR && starCount < 100) {
                append(starString, obj);
                starCount++;
            }
        }

        starString.append("};\n");

        Path arduinoDir = Paths.get(Local.getOrThrow("arduino.dir"));

        Files.write(arduinoDir.resolve("stars.h"),
                starString.toString().getBytes("ASCII"),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static void exportDSOs(Astronomy astronomy) throws UnsupportedEncodingException, IOException {

        int dsoCount = 0;

        StringBuilder dsoString = new StringBuilder("const AstronomicalObject dsos[] PROGMEM = {\n");

        for (AstronomicalObject obj : astronomy.getAstronomicalObjects()) {
            if (obj.type == Type.DSO && dsoCount < 100) {
                append(dsoString, obj);
                dsoCount++;
            }
        }

        dsoString.append("};\n");

        Path arduinoDir = Paths.get(Local.getOrThrow("arduino.dir"));

        Files.write(arduinoDir.resolve("dsos.h"),
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
        for (int i = 0; i < Math.min(9, name.length()); i++) {
            String sub = name.substring(i, i + 1);
            if (sub.equals("'")) {
                sub = "\\'";
            }
            string.append("'" + sub + "', ");
        }
        for (int i = Math.min(9, name.length()); i <= 9; i++) {
            string.append("  0, ");
        }

        string.append("},");

        string.append("// " + String.format("%12s %4.1f ", obj.getName(), obj.mag));
        if (obj.type == Type.DSO) {
            if (obj.cat2 != null) {
                string.append(String.format("%12s %4s", obj.cat2, obj.id2));
            }
            string.append("[" + obj.r1 + "']");
            if (obj.r2 != null) {
                string.append(" [" + obj.r2 + "']");
            }
        }

        string.append("\n");
    }

}
