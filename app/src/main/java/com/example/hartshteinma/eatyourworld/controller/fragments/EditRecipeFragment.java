package com.example.hartshteinma.eatyourworld.controller.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.hartshteinma.eatyourworld.R;
import com.example.hartshteinma.eatyourworld.model.Recipe;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditRecipeFragment extends Fragment {

    private EditText countryEt, nameEt, detailsEt;
    private Button saveButton1, cancelButton1;
    private ImageView addImageButton;
//    private NewRecipeFragment.Delegate delegate;
    private String userChoosenTask;
    private Bitmap currentBitmap;
    private String currentUserId;
    private Delegate delegate;


    public EditRecipeFragment() {
        // Required empty public constructor
    }


    public interface Delegate {
        void onSaveButtonClick(Recipe recipe);

        void onCancelButtonClick();
    }

    public void setDelegate(NewRecipeFragment.Delegate delegate) {
        this.delegate = (Delegate) delegate;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        initWidgets(view);
        return view;

    }

    public void initWidgets(View view){

        this.addImageButton = (ImageView) view.findViewById(R.id.fragment_editRecipe_image_imageView);
       // this.addImageButton.setOnClickListener(this);
        this.countryEt = (EditText) view.findViewById(R.id.fragmemt_editRecipe_country);
        this.nameEt = (EditText) view.findViewById(R.id.fragmemt_editRecipe_recipeName);
        this.detailsEt = (EditText) view.findViewById(R.id.fragmemt_editRecipe_recipeDetail);
        this.saveButton1 = (Button) view.findViewById(R.id.fragment_editRecipe_saveBtn);
        this.cancelButton1 = (Button) view.findViewById(R.id.fragment_editRecipe_cancelBtn);

        this.cancelButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.onCancelButtonClick();
            }
        });

    }

}
