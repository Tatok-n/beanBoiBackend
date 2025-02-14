package com.beanBoi.beanBoiBackend.beanBoiBackend;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Bean;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.services.BeanService;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.services.GrinderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
class manualTests extends TestUtils{

    @Autowired
    BeanService service;

    @Autowired
    GrinderService grinderService;

    @Test
    void test(){

        Map<String, Object> requestF = new HashMap<>();
        requestF.put("startNumerator",1.0f);
        requestF.put("endNumerator",2.0f);
        requestF.put("precision",0.1f);
        requestF.put("type","F");

        Map<String, Object> requestI = new HashMap<>();
        requestI.put("startNumerator",1);
        requestI.put("endNumerator",2);
        requestI.put("type","I");

        Map<String, Object> requestA = new HashMap<>();
        requestA.put("startLetter",'B');
        requestA.put("endLetter",'A');
        requestA.put("type","A");

        List<Map<String,Object>> requests = List.of(requestF, requestI, requestA);
        System.out.println(grinderService.getSettingList(requests));
    }





}
