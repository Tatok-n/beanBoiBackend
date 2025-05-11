package com.beanBoi.beanBoiBackend.beanBoiBackend.core.models;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class EspressoRecipe extends Recipe {
    private List<Water> waterFlow;
}
