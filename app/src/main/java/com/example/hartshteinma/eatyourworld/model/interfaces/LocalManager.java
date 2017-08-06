package com.example.hartshteinma.eatyourworld.model.interfaces;

import com.example.hartshteinma.eatyourworld.model.User;

/**
 * Created by zegu23 on 02/08/2017.
 */

public interface LocalManager
{
    void setUser(User user);
    User getUserById(String id);
    void dropTable();
    User getCurrentUser();
}
