package com.beanBoi.beanBoiBackend.beanBoiBackend.core.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

public class DocumentData {
    @Getter @Setter @Id
    private String id;
    @Getter @Setter
    private boolean isActive;





}
