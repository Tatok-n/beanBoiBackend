package com.beanBoi.beanBoiBackend.beanBoiBackend.models;

import lombok.Data;

import java.util.List;

@Data
public class EspressoRecipe extends Recipe {
    private List<Water> waterFlow;
}
