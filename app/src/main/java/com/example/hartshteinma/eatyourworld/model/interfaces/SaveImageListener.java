package com.example.hartshteinma.eatyourworld.model.interfaces;

/**
 * Created by hartshteinma on 31/07/2017.
 */

/* wait until the save image action ended complete or failed*/

public interface SaveImageListener
{
    void complete(String s);

    void fail();
}
