package com.example.hartshteinma.eatyourworld.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hartshteinma on 26/07/2017.
 */

public class UserSQL {
    private static final String  USERS = "users";
    private static final String  USER_ID = "userID";
    private static final String  USER_FNAME = "firstName";
    private static final String  USER_lNAME = "lasttName";
    private static final String  IMAGE_URL = "imageUrl";
    private static final String  COUNTRY = "country";
    private static final String  USER_PASSWORD = "password";
    private static final String USER_EMAIL = "email";

    static List<User> getAllUsers(SQLiteDatabase db) {
        Cursor cursor = db.query("users", null, null, null, null, null, null);
        List<User> list = new LinkedList<User>();
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(USER_ID);
            int firstNameIndex = cursor.getColumnIndex(USER_FNAME);
            int lastNameIndex = cursor.getColumnIndex(USER_lNAME);
            int countryIndex = cursor.getColumnIndex(COUNTRY);
           // int imageUrlIndex = cursor.getColumnIndex(IMAGE_URL);
            int userPassword = cursor.getColumnIndex(USER_PASSWORD);
            int userEmail = cursor.getColumnIndex(USER_EMAIL);



            do {
                User user = new User();
                user.setUserId(cursor.getString(idIndex));
                user.setFirstName(cursor.getString(firstNameIndex));
                user.setLastName(cursor.getString(lastNameIndex));
                user.setCountry(cursor.getString(countryIndex));
                user.setPassword(cursor.getString(userPassword));
                user.setEmail(cursor.getString(userEmail));
            //    user.setUserImage(cursor.getBlob(imageUrlIndex));

                list.add(user);
            } while (cursor.moveToNext());
        }
        return list;
    }


    static void addUser(SQLiteDatabase db, User user) {
        ContentValues values = new ContentValues();
        values.put(USER_ID, user.getUserId());
        values.put(USER_FNAME, user.getFirstName());
        values.put(USER_lNAME, user.getLastName());
        values.put(COUNTRY, user.getCountry());
        values.put(USER_PASSWORD, user.getPassword());
        values.put(USER_EMAIL, user.getEmail());


//        if (user.checked) {
//            values.put(STUDENT_CHECK, 1);
//        } else {
//            values.put(STUDENT_CHECK, 0);
//        }
      //  values.put(STUDENT_IMAGE_URL, st.imageUrl);
        db.insert(USERS, USER_ID, values);
    }

    static User getUser(SQLiteDatabase db, String userId) {
        return null;
    }


    static public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + USERS +
                " (" +
                USER_ID + " TEXT PRIMARY KEY, " +
                USER_FNAME + " TEXT, " +
                USER_lNAME + " TEXT, " +
                COUNTRY + " TEXT, " +
                USER_PASSWORD + " TEXT, " +
                USER_EMAIL + " TEXT, " +
                IMAGE_URL + " TEXT);");
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop " + USERS + ";");
        onCreate(db);
    }


}
