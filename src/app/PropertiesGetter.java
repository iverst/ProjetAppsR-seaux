package app;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesGetter {
    public static String getProperty(String fileName, String property) {
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream(fileName)) {
            properties.load(inputStream);
        } catch (IOException io) {
            io.printStackTrace();
        }
        return properties.getProperty(property);
    }
}
