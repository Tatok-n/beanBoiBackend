package com.beanBoi.beanBoiBackend.beanBoiBackend.core.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection="users")
public class User extends DocumentData {

    private String name;
    private List<Recipe> recipies;
    private List<Grinder> grinders;
    private List<Brew> brews;
    private List<Bean> beansOwned;
    private List<BeanPurchase> beansAvailable;
    private String email;
}
