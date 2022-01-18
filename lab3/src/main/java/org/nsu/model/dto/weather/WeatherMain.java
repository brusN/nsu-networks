package org.nsu.model.dto.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nsu.model.dto.DTO;

@Getter
@JsonRootName(value = "main")
@JsonIgnoreProperties (ignoreUnknown = true)
public class WeatherMain implements DTO {
    @JsonProperty("temp")
    private String temperature;
    @JsonProperty("temp_min")
    private String temperatureMin;
    @JsonProperty("temp_max")
    private String temperatureMax;
    @JsonProperty("feels_like")
    private String temperatureFeelsLike;
    private String pressure;
    private String humidity;
}
