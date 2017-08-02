package com.example.hartshteinma.eatyourworld.controller.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hartshteinma.eatyourworld.R;
import com.example.hartshteinma.eatyourworld.model.Model;
import com.example.hartshteinma.eatyourworld.model.Recipe;
import com.example.hartshteinma.eatyourworld.model.interfaces.GetImageListener;

public class RecipeDetailsFragment extends Fragment {
    private TextView countryTv, nameTv, detailsTv;
    private Button deleteBtn;
    private ImageView recipeImg;
    private Delegate delegate;
    private boolean isOwner;
    private LinearLayout buttonsBar;
    private ProgressBar spinner;

    public interface Delegate {
        void onCreateViewFinished();

        void onDeletePressed(Recipe recipe);
    }

    public void setOwner(boolean owner) {
        this.isOwner = owner;
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public RecipeDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        initWidgets(view);
        delegate.onCreateViewFinished();
        return view;

    }

    private void initWidgets(View view) {
        this.spinner = (ProgressBar) view.findViewById(R.id.spinner);
        this.recipeImg = (ImageView) view.findViewById(R.id.fragment_details_Recipe_image);
        this.countryTv = (TextView) view.findViewById(R.id.fragment_recipeDetails_countryName);
        this.nameTv = (TextView) view.findViewById(R.id.fragment_recipeDetails_recipeName);
        this.detailsTv = (TextView) view.findViewById(R.id.recipe_details_textView);
        this.deleteBtn = (Button) view.findViewById(R.id.fragment_recipeDetails_deleteBtn);
        this.buttonsBar = (LinearLayout) view.findViewById(R.id.buttons_bar);

        this.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.onDeletePressed(getRepiceFromWidgets());
            }
        });

    }

    public void displayRecipeDetails(Recipe recipe) {
        if (recipe != null && this.countryTv != null) {
            this.countryTv.setText(recipe.getCountry());
            this.nameTv.setText(recipe.getName());
            this.detailsTv.setText(recipe.getDetails());
            if (recipe.getImgSrc() != null) {
                Model.getInstance().getImage(recipe.getImgSrc(), new GetImageListener() {
                    @Override
                    public void onSccess(Bitmap image) {
                        recipeImg.setImageBitmap(image);
                        recipeImg.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFail() {

                    }
                });
            }
        }
    }

    @Override
    public void onResume() {
        /*if (this.buttonsBar != null) {
            if (this.isOwner) {
                buttonsBar.setVisibility(View.VISIBLE);
            } else {
                buttonsBar.setVisibility(View.INVISIBLE);
            }
        }*/
        super.onResume();
    }

    private Recipe getRepiceFromWidgets() {
        Recipe recipe = new Recipe();
        recipe.setCountry(this.countryTv.getText().toString());
        recipe.setDetails(this.detailsTv.getText().toString());
        recipe.setName(this.nameTv.getText().toString());
        recipe.setRecipeId(String.valueOf(Calendar.getInstance().getTime()));
        recipe.setUserId(Model.getInstance().getUser().getUserId());
        return recipe;
    }

}
