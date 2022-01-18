package org.nsu.model.dto.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.nsu.model.dto.DTO;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather implements DTO {
    private String name;
    @JsonProperty("main")
    private WeatherMain weatherMain;
    @JsonProperty("weather")
    private List<WeatherDescription> descriptions;
    @JsonProperty("wind")
    private Wind windInfo;
    @JsonProperty("clouds")
    private Clouds cloudsInfo;
}
