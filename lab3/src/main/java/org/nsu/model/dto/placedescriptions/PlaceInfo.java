package org.nsu.model.dto.placedescriptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;

@Getter
@JsonRootName("info")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceInfo {
    private String descr;
    @JsonProperty("image_width")
    private String imageWidth;
    @JsonProperty("image_height")
    private String imageHeight;
}
