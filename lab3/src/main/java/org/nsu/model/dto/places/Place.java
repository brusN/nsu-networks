package org.nsu.model.dto.places;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.nsu.model.dto.DTO;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Place implements DTO {
    private Point point;
    private String name;
    private String country;
    private String city;
    private String state;
    private String osm_key;
    private String osm_value;

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (name != null) {
            builder.append("[Name]: ").append(name).append("\n");
        }
        if (country != null) {
            builder.append("[Country]: ").append(country).append("\n");
        }
        if (city != null) {
            builder.append("[City]: ").append(city).append("\n");
        }
        return builder.toString();
    }
}
