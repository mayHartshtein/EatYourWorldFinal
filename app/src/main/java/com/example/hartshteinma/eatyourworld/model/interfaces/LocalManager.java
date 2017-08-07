package com.example.hartshteinma.eatyourworld.model.interfaces;

import com.example.hartshteinma.eatyourworld.model.User;

/**
 * Created by hartshteinma on 31/07/2017.
 */

/* deal with the local save of the user*/


public interface LocalManager
{
    void setUser(User user);
    User getUserById(String id);
    void dropTable();
    User getCurrentUser();
}
