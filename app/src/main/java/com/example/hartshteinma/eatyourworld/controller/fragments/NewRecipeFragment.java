package com.example.hartshteinma.eatyourworld.controller.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.hartshteinma.eatyourworld.R;
import com.example.hartshteinma.eatyourworld.controller.Constants;
import com.example.hartshteinma.eatyourworld.model.Model;
import com.example.hartshteinma.eatyourworld.model.Recipe;
import com.example.hartshteinma.eatyourworld.model.Utilities;

public class NewRecipeFragment extends Fragment implements View.OnClickListener {
    private EditText countryEt, nameEt, detailsEt;
    private Button saveButton, cancelButton;
    private ImageView addImageButton;
    private Delegate delegate;
    String userChoosenTask;

    public NewRecipeFragment() {

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
        this.saveButton = (Button) view.findViewById(R.id.fragment_newRecipe_saveBtn);
        this.cancelButton = (Button) view.findViewById(R.id.fragment_newRecipe_cancelBtn);
        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.onSaveButtonClick(getRepiceFromWidgets());
            }
        });
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.onCancelButtonClick();
            }
        });
    }

    private Recipe getRepiceFromWidgets() {
        Recipe recipe = new Recipe();
        recipe.setCountry(this.countryEt.getText().toString());
        recipe.setDetails(this.detailsEt.getText().toString());
//        recipe.setImgSrc(this.countryEt.getText().toString()); // TODO: 31/07/2017 handle image source
        recipe.setName(this.nameEt.getText().toString());
        recipe.setRecipeId(String.valueOf(Calendar.getInstance().getTime()));
        recipe.setUserId(Model.getInstance().getUser().getUserId());
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
                boolean result = Utilities.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
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
}
