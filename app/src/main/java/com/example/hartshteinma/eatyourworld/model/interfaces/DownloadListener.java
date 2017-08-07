package com.example.hartshteinma.eatyourworld.model.interfaces;

import com.example.hartshteinma.eatyourworld.model.User;

/**
 * Created by hartshteinma on 31/07/2017.
 */
/* listen until the user download ended*/

public interface DownloadListener {
    void onDownloadFinished(User user);
}
