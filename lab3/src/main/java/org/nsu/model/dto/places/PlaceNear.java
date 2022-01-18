package org.nsu.model.dto.places;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.nsu.model.dto.DTO;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceNear implements DTO {
    private String name;
    private String xid;
    private String kinds;
    private String wikidata;
    private String dist;
    private Point point;

    public PlaceNear() {

    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (name != null) {
            builder.append("[Name]: ").append(name).append("\n");
        }
        if (kinds != null) {
            builder.append("[Kinds]: ").append(kinds).append("\n");
        }
        if (dist != null) {
            builder.append("[Distance]: ").append(dist).append("\n");
        }

        return builder.toString();
    }
}
