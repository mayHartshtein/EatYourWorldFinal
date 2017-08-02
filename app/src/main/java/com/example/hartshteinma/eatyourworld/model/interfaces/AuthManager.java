package com.example.hartshteinma.eatyourworld.model.interfaces;

import com.example.hartshteinma.eatyourworld.model.User;

/**
 * Created by zegu23 on 01/08/2017.
 */

public interface AuthManager {

    void login(String email, String password, LoginListener loginListener);

    void register(User user, RegisterListener registerListener);

    void getUserByEmail(String email, DownloadListener downloadListener);
}
