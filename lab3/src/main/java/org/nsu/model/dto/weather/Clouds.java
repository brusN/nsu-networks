package org.nsu.model.dto.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import org.nsu.model.dto.DTO;

@Getter
@JsonRootName(value = "clouds")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Clouds implements DTO {
    @JsonProperty("all")
    private String cloudiness;
}
