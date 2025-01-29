package com.beanBoi.beanBoiBackend.beanBoiBackend.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.models.BeanPurchase;
import com.beanBoi.beanBoiBackend.beanBoiBackend.models.User;
import com.google.cloud.firestore.DocumentReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class testUserRepository extends TestUtils {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GrinderRepository grinderRepository;
    @Autowired
    private BeanRepository beanRepository;
    @Autowired
    private BeanPurchaseRepository beanPurchaseRepository;
    @Autowired
    private BrewRepository brewRepository;
    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    public void testGetUser() {
        User testUser = getTestUser();
        DocumentReference documentReference = userRepository.saveDocument(testUser);

        //Execute
        User retrievedUser = userRepository.getUserById(documentReference.getId());
        testUser.setId(retrievedUser.getId());
        usedCollections.add(userRepository.collectionName);

        //Assert
        assertEquals(testUser, retrievedUser);
    }


    @Test void testSaveUser() {
//Setup
        User testUser = getTestUser();

        //Execute
        DocumentReference documentReference = userRepository.saveDocument(testUser);
        usedCollections.add(beanRepository.collectionName);
        usedCollections.add(grinderRepository.collectionName);
        usedCollections.add(beanPurchaseRepository.collectionName);
        usedCollections.add(brewRepository.collectionName);
        usedCollections.add(recipeRepository.collectionName);

        String id = documentReference.getId();

        //Assert
        Map<String,Object> savedUser = userRepository.getAsMap(userRepository.getUserById(id));
        getTestUserMap().keySet().forEach(key ->
                {
                    if (key.equals("beansOwned")) {
                        assertEquals((List.of(getBeanMap())).toString(),  savedUser.get(key).toString());
                    } else if (key.equals("beansAvailable")) {
                        assertEquals((List.of(getBeanPurchaseMap())).toString(),  (savedUser.get(key)).toString());
                    }else if (key.equals("brews")) {
                        assertEquals((List.of(getTestBrewMap())).toString(),  savedUser.get(key).toString());
                    }else if (key.equals("grinders")) {
                        assertEquals((List.of(getTestGrinderMap())).toString(),  savedUser.get(key).toString());
                    }else if (key.equals("recipes")) {
                        assertEquals((List.of(getTestEspressoRecipeMap())).toString(),  savedUser.get(key).toString());
                    }else {
                        assertEquals(getTestUserMap().get(key).toString(), firestore.getDocument(userRepository.collectionName, id).get(key).toString());}
                }

        );
    }
}
