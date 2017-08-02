package com.example.hartshteinma.eatyourworld.model.interfaces;

import android.webkit.DownloadListener;

import com.example.hartshteinma.eatyourworld.model.Recipe;
import com.example.hartshteinma.eatyourworld.model.User;
import com.example.hartshteinma.eatyourworld.model.interfaces.DownloadRecipeListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.EditListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.LoginListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.RegisterListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.RemoveListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.UploadListener;

/**
 * Created by hartshteinma on 31/07/2017.
 */

public interface CloudManager {


    void addRecipe(Recipe recipe, UploadListener uploadListener);

    void removeRecipe(Recipe recipe, RemoveListener removeListener);

    void editRecipe(Recipe recipe, EditListener editListener);

    void getAllRecipes(DownloadRecipeListener downloadListener);

    void getRecipesByUser(String userId, DownloadRecipeListener downloadListener);
}
