package com.example.hartshteinma.eatyourworld.controller.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hartshteinma.eatyourworld.R;
import com.example.hartshteinma.eatyourworld.controller.Constants;
import com.example.hartshteinma.eatyourworld.model.Model;
import com.example.hartshteinma.eatyourworld.model.Recipe;
import com.example.hartshteinma.eatyourworld.model.interfaces.SaveImageListener;

import java.io.IOException;

public class NewRecipeFragment extends Fragment implements View.OnClickListener {
    private EditText countryEt, nameEt, detailsEt;
    private Button saveButton1, cancelButton1, artButton1;
    private ImageView addImageButton;
    private Delegate delegate;
    private String userChoosenTask;
    private Bitmap currentBitmap;
    private String currentUserId;

    public NewRecipeFragment() {
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public interface Delegate {
        void onSaveButtonClick(Recipe recipe);

        void onCancelButtonClick();
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_recipe, container, false);
        initWidgets(view);
        return view;
    }

    private void initWidgets(View view) {
        this.addImageButton = (ImageView) view.findViewById(R.id.fragment_newRecipe_image_imageView);
        this.addImageButton.setOnClickListener(this);
        this.countryEt = (EditText) view.findViewById(R.id.fragmemt_newRecipe_country);
        this.nameEt = (EditText) view.findViewById(R.id.fragmemt_newRecipe_recipeName);
        this.detailsEt = (EditText) view.findViewById(R.id.fragmemt_newRecipe_recipeDetail);

        this.saveButton1 = (Button) view.findViewById(R.id.fragment_newRecipe_saveBtn);
        this.saveButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRecipeFromWidgets(new GeneratingRecipeListener() {
                    @Override
                    public void onRecipeGenerated(Recipe recipe) {
                        delegate.onSaveButtonClick(recipe);
                    }
                });
            }
        });
        this.cancelButton1 = (Button) view.findViewById(R.id.fragment_newRecipe_cancelBtn);
        this.cancelButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.onCancelButtonClick();
            }
        });
    }

    interface GeneratingRecipeListener {
        void onRecipeGenerated(Recipe recipe);
    }

    private void getRecipeFromWidgets(final GeneratingRecipeListener recipeListener) {
        Log.d("NGNGNG", this.currentBitmap + "");
        if (this.currentBitmap != null) {
            String imageName = this.nameEt.getText().toString() + " - " + Calendar.getInstance().getTime().toString();
            Model.getInstance().saveImage(this.currentBitmap, imageName, new SaveImageListener() {
                @Override
                public void fail() {
                    Toast.makeText(getActivity(), "Image was not uploaded", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void complete(String s) {
                    recipeListener.onRecipeGenerated(generateRecipeFromWidgets(s));
                    Toast.makeText(getActivity(), "Image was uploaded", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            recipeListener.onRecipeGenerated(generateRecipeFromWidgets(null));
        }
        /*// TODO: 31/07/2017 fix null user
        try {
            recipe.setUserId(Model.getInstance().getUser().getUserId());
        } catch (Exception e) {
            recipe.setUserId("1234");
        }

        return recipe;*/
    }

    private Recipe generateRecipeFromWidgets(String imageSrc) {
        Recipe recipe = new Recipe();
        Log.d("NGNGNG", "this.currentUserId=" + this.currentUserId);
        Log.d("NGNGNG", "Model.currentUser==null?: " + (Model.getInstance().getUser() == null));
        Log.d("NGNGNG", "Model.currentUser=: " + (Model.getInstance().getUser()));
        recipe.setUserId(this.currentUserId);
        recipe.setCountry(countryEt.getText().toString());
        recipe.setDetails(detailsEt.getText().toString());
        recipe.setImgSrc(imageSrc);
        recipe.setName(nameEt.getText().toString());
        recipe.setName(nameEt.getText().toString());
        recipe.setRecipeId(String.valueOf(Calendar.getInstance().getTime()));
        return recipe;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_newRecipe_image_imageView:
                selectImage();
        }
    }

    private void selectImage() {
        userChoosenTask = null;
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //boolean result = Utilities.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    //if (result)
                    cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    //if (result)
                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constants.REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), Constants.SELECT_FILE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == Constants.REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        addImageButton.setImageBitmap(bm);
        this.currentBitmap = bm;
        Log.d("NGNGNG", "onSelectFromGalleryResult: this.currentBitmap = " + this.currentBitmap);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

        //Uri tempUri = getImageUri(getActivity(), thumbnail);
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        if (thumbnail != null) {
//            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//        }
//
//
//        File destination = new File(Environment.getExternalStorageDirectory(),
//                          System.currentTimeMillis() + ".jpg");
//
//        FileOutputStream fo;
//        try {
//            destination.createNewFile();
//            fo = new FileOutputStream(destination);
//            fo.write(bytes.toByteArray());
//            fo.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Bitmap rotatedThumbnail=null;
//
//        try {
//            rotatedThumbnail=rotateImageIfRequired(thumbnail,tempUri);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        addImageButton.setImageBitmap(thumbnail);
        this.currentBitmap = thumbnail;
        Log.d("NGNGNG", "onCaptureImageResult: this.currentBitmap = " + this.currentBitmap);
    }
//    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }
//
//    //rotating the image if needed
//    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {
//
//        ExifInterface ei = new ExifInterface(selectedImage.getPath());
//        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//
//        switch (orientation) {
//            case ExifInterface.ORIENTATION_ROTATE_90:
//                return rotateImage(img, 90);
//            case ExifInterface.ORIENTATION_ROTATE_180:
//                return rotateImage(img, 180);
//            case ExifInterface.ORIENTATION_ROTATE_270:
//                return rotateImage(img, 270);
//            default:
//                return img;
//        }
//    }
//    private static Bitmap rotateImage(Bitmap img, int degree) {
//        Matrix matrix = new Matrix();
//        matrix.postRotate(degree);
//        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
//        img.recycle();
//        return rotatedImg;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if(userChoosenTask.equals("Take Photo"))
//                        cameraIntent();
//                    else if(userChoosenTask.equals("Choose from Library"))
//                        galleryIntent();
//                } else {
//                    //code for deny
//                }
//                break;
//        }
//    }


}
