package com.beanBoi.beanBoiBackend.beanBoiBackend.core.models;

import lombok.Data;

@Data
public class Bean extends DocumentData {

        private String name;
        private String tastingNotes;
        private String roaster;
        private long roastDegree;
        private long altitude;
        private float price;
        private String uid;
}
