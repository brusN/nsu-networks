package org.nsu.model.http.args;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaceDescriptionRequestArg implements RequestArg {
    private final String xid;
}
