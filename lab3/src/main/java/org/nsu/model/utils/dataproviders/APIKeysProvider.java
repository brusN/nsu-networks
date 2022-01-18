package org.nsu.model.utils.dataproviders;

import java.util.HashMap;
import java.util.Properties;

public enum APIKeysProvider {
    GRAPHHOPPER,
    OPENWEATHERMAP,
    OPENTRIPMAP;

    private static Properties apiKeys;
    private String apiKey;

    private void initAPIKey() {
        if (apiKeys == null) {
            apiKeys = PropertiesProvider.getProperties("apikeys.properties");
        }
        apiKey = (String)apiKeys.get("API_KEY_" + this);
    }

    public String getAPIKey() {
        if (apiKey == null) {
            initAPIKey();
        }
        return apiKey;
    }
}
