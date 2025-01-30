package com.beanBoi.beanBoiBackend.beanBoiBackend;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.*;
import com.beanBoi.beanBoiBackend.beanBoiBackend.firestore.repositories.FirestoreImplementation;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class TestUtils {
    public String testCollection = "test data";
    public String userUID = "temp";
    public String testId = "TESTID";
    public Timestamp testStamp;
    public static List<String> usedCollections = new ArrayList<>();

    @Autowired
    public FirestoreImplementation firestore;

    @BeforeEach
    void setup() {
        testStamp = Timestamp.now();
    }
    public Bean getTestBean() {
        Bean bean = new Bean();
        bean.setName("TanzaniaPeaberry");
        bean.setAltitude(1200);
        bean.setPrice(12.5f);
        bean.setRoaster("Faro");
        bean.setActive(true);
        bean.setRoastDegree(5);
        bean.setTastingNotes("Good stuff");
        bean.setUid(userUID);
        bean.setId(testId);
        return bean;
    }

    public HashMap<String, Object> getBeanMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("Name", "TanzaniaPeaberry");
        map.put("uid", userUID);
        map.put("price", 12.5f);
        map.put("altitude", 1200L);
        map.put("Roaster", "Faro");
        map.put("isActive", true);
        map.put("roastDegree", 5L);
        map.put("tastingNotes", "Good stuff");
        map.put("id", testId);
        return map;
    }

    public BeanPurchase getTestBeanPurchase() {
        BeanPurchase beanPurchase = new BeanPurchase();
        beanPurchase.setBeansPurchased(getTestBean());
        beanPurchase.setAmountPurchased(300f);
        beanPurchase.setAmountRemaining(300f);
        beanPurchase.setUid(userUID);
        beanPurchase.setActive(true);
        beanPurchase.setId(testId);
        return beanPurchase;
    }

    public HashMap<String, Object> getBeanPurchaseMap() {
        HashMap<String, Object> beanPurchaseMap = new HashMap<>();
        beanPurchaseMap.put("amountPurchased", 300f);
        beanPurchaseMap.put("amountRemaining", 300f);
        beanPurchaseMap.put("beans", getBeanMap());
        beanPurchaseMap.put("uid", userUID);
        beanPurchaseMap.put("isActive", true);
        beanPurchaseMap.put("id", testId);
        return beanPurchaseMap;
    }

    public Grinder getTestGrinder() {
        Grinder grinder = new Grinder();
        grinder.setName("Baratza sette 30");
        grinder.setUid(userUID);
        grinder.setId(testId);
        grinder.setActive(true);
        grinder.setGrindSetting(List.of("1","2","3"));
        return grinder;
    }

    public Map<String, Object> getTestGrinderMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", userUID);
        map.put("settings", List.of("1","2","3"));
        map.put("id", testId);
        map.put("name", "Baratza sette 30");
        map.put("isActive", true);
        return map;
    }

    public Brew getTestBrew() {
        Brew brew = new Brew();
        brew.setId(testId);
        brew.setBrewType(BrewType.Espresso);
        brew.setDuration(30f);
        brew.setTemperature(100f);
        brew.setBrewDate(testStamp);
        brew.setActive(true);
        brew.setUid(userUID);
        brew.setGrindSetting("2A");
        brew.setNotes("Good stuff");
        brew.setDoseIn(20.0f);
        brew.setDoseOut(35.0f);
        brew.setCoffeeUsed(getTestBeanPurchase());
        brew.setGrinderUsed(getTestGrinder());
        return brew;
    }

    public Map<String, Object> getTestBrewMap() {
        Map<String, Object> brewMap = new HashMap<String, Object>();
        brewMap.put("createdAt",testStamp);
        brewMap.put("coffeeUsed",getBeanPurchaseMap());
        brewMap.put("grinderUsed",getTestGrinderMap());

        brewMap.put("doseIn",20.0f);
        brewMap.put("doseOut",35.0f);
        brewMap.put("temperature",100f);
        brewMap.put("duration",30f);

        brewMap.put("type", "Espresso");
        brewMap.put("isActive", true);
        brewMap.put("grindSetting", "2A");
        brewMap.put("notes", "Good stuff");
        brewMap.put("uid", userUID);
        brewMap.put("id", testId);
        return brewMap;
    }

    public EspressoRecipe getTestEspressoRecipe() {
        EspressoRecipe espressoRecipe = new EspressoRecipe();
        espressoRecipe.setName("THE recipie");
        espressoRecipe.setDescription("Good stuff");
        espressoRecipe.setId(testId);
        espressoRecipe.setUid(userUID);
        espressoRecipe.setActive(true);
        espressoRecipe.setRatio(2.0f);
        espressoRecipe.setTemperature(100f);
        espressoRecipe.setDuration(30f);
        List<Water> waterFlow = IntStream.range(0, 10).mapToObj(i -> {
            Water water = new Water();
            water.setWater(1.0f*i);
            water.setTime(2L);
            return water;
        }
        ).collect(Collectors.toList());
        espressoRecipe.setWaterFlow(waterFlow);
        return espressoRecipe;
    }

    public Map<String, Object> getTestEspressoRecipeMap() {
        Map<String, Object> espressoRecipeMap = new HashMap<>();
        espressoRecipeMap.put("name", "THE recipie");
        espressoRecipeMap.put("description", "Good stuff");
        espressoRecipeMap.put("id", testId);
        espressoRecipeMap.put("uid", userUID);
        espressoRecipeMap.put("isActive", true);
        espressoRecipeMap.put("ratio", 2.0f);
        espressoRecipeMap.put("temperature", 100f);
        espressoRecipeMap.put("duration", 30f);
        espressoRecipeMap.put("type", "Espresso");

        List<Map <String,Object>> waterMap = IntStream.range(0, 10).mapToObj(i -> {
            Map<String, Object> map = new HashMap<>();
            map.put("flow", 1.0f*i);
            map.put("time", 2L);
                return map;
            }).toList();
        espressoRecipeMap.put("water flow", waterMap);
        return espressoRecipeMap;

    }

    public User getTestUser() {
        User user = new User();
        user.setId(userUID);
        user.setName("My man");
        user.setActive(true);

        List<Brew> brewList = new ArrayList<>();
        brewList.add(getTestBrew());
        List<Recipe> espressoRecipeList = new ArrayList<>();
        espressoRecipeList.add(getTestEspressoRecipe());
        List<Grinder> grinderList = new ArrayList<>();
        grinderList.add(getTestGrinder());
        List<Bean> beanList = new ArrayList<>();
        beanList.add(getTestBean());
        List<BeanPurchase> beanPurchaseList = new ArrayList<>();
        beanPurchaseList.add(getTestBeanPurchase());

        user.setBrews(brewList);
        user.setGrinders(grinderList);
        user.setBeansAvailable(beanPurchaseList);
        user.setBeansOwned(beanList);
        user.setRecipies(espressoRecipeList);
        return user;
    }

    public Map<String, Object> getTestUserMap() {
        User user = getTestUser();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("displayName", user.getName());
        userMap.put("isActive", user.isActive());
        userMap.put("id", user.getId());

        List<Map<String, Object>> brews = List.of(getTestBrewMap());
        List<Map<String, Object>> grinders = List.of(getTestGrinderMap());
        List<Map<String, Object>> beanPurchases = List.of(getBeanPurchaseMap());
        List<Map<String, Object>> beans = List.of(getBeanMap());
        List<Map<String, Object>> recipes = List.of(getTestEspressoRecipeMap());

        userMap.put("brews", brews);
        userMap.put("grinders", grinders);
        userMap.put("beansOwned", beans);
        userMap.put("beansAvailable", beanPurchases);
        userMap.put("recipes", recipes);
        return userMap;
    }



    @AfterAll
    static void clearDb() {
        Firestore firestore = FirestoreClient.getFirestore();
        FirestoreImplementation firestoreImplementation = new FirestoreImplementation();
        firestoreImplementation.setFirestore(firestore);
        usedCollections.forEach(collection ->firestoreImplementation.deleteDocument(collection,"TESTID"));
    }




}
