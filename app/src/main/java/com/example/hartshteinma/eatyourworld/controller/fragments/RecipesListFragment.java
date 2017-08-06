package com.example.hartshteinma.eatyourworld.controller.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hartshteinma.eatyourworld.R;
import com.example.hartshteinma.eatyourworld.dialogs.MyProgressBar;
import com.example.hartshteinma.eatyourworld.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipesListFragment extends Fragment {
    private ListView listView;
    private ImageButton addButton;
    private List<Recipe> recipes;
    private CustomAdapater adapater;
    private Delegate delegate;

    public interface Delegate {
        void onItemClicked(int position, Recipe recipe);

        void onAddButtonClicked();
    }

    public RecipesListFragment() {
        this.recipes = new ArrayList<>();
    }

    public void setDelegate(Delegate dlg) {
        this.delegate = dlg;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Recipes List:");
        View contentView = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        initWidgets(contentView);
        return contentView;
    }

    private void initWidgets(View contentView) {
        this.adapater = new CustomAdapater();
        this.listView = (ListView) contentView.findViewById(R.id.recipes_listView);
        this.listView.setAdapter(this.adapater);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                delegate.onItemClicked(position, recipes.get(position));
            }
        });
        this.addButton = (ImageButton) contentView.findViewById(R.id.add_button);
        this.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.onAddButtonClicked();
            }
        });
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        refreshListView();
    }

    public void refreshListView() {
        if (this.adapater != null) {
            this.adapater.notifyDataSetChanged();
        }
    }

    class CustomAdapater extends BaseAdapter {

        @Override
        public int getCount() {
            return recipes.size();
        }

        @Override
        public Object getItem(int position) {
            return recipes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.recipe_row_layout, null);
            }
            TextView countryTv, recipeNameTv;
            countryTv = (TextView) convertView.findViewById(R.id.country_textView);
            recipeNameTv = (TextView) convertView.findViewById(R.id.recipe_name_textView);
            Recipe recipe = recipes.get(position);
            countryTv.setText(recipe.getCountry());
            recipeNameTv.setText(recipe.getName());
            return convertView;
        }
    }

}


