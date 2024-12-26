package com.beanBoi.beanBoiBackend.beanBoiBackend;

import com.beanBoi.beanBoiBackend.beanBoiBackend.repositories.BeanRepository;
import com.beanBoi.beanBoiBackend.beanBoiBackend.repositories.FirestoreImplementation;
import com.google.cloud.firestore.DocumentSnapshot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class manualTests {

    @Autowired
    private FirestoreImplementation firestoreImplementation;
    @Autowired
    private BeanRepository beanRepository;

    @Test public void manualTests() {
       System.out.println(beanRepository.getBeanById("bean0"));

    }
}
