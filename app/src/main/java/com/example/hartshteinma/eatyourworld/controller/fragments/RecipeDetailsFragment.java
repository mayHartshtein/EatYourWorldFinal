package com.example.hartshteinma.eatyourworld.controller.fragments;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hartshteinma.eatyourworld.R;
import com.example.hartshteinma.eatyourworld.model.Model;
import com.example.hartshteinma.eatyourworld.model.Recipe;

import java.util.List;

public class RecipeDetailsFragment extends Fragment {
    private TextView countryTv, nameTv, detailsTv;
    private Delegate delegate;
    private Button deleteBtn,editBtn;
    private ImageView recipeImg;
  //  private Boolean isOwner;


    public interface Delegate {
        void onCreateViewFinished();
//        void onDeletePressed(Recipe recipe);
//        void onEditPressed(Recipe recipe);
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
//        openScreem(this.isOwner);
        delegate.onCreateViewFinished();
        return view;

    }

//    public void setOwner(Boolean owner) {
//        isOwner = owner;
//    }

    private void initWidgets(View view) {
        this.recipeImg = (ImageView) view.findViewById(R.id.fragment_details_Recipe_image);
        this.countryTv = (TextView) view.findViewById(R.id.fragment_recipeDetails_countryName);
        this.nameTv = (TextView) view.findViewById(R.id.fragment_recipeDetails_recipeName);
        this.detailsTv = (TextView) view.findViewById(R.id.recipe_details_textView);
        this.deleteBtn=(Button) view.findViewById(R.id.fragment_recipeDetails_deleteBtn);
        this.editBtn=(Button) view.findViewById(R.id.fragment_recipeDetails_editBtn);


//        this.deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                delegate.onDeletePressed(getRepiceFromWidgets());
//            }
//        });
//        this.editBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                delegate.onEditPressed(getRepiceFromWidgets());
//            }
//        });

    }

    public void displayRecipeDetails(Recipe recipe) {
        if (recipe != null && this.countryTv != null) {
            this.countryTv.setText(recipe.getCountry());
            this.nameTv.setText(recipe.getName());
            this.detailsTv.setText(recipe.getDetails());
            //this.recipeImg.setIma;
        }
    }

    public void openScreem(boolean isOwner) {
        if(editBtn!=null&& deleteBtn!=null)
        {
            if(isOwner)
            {
                deleteBtn.setVisibility(View.VISIBLE);
                editBtn.setVisibility(View.VISIBLE);
            }else
            {
                deleteBtn.setVisibility(View.INVISIBLE);
                editBtn.setVisibility(View.INVISIBLE);
            }


        }

    }

    private Recipe getRepiceFromWidgets() {
        Recipe recipe = new Recipe();
        recipe.setCountry(this.countryTv.getText().toString());
        recipe.setDetails(this.detailsTv.getText().toString());
//        recipe.setImgSrc(this.countryEt.getText().toString()); // TODO: 31/07/2017 handle image source
        recipe.setName(this.nameTv.getText().toString());
        recipe.setRecipeId(String.valueOf(Calendar.getInstance().getTime()));
        recipe.setUserId(Model.getInstance().getUser().getUserId());
        return recipe;
    }

}
