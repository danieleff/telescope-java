package telescope;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Local {

    private static String LOCAL_EXAMPLE_FILENAME = "local-example.properties";

    private static String LOCAL_FILENAME = "local.properties";

    private static Properties props;

    public static String getOrThrow(String key) {
        load();

        if (!props.containsKey(key)) {
            throw new Error(LOCAL_FILENAME + " does not contain [" + key + "]");
        }

        return props.getProperty(key);
    }

    private static void load() {
        try {
            if (props == null) {
                if (!Files.exists(Paths.get(LOCAL_FILENAME))) {
                    Files.copy(Paths.get(LOCAL_EXAMPLE_FILENAME), Paths.get(LOCAL_FILENAME));
                }

                props = new Properties();
                props.load(new FileInputStream(LOCAL_FILENAME));
            }
        } catch (IOException e) {
            throw new Error("Could not load local property file", e);
        }
    }

}
