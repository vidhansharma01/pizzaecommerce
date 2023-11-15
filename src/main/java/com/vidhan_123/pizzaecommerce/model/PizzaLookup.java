package com.vidhan_123.pizzaecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PizzaLookup {
    private Double id;
    private String brand;
    private String name;
    private Double price;
    private Integer quantity;
    private List<Pizza> type;
}
