package com.example.hartshteinma.eatyourworld.model.interfaces;

import com.example.hartshteinma.eatyourworld.model.User;

/**
 * Created by hartshteinma on 31/07/2017.
 */

/* make the authentication , responssible for download the user by email , get the email and return the user */

public interface AuthManager {

    void login(String email, String password, LoginListener loginListener);

    void register(User user, RegisterListener registerListener);

    void getUserByEmail(String email, DownloadListener downloadListener);
}
