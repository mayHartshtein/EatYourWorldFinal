package com.example.hartshteinma.eatyourworld.controller.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hartshteinma.eatyourworld.R;
import com.example.hartshteinma.eatyourworld.model.Recipe;

public class RecipeDetailsFragment extends Fragment {
    private TextView countryTv, nameTv, detailsTv;
    private Delegate delegate;

    public interface Delegate {
        void onCreateViewFinished();
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
        this.countryTv = (TextView) view.findViewById(R.id.fragment_recipeDetails_countryName);
        this.nameTv = (TextView) view.findViewById(R.id.fragment_recipeDetails_recipeName);
        this.detailsTv = (TextView) view.findViewById(R.id.recipe_details_textView);
    }

    public void displayRecipeDetails(Recipe recipe) {
        if (recipe != null && this.countryTv != null) {
            this.countryTv.setText(recipe.getCountry());
            this.nameTv.setText(recipe.getName());
            this.detailsTv.setText(recipe.getDetails());
        }
    }
}
