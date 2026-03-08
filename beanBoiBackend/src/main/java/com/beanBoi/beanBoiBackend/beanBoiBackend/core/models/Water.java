package com.beanBoi.beanBoiBackend.beanBoiBackend.core.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "water")
public class Water {
    private String name;
    private double water;
    private double time;
}
