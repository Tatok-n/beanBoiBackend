package com.beanBoi.beanBoiBackend.beanBoiBackend.core.models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BeanPurchase extends DocumentData {

    String name;
    private float amountRemaining;
    private float amountPurchased;
    private Bean beansPurchased;
    private String uid;
    private LocalDate purchaseDate;
    private LocalDate roastDate;
}
