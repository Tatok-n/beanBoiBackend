package com.beanBoi.beanBoiBackend.beanBoiBackend.core.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection="v60_recipes")
public class V60Recipe extends Recipe {
    private List<Water> waterAmount;

}
