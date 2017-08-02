package com.example.hartshteinma.eatyourworld.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by DELL on 8/1/2017.
 */

public class RecipeSQL {

    private static final String RECIPES = "recipes";
    private static final String RECIPE_NAME = "recipeName";
    private static final String RECIPE_COUNTRY = "recipeCountry";
    private static final String DETAILS = "details";
    private static final String IMAGE_URL = "imageUrl";
    private static final String USER_ID = "userId";
    private static final String RECIPE_ID = "recipeId";


    static List<Recipe> getAllRecipes(SQLiteDatabase db) {
        Cursor cursor = db.query("Recipe", null, null, null, null, null, null);
        List<Recipe> list = new LinkedList<Recipe>();
        if (cursor.moveToFirst()) {
            int userIdIndex = cursor.getColumnIndex(USER_ID);
            int recipeIdIndex = cursor.getColumnIndex(RECIPE_ID);
            int recipeNameIndex = cursor.getColumnIndex(RECIPE_NAME);
            int recipeCountryIndex = cursor.getColumnIndex(RECIPE_COUNTRY);
            // int imageUrlIndex = cursor.getColumnIndex(IMAGE_URL);
            int recipeDetails = cursor.getColumnIndex(DETAILS);


            do {
                Recipe recipe = new Recipe();
                recipe.setName(cursor.getString(recipeNameIndex));
                recipe.setUserId(cursor.getString(userIdIndex));
                recipe.setRecipeId(cursor.getString(recipeIdIndex));
                recipe.setCountry(cursor.getString(recipeCountryIndex));
                recipe.setDetails(cursor.getString(recipeDetails));
                //  recipe.setImgSrc(cursor.getBlob(imageUrlIndex));

                list.add(recipe);
            } while (cursor.moveToNext());
        }
        return list;
    }

    static void addRecipe(SQLiteDatabase db, Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put(USER_ID, recipe.getUserId());
        values.put(RECIPE_ID, recipe.getRecipeId());
        values.put(RECIPE_NAME, recipe.getName());
        values.put(RECIPE_COUNTRY, recipe.getCountry());
        values.put(DETAILS, recipe.getDetails());


//        if (user.checked) {
//            values.put(STUDENT_CHECK, 1);
//        } else {
//            values.put(STUDENT_CHECK, 0);
//        }
        //  values.put(STUDENT_IMAGE_URL, st.imageUrl);
        db.insert(RECIPES, RECIPE_ID, values);
    }

    static Recipe getRecipe(SQLiteDatabase db, String recipeId) {
        return null;
    }


    static public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + RECIPES +
                " (" +
                USER_ID + " TEXT PRIMARY KEY, " +
                RECIPE_ID + " TEXT, " +
                RECIPE_NAME + " TEXT, " +
                RECIPE_COUNTRY + " TEXT, " +
                DETAILS + " TEXT, " +
                IMAGE_URL + " TEXT);");
    }


    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop " + RECIPES + ";");
        onCreate(db);
    }


}
