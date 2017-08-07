package com.example.hartshteinma.eatyourworld.model.interfaces;

import android.graphics.Bitmap;

/**
 * Created by hartshteinma on 31/07/2017.
 */


/* upload and download the image , all the actions on the images*/

public interface ImagesLoader {
    void saveImage(Bitmap imageBmp, String name, SaveImageListener listener);

    void getImage(String url, GetImageListener listener);

    void removeImage(String url, RemoveImageListener listener);
}
