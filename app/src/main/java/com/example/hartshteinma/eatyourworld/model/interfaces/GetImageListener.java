package com.example.hartshteinma.eatyourworld.model.interfaces;

import android.graphics.Bitmap;

/**
 * Created by hartshteinma on 31/07/2017.
 */

/* wait until the image download from the server */

public interface GetImageListener {
    void onSuccess(Bitmap image);

    void onFail();
}
