package com.example.hartshteinma.eatyourworld.controller.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hartshteinma.eatyourworld.R;
import com.example.hartshteinma.eatyourworld.model.Model;
import com.example.hartshteinma.eatyourworld.model.Recipe;
import com.example.hartshteinma.eatyourworld.model.interfaces.GetImageListener;

public class RecipeDetailsFragment extends Fragment
{
    private TextView countryTextView, nameTextView, detailsTextView;
    private Button deleteButton, editButton;
    private ImageView recipeImg;
    private Delegate delegate;
    private LinearLayout buttonsBar;
    private ProgressBar spinner;
    private Recipe recipe;
    private boolean fragmentCreated;
    private FragmentCreationListener fragmentCreationListener;

    public interface Delegate
    {
        void onDeletePressed(Recipe recipe);
        void onEditPressed(Recipe recipe, Bitmap recipeImage);
        boolean isOwner(Recipe recipe);
    }

    public interface FragmentCreationListener
    {
        void onFragmentCreated();
    }

    public boolean isFragmentCreated()
    {
        return fragmentCreated;
    }

    public void setFragmentCreationListener(FragmentCreationListener fragmentCreationListener)
    {
        this.fragmentCreationListener = fragmentCreationListener;
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        initWidgets(view);
        if (this.fragmentCreationListener != null)
        {
            this.fragmentCreationListener.onFragmentCreated();
        }
        this.fragmentCreated = true;
        return view;
    }

    private void initWidgets(View view)
    {
        this.spinner = (ProgressBar) view.findViewById(R.id.spinner);
        this.recipeImg = (ImageView) view.findViewById(R.id.recipe_imageView);
        this.countryTextView = (TextView) view.findViewById(R.id.country_name_textView);
        this.nameTextView = (TextView) view.findViewById(R.id.recipe_name_textView);
        this.detailsTextView = (TextView) view.findViewById(R.id.recipe_details_textView);
        this.buttonsBar = (LinearLayout) view.findViewById(R.id.buttons_bar);
        this.deleteButton = (Button) view.findViewById(R.id.delete_button);
        this.editButton = (Button) view.findViewById(R.id.edit_button);
        this.deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                delegate.onDeletePressed(recipe);
            }
        });
        this.editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bitmap bitmap = recipeImg != null ? recipeImg.getDrawingCache() : null;
                delegate.onEditPressed(recipe, bitmap);
            }
        });
    }

    public void displayRecipeDetails(final Recipe recipe)
    {
        if (recipe != null)
        {
            this.recipe = recipe;
            if (this.fragmentCreated)
            {
                if (this.delegate.isOwner(this.recipe))
                    buttonsBar.setVisibility(View.VISIBLE);
                else
                    buttonsBar.setVisibility(View.INVISIBLE);

                this.recipeImg.setVisibility(View.INVISIBLE);
                this.countryTextView.setText(recipe.getCountry());
                this.nameTextView.setText(recipe.getName());
                this.detailsTextView.setText(recipe.getDetails());
                if (recipe.getImgSrc() != null && !recipe.getImgSrc().equals(""))
                {
                    this.spinner.setVisibility(View.VISIBLE);
                    Model.getInstance().getImage(recipe.getImgSrc(), new GetImageListener()
                    {
                        @Override
                        public void onSccess(Bitmap image)
                        {
                            recipeImg.setImageBitmap(image);
                            recipeImg.setVisibility(View.VISIBLE);
                            spinner.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onFail()
                        {

                        }
                    });
                }
                else
                {
                    spinner.setVisibility(View.INVISIBLE);
                    recipeImg.setVisibility(View.VISIBLE);
                    try
                    {
                        recipeImg.setImageResource(R.drawable.picfood);
                    }
                    catch (Exception e)
                    {
                    }
                }
            }
        }
    }
}
