package com.example.hartshteinma.eatyourworld.model.interfaces;

/**
 * Created by hartshteinma on 31/07/2017.
 */

/* wait until upload recipe action ended */

public interface UploadListener
{
    void onRecipeAdded(boolean success, String errorMsg);
}
