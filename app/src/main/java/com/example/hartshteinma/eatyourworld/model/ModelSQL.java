package com.example.hartshteinma.eatyourworld.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hartshteinma on 26/07/2017.
 */


public class ModelSQL extends SQLiteOpenHelper {
    ModelSQL(Context context) {
        super(context, "database.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        UserSQL.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        UserSQL.onUpgrade(db, oldVersion, newVersion);
    }

}