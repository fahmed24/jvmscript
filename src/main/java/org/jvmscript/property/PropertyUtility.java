package org.jvmscript.property;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtility {

    private static Properties properties;

    public static void openPropertyFileClassPath(String propertyFile) throws IOException {
        properties = new Properties();
        InputStream inputStream = PropertyUtility.class.getResourceAsStream("/" + propertyFile);
        properties.load(inputStream);
    }

    public static String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

}
