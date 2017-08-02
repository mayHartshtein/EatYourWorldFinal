package com.example.hartshteinma.eatyourworld.model.interfaces;

import android.graphics.Bitmap;

/**
 * Created by zegu23 on 02/08/2017.
 */

public interface ImagesLoader {
    void saveImage(Bitmap imageBmp, String name, SaveImageListener listener);

    void getImage(String url, GetImageListener listener);

    void removeImage(String url, RemoveImageListener listener);
}
