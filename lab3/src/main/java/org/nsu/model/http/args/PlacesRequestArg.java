package org.nsu.model.http.args;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlacesRequestArg implements RequestArg {
    String name;
}
