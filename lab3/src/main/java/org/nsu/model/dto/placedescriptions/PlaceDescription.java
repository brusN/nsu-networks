package org.nsu.model.dto.placedescriptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.nsu.model.dto.DTO;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceDescription implements DTO {

    private String kinds;
    @JsonProperty("image")
    private String imageUrl;
    private String rate;
    @JsonProperty("info")
    private PlaceInfo info;
    @JsonProperty("address")
    private PlaceAddress address;
}
