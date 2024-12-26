package com.beanBoi.beanBoiBackend.beanBoiBackend.models;

import lombok.Data;

import java.util.Date;

@Data
public class Brew {
    private String id;
    private String name;
    private String description;
    private String grindSetting;

    private BrewType brewType;

    private long duration;
    private long doseIn;
    private long doseOut;

    private Date brewDate;
    private Grinder grinderUsed;
    private BeanPurchase coffeeUsed;
}
