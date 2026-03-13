package com.beanBoi.beanBoiBackend.beanBoiBackend.core.DTOs;

import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.time.LocalDate;

public record BeanPurchaseRequest(
        String name,
        String beanId,
                                  float pricePaid,
                                  float amountPurchased,
                                  LocalDate dateOfPurchase,
                                  LocalDate dateOfRoast
) implements Serializable {

}
