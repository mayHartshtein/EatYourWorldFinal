package com.example.hartshteinma.eatyourworld.model.interfaces;

import com.example.hartshteinma.eatyourworld.model.Recipe;

import java.util.List;

/**
 * Created by hartshteinma on 31/07/2017.
 */

public interface RecipesListListener
{
    void datasetChanged(List<Recipe> recipes);
}
