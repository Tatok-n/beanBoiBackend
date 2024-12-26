package com.beanBoi.beanBoiBackend.beanBoiBackend.models;

import lombok.Data;

@Data
public class Bean {
        private String id;
        private String name;
        private String tastingNotes;
        private String roaster;
        private int roastDegree;
        private long altitude;
        private long price;
}
