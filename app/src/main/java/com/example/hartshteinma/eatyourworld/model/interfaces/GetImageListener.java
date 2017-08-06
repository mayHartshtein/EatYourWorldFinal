package com.example.hartshteinma.eatyourworld.model.interfaces;

import android.graphics.Bitmap;

/**
 * Created by zegu23 on 02/08/2017.
 */

public interface GetImageListener {
    void onSuccess(Bitmap image);

    void onFail();
}
