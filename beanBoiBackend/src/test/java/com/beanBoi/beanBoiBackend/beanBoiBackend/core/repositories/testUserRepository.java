package com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.User;
import com.google.cloud.firestore.DocumentReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        //setup
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
        Map<String,Object> savedBean = ((List<Map<String,Object>>) savedUser.get("beansOwned")).get(0);
        Map<String,Object> beansAvailable = ((List<Map<String,Object>>) savedUser.get("beansAvailable")).get(0);
        Map<String,Object> brews = ((List<Map<String,Object>>) savedUser.get("brews")).get(0);
        Map<String,Object> grinders = ((List<Map<String,Object>>) savedUser.get("grinders")).get(0);
        Map<String,Object> recipes = ((List<Map<String,Object>>) savedUser.get("recipes")).get(0);
        getTestUserMap().keySet().forEach(key ->
                {
                    if (key.equals("beansOwned")) {
                        assertEquals(getBeanMap().toString(),  savedBean.toString());
                    } else if (key.equals("beansAvailable")) {
                        assertEquals(getBeanPurchaseMap().toString(),  beansAvailable.toString());
                    }else if (key.equals("brews")) {
                        assertEquals(getTestBrewMap().toString(),  brews.toString());
                    }else if (key.equals("grinders")) {
                        assertEquals(getTestGrinderMap().toString(), grinders.toString());
                    }else if (key.equals("recipes")) {
                        assertEquals(getTestEspressoRecipeMap().toString(), recipes.toString());
                    }else {
                        Assertions.assertEquals(getTestUserMap().get(key).toString(), firestore.getDocument(userRepository.collectionName, id).get(key).toString());}
                }

        );
    }
}
