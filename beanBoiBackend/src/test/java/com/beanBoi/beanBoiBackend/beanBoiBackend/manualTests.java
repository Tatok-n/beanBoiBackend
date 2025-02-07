package com.beanBoi.beanBoiBackend.beanBoiBackend;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Bean;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.services.BeanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;


@SpringBootTest
class manualTests extends TestUtils{

    @Autowired
    BeanService service;

    @Test
    void test(){
        Bean bean = getTestBean();
        try {
            service.addBeanToUser(bean,"user0");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }





}
