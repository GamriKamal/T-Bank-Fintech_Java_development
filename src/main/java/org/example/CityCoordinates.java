package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CityCoordinates {
    @JsonProperty("lat")
    private double lat;

    @JsonProperty("lon")
    private double lon;
}
