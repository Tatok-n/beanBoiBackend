package com.beanBoi.beanBoiBackend.beanBoiBackend;

import com.beanBoi.beanBoiBackend.beanBoiBackend.models.Bean;
import com.beanBoi.beanBoiBackend.beanBoiBackend.repositories.*;
import com.google.cloud.firestore.DocumentReference;
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
    @Autowired
    private BeanPurchaseRepository beanPurchaseRepository;
    @Autowired
    private GrinderRepository grinderRepository;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private BrewRepository brewRepository;
    @Autowired
    private UserRepository userRepository;

    @Test public void manualTests() {
       System.out.println(beanRepository.getBeanById("bean0"));
       System.out.println(beanPurchaseRepository.getBeanPurchaseById("beanPurchase0"));

       Bean newBean = new Bean();
       newBean.setId("bean0");
       newBean.setName("bean3");
       newBean.setAltitude(2000);
       newBean.setRoaster("Not faro");
       newBean.setPrice(69.99f);
       newBean.setTastingNotes("choco");
       newBean.setRoastDegree(2);

       beanRepository.updateDocumentField(beanRepository.getBeanById("bean0"),"Name","Tanzania peaberry");
       DocumentReference beanRef = beanRepository.saveDocument(newBean);
       newBean.setRoastDegree(192);
       beanRepository.updateDocument(newBean);
       beanRepository.updateDocumentField(newBean,"Name","Henry");
       System.out.println(grinderRepository.getGrinderById("grinder0"));
       System.out.println(recipeRepository.getRecipeById("recipe0").getDescription());
       System.out.println(brewRepository.getBrewById("brew0"));
       System.out.println(userRepository.getUserById("user0"));


    }
}
