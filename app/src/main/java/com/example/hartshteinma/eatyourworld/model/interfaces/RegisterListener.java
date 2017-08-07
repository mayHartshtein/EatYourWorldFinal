package com.example.hartshteinma.eatyourworld.model.interfaces;

/**
 * Created by hartshteinma on 31/07/2017.
 */

/*  wait fot the register action ended*/

public interface RegisterListener {
    void onRegisterFinished(boolean register, String errorMsg);
}
