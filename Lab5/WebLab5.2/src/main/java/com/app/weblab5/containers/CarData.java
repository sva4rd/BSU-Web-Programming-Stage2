package com.app.weblab5.containers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarData {
    private int id;
    private String model;
    private String manufacturer;
    private String state;

}
