package com.example.hartshteinma.eatyourworld.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zegu23 on 02/08/2017.
 */

public interface LocalManager {
    List<Recipe> getAllRecipe(SQLiteDatabase db);

    void addRecipe(SQLiteDatabase db, Recipe recipe);

    Recipe getRecipe(SQLiteDatabase db, String recipeId);

    public void onCreate(SQLiteDatabase db);

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

}
