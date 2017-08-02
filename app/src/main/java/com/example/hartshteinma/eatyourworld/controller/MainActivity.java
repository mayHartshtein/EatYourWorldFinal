package com.example.hartshteinma.eatyourworld.controller;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.hartshteinma.eatyourworld.R;
import com.example.hartshteinma.eatyourworld.controller.fragments.MainFragment;
import com.example.hartshteinma.eatyourworld.controller.fragments.NewRecipeFragment;
import com.example.hartshteinma.eatyourworld.controller.fragments.RecipeDetailsFragment;
import com.example.hartshteinma.eatyourworld.controller.fragments.RecipesListFragment;
import com.example.hartshteinma.eatyourworld.controller.fragments.RegisterFragment;
import com.example.hartshteinma.eatyourworld.controller.fragments.SignInFragment;
import com.example.hartshteinma.eatyourworld.dialogs.MyProgressBar;
import com.example.hartshteinma.eatyourworld.model.Model;
import com.example.hartshteinma.eatyourworld.model.Recipe;
import com.example.hartshteinma.eatyourworld.model.User;
import com.example.hartshteinma.eatyourworld.model.interfaces.LoginListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.RecipesListListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.RegisterListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.UploadListener;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public class MainActivity extends Activity {

    private FragmentTransaction ftr;
    private MainFragment mainFragment;
    private SignInFragment signInFragment;
    private MyProgressBar progressBar;
    private RegisterFragment registerFragment;
    private RecipesListFragment recipesListFragment;
    private NewRecipeFragment newRecipeFragment;
    private RecipeDetailsFragment recipeDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(MainActivity.this);
        initFragments();
        initActionBar();
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initFragments() {
        mainFragment = new MainFragment();
        signInFragment = new SignInFragment();
        registerFragment = new RegisterFragment();
        recipesListFragment = new RecipesListFragment();
        newRecipeFragment = new NewRecipeFragment();
        recipeDetailsFragment = new RecipeDetailsFragment();
        initFragmentsListeners();

        ftr = getFragmentManager().beginTransaction();

        ftr.add(R.id.main_container, mainFragment);

        ftr.show(mainFragment);
        ftr.commit();


    }

    private void initFragmentsListeners() {
        mainFragment.setDelegate(new MainFragment.Delegate() {
            @Override
            public void onSignInPressed() {
                switchToFragment(signInFragment);
            }

            @Override
            public void onRegisterPressed() {
                switchToFragment(registerFragment);
            }

        });
        signInFragment.setDelegate(new SignInFragment.Delegate() {
            @Override
            public void onSignInPressed(final String email, String password) {
                Model.getInstance().login(email, password, new LoginListener() {
                    @Override
                    public void onLoginFinished(boolean login) {
                        if (login) {
                            Model.getInstance().setUserByEmail(email);
                            switchToFragment(recipesListFragment);
                            recipesListFragment.refreshListView();
                        }
                    }
                });
            }
        });
        registerFragment.setDelegate(new RegisterFragment.Delegate() {
            @Override
            public void onRegisterButtonClick(final User user) {
                Toast.makeText(MainActivity.this, "onRegisterButtonClick", Toast.LENGTH_SHORT).show();
                Model.getInstance().register(user, new RegisterListener() {
                    @Override
                    public void onRegisterFinished(boolean register) {
                        if (register) {
                            Model.getInstance().setUser(user);
                            switchToFragment(recipesListFragment);
                            recipesListFragment.refreshListView();
                        }
                    }
                });
            }
        });
        newRecipeFragment.setDelegate(new NewRecipeFragment.Delegate() {
            @Override
            public void onSaveButtonClick(Recipe recipe) {
                Model.getInstance().addRecipe(recipe, new UploadListener() {
                    @Override
                    public void onRecipeAdded(DatabaseError e) {
                        Toast.makeText(MainActivity.this, "Recipe added", Toast.LENGTH_SHORT).show();
                        switchToFragment(recipesListFragment);
                        recipesListFragment.refreshListView();
                    }
                });
            }

            @Override
            public void onCancelButtonClick() {
                switchToFragment(recipesListFragment);
                recipesListFragment.refreshListView();
            }
        });
        recipesListFragment.setDelegate(new RecipesListFragment.Delegate() {

            @Override
            public void onItemClicked(int position, final Recipe recipe) {
                recipeDetailsFragment.setDelegate(new RecipeDetailsFragment.Delegate() {
                    @Override
                    public void onCreateViewFinished() {
                        recipeDetailsFragment.displayRecipeDetails(recipe);
                    }
                });
                switchToFragment(recipeDetailsFragment);
                recipeDetailsFragment.displayRecipeDetails(recipe);
            }

            @Override
            public void onAddButtonClicked() {
                switchToFragment(newRecipeFragment);
            }
        });
        recipesListFragment.setRecipes(Model.getInstance().getRecipes());
        Model.getInstance().setRecipesListListener(new RecipesListListener() {
            @Override
            public void datasetChanged(List<Recipe> recipes) {
                recipesListFragment.refreshListView();
            }
        });
    }

    private void switchToFragment(Fragment fragment) {
        ftr = getFragmentManager().beginTransaction();
        ftr.replace(R.id.main_container, fragment);
        ftr.commit();
    }

    private void backButton() {
        if (mainFragment.isVisible()) {
            super.onBackPressed();
        } else if (newRecipeFragment.isVisible()) {
            switchToFragment(recipesListFragment);
        } else if (recipeDetailsFragment.isVisible()) {
            switchToFragment(recipesListFragment);
        } else if (recipesListFragment.isVisible()) {
            switchToFragment(mainFragment);
        } else if (registerFragment.isVisible()) {
            switchToFragment(mainFragment);
        } else if (signInFragment.isVisible()) {
            switchToFragment(mainFragment);
        }
    }

    @Override
    public void onBackPressed() {
        backButton();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backButton();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
