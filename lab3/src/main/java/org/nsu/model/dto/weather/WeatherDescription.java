package org.nsu.model.dto.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import org.nsu.model.dto.DTO;

@Getter
@JsonRootName(value = "weather")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDescription implements DTO {
    private String main;
    private String description;
}
