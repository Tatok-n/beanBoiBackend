package com.beanBoi.beanBoiBackend.beanBoiBackend.core.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection= "bean_purchases")
public class BeanPurchase extends DocumentData {

    String name;
    private float amountRemaining;
    private float amountPurchased;
    private Bean beansPurchased;
    private String uid;
    private LocalDate purchaseDate;
    private LocalDate roastDate;
    private float pricePaid;
}
