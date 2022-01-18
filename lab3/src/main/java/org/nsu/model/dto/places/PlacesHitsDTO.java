package org.nsu.model.dto.places;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.nsu.model.dto.DTO;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlacesHitsDTO implements DTO {
    @JsonProperty("hits")
    private List<Place> hits;
}
