package com.beanBoi.beanBoiBackend.beanBoiBackend.core.models;

import lombok.Data;

@Data
public class Recipe extends DocumentData{
    private String name;
    private String uid;
    private String description;
    private double temperature;
    private double duration;
    private double ratio;
}
