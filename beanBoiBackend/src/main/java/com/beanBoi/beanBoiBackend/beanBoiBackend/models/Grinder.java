package com.beanBoi.beanBoiBackend.beanBoiBackend.models;

import lombok.Data;

import java.util.List;

@Data
public class Grinder {
    private String id;
    private String name;
    private List<String> grindSetting;
}
