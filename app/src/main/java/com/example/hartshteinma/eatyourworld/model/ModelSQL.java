package com.example.hartshteinma.eatyourworld.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hartshteinma.eatyourworld.model.interfaces.LocalManager;

import java.util.List;

/**
 * Created by hartshteinma on 26/07/2017.
 */

public class ModelSQL implements LocalManager
{
    private SQLiteOpenHelper helper;
    private int version = 1;
    private UserSQL usersSql;

    public ModelSQL(Context context)
    {
        helper = new Helper(context);
        usersSql = new UserSQL();
    }

    @Override
    public void setUser(User user)
    {
        usersSql.setCurrentUser(helper.getWritableDatabase(), user);
    }

    @Override
    public User getUserById(String id)
    {
        return this.usersSql.getUser(helper.getReadableDatabase(), id);
    }

    @Override
    public void dropTable()
    {
        this.usersSql.dropTable(helper.getWritableDatabase());
    }

    @Override
    public User getCurrentUser()
    {
        return this.usersSql.getCurrentUser(helper.getReadableDatabase());
    }

    public class Helper extends SQLiteOpenHelper
    {


        public Helper(Context context)
        {
            super(context, "database.db", null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase)
        {
            usersSql.create(sqLiteDatabase);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
        {
            usersSql.dropTable(sqLiteDatabase);
            onCreate(sqLiteDatabase);
        }
    }

}