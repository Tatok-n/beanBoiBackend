package com.beanBoi.beanBoiBackend.beanBoiBackend.core.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Document(collection="espresso_recipes")
public class EspressoRecipe extends Recipe {
    private List<Water> waterFlow;
}
