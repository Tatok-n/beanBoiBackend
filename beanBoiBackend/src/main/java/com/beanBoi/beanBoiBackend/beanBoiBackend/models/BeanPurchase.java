package com.beanBoi.beanBoiBackend.beanBoiBackend.models;

import lombok.Data;

@Data
public class BeanPurchase extends DocumentData {

    private float amountRemaining;
    private float amountPurchased;
    private Bean beansPurchased;
    private String uid;
}
