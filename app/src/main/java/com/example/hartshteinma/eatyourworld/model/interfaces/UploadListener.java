package com.example.hartshteinma.eatyourworld.model.interfaces;

import com.google.firebase.database.DatabaseError;

/**
 * Created by hartshteinma on 31/07/2017.
 */

public interface UploadListener {
    void onRecipeAdded(DatabaseError e);
}
