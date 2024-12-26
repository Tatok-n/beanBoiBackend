package com.beanBoi.beanBoiBackend.beanBoiBackend.models;

import lombok.Data;

import java.util.List;

@Data
public class User {
    private String id;
    private String name;
    private List<Recipe> recipies;
    private List<Grinder> grinders;
    private List<Brew> brews;
    private List<Bean> beansOwned;
    private List<BeanPurchase> beansAvailable;
}
