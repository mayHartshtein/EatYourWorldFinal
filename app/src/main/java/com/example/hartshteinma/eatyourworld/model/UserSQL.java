package com.example.hartshteinma.eatyourworld.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hartshteinma on 26/07/2017.
 */

public class UserSQL
{
    private static final String USERS = "users";
    private static final String USER_ID = "userId";
    private static final String USER_FIRST_NAME = "firstName";
    private static final String USER_LAST_NAME = "lastName";
    private static final String USER_BIRTHDAY = "birthday";
    private static final String USER_EMAIL = "email";
    private static final String USER_PASSWORD = "password";

    static User getCurrentUser(SQLiteDatabase db)
    {
        List<User> list = new LinkedList<User>();
        try
        {
            Cursor cursor = db.query("users", null, null, null, null, null, null);
            if (cursor.moveToFirst())
            {
                int idIndex = cursor.getColumnIndex(USER_ID);
                int firstNameIndex = cursor.getColumnIndex(USER_FIRST_NAME);
                int lastNameIndex = cursor.getColumnIndex(USER_LAST_NAME);
                int birthdayIndex = cursor.getColumnIndex(USER_BIRTHDAY);
                int emailIndex = cursor.getColumnIndex(USER_EMAIL);
                int passwordIndex = cursor.getColumnIndex(USER_PASSWORD);
                do
                {
                    User user = new User();
                    user.setUserId(cursor.getString(idIndex));
                    user.setFirstName(cursor.getString(firstNameIndex));
                    user.setLastName(cursor.getString(lastNameIndex));
                    user.setBirthday(cursor.getString(birthdayIndex));
                    user.setEmail(cursor.getString(emailIndex));
                    user.setPassword(cursor.getString(passwordIndex));
                    list.add(user);
                } while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
        }
        if (list == null || list.size() == 0)
            return null;
        return list.get(0);
    }

    static void setCurrentUser(SQLiteDatabase db, User user)
    {
        ContentValues values = new ContentValues();
        values.put(USER_ID, user.getUserId());
        values.put(USER_FIRST_NAME, user.getFirstName());
        values.put(USER_LAST_NAME, user.getLastName());
        values.put(USER_BIRTHDAY, user.getBirthday());
        values.put(USER_EMAIL, user.getEmail());
        values.put(USER_PASSWORD, user.getPassword());
        try
        {
            dropTable(db);
        }
        catch (Exception e)
        {
        }
        create(db);
        db.insert(USERS, USER_ID, values);
    }

    static User getUser(SQLiteDatabase db, String id)
    {
        String[] selectionArgs = {id};
        Cursor cursor = db.query(USERS, null, USER_ID + " = ?", selectionArgs, null, null, null);
        User user = null;
        if (cursor.moveToFirst() == true)
        {
            String userId = cursor.getString(cursor.getColumnIndex(USER_ID));
            String firstName = cursor.getString(cursor.getColumnIndex(USER_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndex(USER_LAST_NAME));
            String birthday = cursor.getString(cursor.getColumnIndex(USER_BIRTHDAY));
            String email = cursor.getString(cursor.getColumnIndex(USER_EMAIL));
            String password = cursor.getString(cursor.getColumnIndex(USER_PASSWORD));
            user = new User(userId, firstName, lastName, birthday, email, password);
        }
        return user;
    }

    public static void create(SQLiteDatabase db)
    {
        db.execSQL("create table " + USERS +
                " (" +
                USER_ID + " TEXT PRIMARY KEY, " +
                USER_FIRST_NAME + " TEXT, " +
                USER_LAST_NAME + " TEXT, " +
                USER_BIRTHDAY + " TEXT, " +
                USER_EMAIL + " TEXT, " +
                USER_PASSWORD + " TEXT);");
    }

    public static void dropTable(SQLiteDatabase sqLiteDatabase)
    {
        try
        {
            sqLiteDatabase.execSQL("DROP TABLE " + USERS);
        }
        catch (Exception e)
        {
        }
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("drop " + USERS + ";");
        create(db);
    }
}
