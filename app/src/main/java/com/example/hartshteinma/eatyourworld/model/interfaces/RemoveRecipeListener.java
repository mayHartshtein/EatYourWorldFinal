package com.example.hartshteinma.eatyourworld.model.interfaces;

/**
 * Created by hartshteinma on 31/07/2017.
 */

/* wait fir the remove recipe action ended*/

public interface RemoveRecipeListener
{
    void onRecipeRemoved(boolean success, String errorMsg);
}
