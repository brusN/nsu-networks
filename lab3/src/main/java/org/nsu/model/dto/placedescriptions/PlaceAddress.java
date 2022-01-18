package org.nsu.model.dto.placedescriptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import org.nsu.model.dto.DTO;

@Getter
@JsonRootName("address")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceAddress {
    private String country;
    private String state;
    private String city;
    @JsonProperty("city_district")
    private String cityDistrict;
    private String suburb;
    private String road;
    private String house;
    @JsonProperty("house_number")
    private String houseNumber;

}
