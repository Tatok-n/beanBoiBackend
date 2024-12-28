package com.beanBoi.beanBoiBackend.beanBoiBackend.models;

import lombok.Data;

@Data
public class Recipe extends DocumentData{
    private String name;
    private String description;
    private float temperature;
    private float duration;
    private float ratio;
}
