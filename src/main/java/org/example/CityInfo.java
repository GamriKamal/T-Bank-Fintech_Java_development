package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties
public class CityInfo {
    @JsonProperty("slug")
    private String slug;

    @JsonProperty("coords")
    private CityCoordinates cityCoordinates;

}

