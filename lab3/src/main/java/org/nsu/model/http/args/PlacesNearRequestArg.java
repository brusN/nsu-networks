package org.nsu.model.http.args;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlacesNearRequestArg implements RequestArg {
    private final String latitude;
    private final String longitude;
    private final String radius;
}
