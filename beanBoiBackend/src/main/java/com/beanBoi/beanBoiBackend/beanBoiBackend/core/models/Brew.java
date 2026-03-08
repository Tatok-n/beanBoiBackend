package com.beanBoi.beanBoiBackend.beanBoiBackend.core.models;


import com.mongodb.internal.connection.Time;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "brews")
public class Brew extends DocumentData {

    private String grindSetting;
    private String notes;
    private String uid;
    private BrewType brewType;

    private float duration;
    private float doseIn;
    private float doseOut;
    private float temperature;

    private Time brewDate;
    private Grinder grinderUsed;
    private BeanPurchase coffeeUsed;
}
