package com.example.hartshteinma.eatyourworld.model.interfaces;

import com.example.hartshteinma.eatyourworld.model.Recipe;

/**
 * Created by hartshteinma on 31/07/2017.
 */

/* manager the recipes , all the actions on the recipes*/
public interface CloudManager {

    void addRecipe(Recipe recipe, UploadListener uploadListener);

    void removeRecipe(Recipe recipe, RemoveRecipeListener removeRecipeListener);

    void editRecipe(Recipe recipe, EditListener editListener);

    void getAllRecipes(DownloadRecipeListener downloadListener);

    void getRecipesByUser(String userId, DownloadRecipeListener downloadListener);
}
