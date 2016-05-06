package telescope.database;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import telescope.Local;
import telescope.database.AstronomicalObject.Type;

public class AstronomyExporter {

    public static void main(String[] args) throws UnsupportedEncodingException, IOException {

        Astronomy astronomy = new Astronomy();
        System.out.println(astronomy.getAstronomicalObjects().size());

        // print(astronomy);

        exportStars(astronomy);
        exportDSOs(astronomy);
        // exportPlanets();

        DateTime day = new DateTime().withZone(DateTimeZone.UTC).withTimeAtStartOfDay();

        AstronomicalObject planet = astronomy.getPlanetPosition(Astronomy.MOON);
        System.out.println(planet.ra + " " + planet.dec + " " + planet.distance);

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
