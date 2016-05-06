package telescope.database;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;

import telescope.Local;
import telescope.Util;
import telescope.database.AstronomicalObject.Type;
import eu.cloudmakers.astronometry.NOVAS;
import eu.cloudmakers.astronometry.NOVAS.CelestialObject;
import eu.cloudmakers.astronometry.NOVAS.DoubleRef;

public class AstronomyExporter {

    public static void main(String[] args) throws UnsupportedEncodingException, IOException {

        Astronomy astronomy = new Astronomy();
        System.out.println(astronomy.getAstronomicalObjects().size());

        // print(astronomy);

        exportStars(astronomy);
        exportDSOs(astronomy);

        DateTime day = new DateTime().withZone(DateTimeZone.UTC).withTimeAtStartOfDay();

        for (int i = 0; i < 100; i++) {
            double julian = DateTimeUtils.toJulianDay(day.getMillis());

            // System.out.println(julian);

            CelestialObject saturnus = new NOVAS.CelestialObject((short) 0, (short) 6, "Saturnus");
            NOVAS.DoubleRef ra = new DoubleRef();
            NOVAS.DoubleRef dec = new DoubleRef();
            NOVAS.DoubleRef distance = new DoubleRef();

            // NOVAS.PositionOnSurface pos = new NOVAS.PositionOnSurface(47.82,
            // 20.58, 116, 20, 1018);
            NOVAS.PositionOnSurface pos = new NOVAS.PositionOnSurface(47.82, 20.58, 0, 0, 0);

            NOVAS.appPlanet(julian, saturnus, 0, ra, dec, distance);
            // NOVAS.localPlanet(julian, saturnus, 69, pos, 0, ra, dec,
            // distance);

            NOVAS.topoPlanet(julian, saturnus, 69, pos, 0, ra, dec, distance);

            System.out.print(day + "\t");
            // System.out.print(ra.value+"\t");
            // System.out.print(dec.value+"\t");

            System.out.print(Util.raToString(ra.value) + "\t");
            System.out.print(Util.decToString(dec.value) + "\t");

            System.out.print(distance.value);
            System.out.println();

            day = day.plusDays(1);
        }
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
            if (obj.type == Type.STAR && starCount < 200) {
                append(starString, obj);
                starCount++;
            }
        }

        starString.append("};");

        Path arduinoDir = Paths.get(Local.getOrThrow("arduino.dir"));

        Files.write(arduinoDir.resolve("stars.h"),
                starString.toString().getBytes("ASCII"),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static void exportDSOs(Astronomy astronomy) throws UnsupportedEncodingException, IOException {

        int dsoCount = 0;

        StringBuilder dsoString = new StringBuilder("const AstronomicalObject dsos[] PROGMEM = {\n");

        for (AstronomicalObject obj : astronomy.getAstronomicalObjects()) {
            if (obj.type == Type.DSO && dsoCount < 1000) {
                append(dsoString, obj);
                dsoCount++;
            }
        }

        dsoString.append("};");

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
