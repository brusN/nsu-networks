package org.nsu.model.utils.dataproviders;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class PropertiesProvider {
    public static Properties getProperties(String propertiesName) {
        Properties properties = new Properties();
        try {
            properties.load(ImageProvider.class.getClassLoader().getResourceAsStream("properties/" + propertiesName));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return properties;
    }
}
