package com.beanBoi.beanBoiBackend.beanBoiBackend.core.models;

import lombok.Data;

import java.util.List;

@Data
public class V60Recipe extends Recipe {
    private List<Water> waterAmount;

}
