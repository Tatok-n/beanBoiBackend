package com.beanBoi.beanBoiBackend.beanBoiBackend.core.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "beans")
public class Bean extends DocumentData {

        private String name;
        private String tastingNotes;
        private String roaster;
        private String process;
        private String origin;
        private long roastDegree;
        private long altitude;
        private float price;
        private int timesPurchased;
        private String uid;
}
