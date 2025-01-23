package com.beanBoi.beanBoiBackend.beanBoiBackend.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.models.*;
import com.google.cloud.firestore.DocumentReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class UserRepository extends DocumentRepository{
    @Autowired
    private FirestoreImplementation firestoreImplementation;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private NativeWebRequest nativeWebRequest;

    public UserRepository() {
        this.collectionName = "users";
    }

    @Autowired
    BrewRepository brewRepository;

    @Autowired
    GrinderRepository grinderRepository;

    @Autowired
    BeanPurchaseRepository beanPurchaseRepository;

    @Autowired
    BeanRepository beanRepository;

    @Override
    public Map<String, Object> getAsMap(DocumentData data) {
        User user = (User) data;
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("displayName", user.getName());
        userMap.put("isActive", user.isActive());
        userMap.put("id", user.getId());

        List<Map<String, Object>> brews = user.getBrews().stream().map(brew -> (brewRepository.getAsMap(brew))).toList();
        List<Map<String, Object>> grinders = user.getGrinders().stream().map(grinder -> (grinderRepository.getAsMap(grinder))).toList();
        List<Map<String, Object>> beanPurchases = user.getBeansAvailable().stream().map(purchase -> (beanPurchaseRepository.getAsMap(purchase))).toList();
        List<Map<String, Object>> beans = user.getBeansOwned().stream().map(bean -> (beanRepository.getAsMap(bean))).toList();
        List<Map<String, Object>> recipes = user.getRecipies().stream().map(recipe -> (recipeRepository.getAsMap(recipe))).toList();

        userMap.put("brews", brews);
        userMap.put("grinders", grinders);
        userMap.put("beansOwned", beans);
        userMap.put("beansAvailable", beanPurchases);
        userMap.put("recipes", recipes);

        return userMap;
    }

    @Override
    public DocumentData getFromMap(Map<String, Object> map) {
        User user = new User();
        user.setName((String) map.get("displayName"));
        user.setActive((boolean) map.get("isActive"));

        List<DocumentReference> grinderReferences = (List<DocumentReference>) map.get("grinders");
        List<DocumentReference> brewReferences = (List<DocumentReference>) map.get("brews");
        List<DocumentReference> beanPurchaseReferences = (List<DocumentReference>) map.get("beansAvailable");
        List<DocumentReference> beanReferences = (List<DocumentReference>) map.get("beansOwned");
        List<DocumentReference> recipeReferences = (List<DocumentReference>) map.get("recipes");

        List<Grinder> grinders = grinderReferences.stream().map(grinderRepository::verifyGrinder).toList();
        List<Brew> brews = brewReferences.stream().map(brewRepository::verifyBrew).toList();
        List<BeanPurchase> beansAvailable = beanPurchaseReferences.stream().map(beanPurchaseRepository::verifyPurchase).toList();
        List<Bean> beans = beanReferences.stream().map(beanRepository::verifyBean).toList();
        List<Recipe> recipes = recipeReferences.stream().map(recipeRepository::verifyRecipe).toList();

        user.setGrinders(grinders);
        user.setBrews(brews);
        user.setBeansAvailable(beansAvailable);
        user.setBeansOwned(beans);
        user.setRecipies(recipes);
        user.setId(map.get("id").toString());
        return user;
    }

    public DocumentReference saveDocument(DocumentData documentData) {
        User user = (User) documentData;
        if (user.getId() == null) {
            user.setId(getNewId());
        }
        List<DocumentReference> beansReferences = user.getBeansOwned().stream().map(bean -> beanRepository.saveDocument(bean)).toList();
        List<DocumentReference> beanPurchaseReference = user.getBeansAvailable().stream().map(beanPurchase -> beanPurchaseRepository.saveDocument(beanPurchase)).toList();
        List<DocumentReference> brewReferences = user.getRecipies().stream().map(brew   -> brewRepository.saveDocument(brew)).toList();
        List<DocumentReference> grinderReferences = user.getGrinders().stream().map(grinder -> grinderRepository.saveDocument(grinder)).toList();
        List<DocumentReference> recipieReferences = user.getRecipies().stream().map(recipe -> recipeRepository.saveDocument(recipe)).toList();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("beansOwned", beansReferences);
        userMap.put("beansAvailable", beanPurchaseReference);
        userMap.put("brews", brewReferences);
        userMap.put("grinders", grinderReferences);
        userMap.put("recipes", recipieReferences);
        DocumentReference savedUser = firestoreImplementation.addDocumentToCollectionWithId(collectionName, getAsMap(user),user.getId());

        return savedUser;
    }

    public User getUserById(String id) {
        return (User) getDocumentById(id);
    }



}
