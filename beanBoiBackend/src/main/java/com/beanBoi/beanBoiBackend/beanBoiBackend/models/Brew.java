package com.beanBoi.beanBoiBackend.beanBoiBackend.models;

import com.google.cloud.Timestamp;
import lombok.Data;

import java.util.Date;

@Data
public class Brew extends DocumentData {

    private String grindSetting;
    private String notes;
    private String uid;
    private BrewType brewType;

    private float duration;
    private float doseIn;
    private float doseOut;
    private float temperature;

    private Timestamp brewDate;
    private Grinder grinderUsed;
    private BeanPurchase coffeeUsed;
}
