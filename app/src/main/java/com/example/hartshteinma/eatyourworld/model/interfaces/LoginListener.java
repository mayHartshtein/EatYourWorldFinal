package com.example.hartshteinma.eatyourworld.model.interfaces;

/**
 * Created by hartshteinma on 31/07/2017.
 */

/* waiting to the login action finished*/

public interface LoginListener {
    void onLoginFinished(boolean login, String errorMsg);
}
