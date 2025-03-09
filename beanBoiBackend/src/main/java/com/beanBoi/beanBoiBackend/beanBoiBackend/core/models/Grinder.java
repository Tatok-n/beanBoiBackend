package com.beanBoi.beanBoiBackend.beanBoiBackend.core.models;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Grinder extends DocumentData {
    private String name;
    private String uid;
    private List<String> grindSetting;
    private List<Map<String, Object>> grindSettingRequests;
}
