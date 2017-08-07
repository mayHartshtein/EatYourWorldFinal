package com.example.hartshteinma.eatyourworld.model.interfaces;

/**
 * Created by hartshteinma on 31/07/2017.
 */

/* when there is an edit changes return T\F if it succeseed  if not then returned an error massage */

public interface EditListener {
    void onEditFinished(boolean success, String errorMsg);
}
