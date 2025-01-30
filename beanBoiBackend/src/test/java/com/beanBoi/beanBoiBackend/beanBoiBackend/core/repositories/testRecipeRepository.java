package com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.TestUtils;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Recipe;
import com.google.cloud.firestore.DocumentReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
public class testRecipeRepository extends TestUtils {
    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    public void testSaveRecipe() {
        //Setup
        Recipe recipe = getTestEspressoRecipe();

        //Execute
        DocumentReference documentReference = recipeRepository.saveDocument(recipe);
        String id = documentReference.getId();

        //Assert
        getTestEspressoRecipeMap().keySet().forEach(key ->
        {
            Assertions.assertEquals(getTestEspressoRecipeMap().get(key).toString(), firestore.getDocument(recipeRepository.collectionName, id).get(key).toString());});
    }

    @Test
    public void testGetRecipe() {
        //Setup
        Recipe recipe = getTestEspressoRecipe();
        DocumentReference documentReference = recipeRepository.saveDocument(recipe);
        usedCollections.add(recipeRepository.collectionName);

        //Execute
        Recipe retrievedRecipe = recipeRepository.getRecipeById(documentReference.getId());
        recipe.setId(retrievedRecipe.getId());

        //Assert
        assertEquals(recipe, retrievedRecipe);

    }

    @Test
    public void testGetDeletedRecipe() {
        //Setup
        Recipe testRecipe = getTestEspressoRecipe();
        DocumentReference documentReference = recipeRepository.saveDocument(testRecipe);
        testRecipe.setId(documentReference.getId());
        recipeRepository.deleteDocument(testRecipe);

        //Execute
        Recipe retrievedRecipe = recipeRepository.verifyRecipe(documentReference);

        //Assert
        assertEquals(-1f, retrievedRecipe.getDuration());

    }
}
