package org.nsu.model.dto.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import org.nsu.model.dto.DTO;

@Getter
@JsonRootName(value = "wind")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Wind implements DTO {
    private String speed;
    private String gust;
}
