package com.beanBoi.beanBoiBackend.beanBoiBackend.core.models;

import lombok.Data;

import java.util.List;

@Data
public class Grinder extends DocumentData {
    private String name;
    private String uid;
    private List<String> grindSetting;
}
