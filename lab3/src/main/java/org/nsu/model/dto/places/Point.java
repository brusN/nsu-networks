package org.nsu.model.dto.places;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;

@Getter
public class Point {
    @JsonAlias("lat")
    private String lat;
    @JsonAlias({"lng", "lon"})
    private String lng;
}
