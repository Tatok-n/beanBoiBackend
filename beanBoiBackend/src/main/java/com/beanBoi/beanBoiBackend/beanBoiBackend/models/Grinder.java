package com.beanBoi.beanBoiBackend.beanBoiBackend.models;

import lombok.Data;

import java.util.List;

@Data
public class Grinder extends DocumentData {
    private String name;
    private List<String> grindSetting;
}
