package org.nsu.model.utils.dataproviders;
import java.util.Properties;

public enum URLProvider {
    GRAPHHOPPER_HOST,
    GRAPHHOPPER_PATH,
    OPENWEATHERMAP_HOST,
    OPENWEATHERMAP_PATH,
    OPENTRIPMAP_HOST,
    OPENTRIPMAP_PATH_1,
    OPENTRIPMAP_PATH_2;

    private static Properties urls;
    private String url;

    private void initURL() {
        if (urls == null) {
            urls = PropertiesProvider.getProperties("urls.properties");
        }
        url = (String)urls.get("URL_" + this);
    }

    public String getURL() {
        if (url == null) {
            initURL();
        }
        return url;
    }
}
