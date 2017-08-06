package com.example.hartshteinma.eatyourworld.model.interfaces;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.hartshteinma.eatyourworld.model.Recipe;
import com.example.hartshteinma.eatyourworld.model.User;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zegu23 on 02/08/2017.
 */

public interface LocalManager
{
    public void setUser(User user);
    public User getUserById(String id);
    public void dropTable();
    public User getCurrentUser();
}
