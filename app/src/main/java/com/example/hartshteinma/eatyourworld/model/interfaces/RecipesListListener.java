package com.example.hartshteinma.eatyourworld.model.interfaces;

import com.example.hartshteinma.eatyourworld.model.Recipe;

import java.util.List;

/**
 * Created by hartshteinma on 31/07/2017.
 */

/* listen to the changes in the recipe list from the firebase*/

public interface RecipesListListener
{
    void datasetChanged(List<Recipe> recipes);
}
