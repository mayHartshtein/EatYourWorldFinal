package com.example.hartshteinma.eatyourworld.controller;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hartshteinma.eatyourworld.R;
import com.example.hartshteinma.eatyourworld.controller.fragments.EditRecipeFragment;
import com.example.hartshteinma.eatyourworld.controller.fragments.MainFragment;
import com.example.hartshteinma.eatyourworld.controller.fragments.NewRecipeFragment;
import com.example.hartshteinma.eatyourworld.controller.fragments.RecipeDetailsFragment;
import com.example.hartshteinma.eatyourworld.controller.fragments.RecipesListFragment;
import com.example.hartshteinma.eatyourworld.controller.fragments.RegisterFragment;
import com.example.hartshteinma.eatyourworld.controller.fragments.SignInFragment;
import com.example.hartshteinma.eatyourworld.model.Model;
import com.example.hartshteinma.eatyourworld.model.Recipe;
import com.example.hartshteinma.eatyourworld.model.User;
import com.example.hartshteinma.eatyourworld.model.interfaces.DownloadListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.EditListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.LoginListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.RecipesListListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.RegisterListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.RemoveRecipeListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.UploadListener;
import com.firebase.client.Firebase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity
{
    private MainFragment mainFragment;
    private SignInFragment signInFragment;
    private RegisterFragment registerFragment;
    private RecipesListFragment recipesListFragment;
    private NewRecipeFragment newRecipeFragment;
    private RecipeDetailsFragment recipeDetailsFragment;
    private EditRecipeFragment editRecipeFragment;
    private List<Fragment> allFragments;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(MainActivity.this);
        initWidgets();
        initFragments();
        initActionBar();
        updateMainFragmentByCurrentUser();
    }

    private void updateMainFragmentByCurrentUser()
    {
        User currentUser = Model.getInstance().getCurrentUser();
        if (currentUser != null)
        {
            Model.getInstance().setUser(currentUser);
            switchToFragment(recipesListFragment);
            recipesListFragment.refreshListView();
            newRecipeFragment.setCurrentUserId(currentUser.getUserId());
        }
    }

    private void initWidgets()
    {
        this.spinner = (ProgressBar) findViewById(R.id.spinner);
        Model.getInstance().setModelContext(this);
    }

    private void initActionBar()
    {
        ActionBar actionBar = getActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initFragments()
    {
        this.allFragments = new ArrayList<>();
        this.mainFragment = new MainFragment();
        this.allFragments.add(this.mainFragment);
        this.signInFragment = new SignInFragment();
        this.allFragments.add(this.signInFragment);
        this.registerFragment = new RegisterFragment();
        this.allFragments.add(this.registerFragment);
        this.recipesListFragment = new RecipesListFragment();
        this.allFragments.add(this.recipesListFragment);
        this.newRecipeFragment = new NewRecipeFragment();
        this.allFragments.add(this.newRecipeFragment);
        this.recipeDetailsFragment = new RecipeDetailsFragment();
        this.allFragments.add(this.recipeDetailsFragment);
        this.editRecipeFragment = new EditRecipeFragment();
        this.allFragments.add(this.editRecipeFragment);
        addAllFragments();
        initFragmentsListeners();
        switchToFragment(mainFragment);
    }

    private void addAllFragments()
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        for (Fragment fragment : this.allFragments)
        {
            transaction.add(R.id.main_container, fragment);
        }
        transaction.commit();
    }

    private void initFragmentsListeners()
    {
        initMainFragmentDelegate();
        initSignInFragmentDelegate();
        initRegisterFragmentDelegate();
        initNewRecipeFragmentDelegate();
        initRecipeDetailsFragmentDelegate();
        initRecipesListFragmentDelegate();
        initEditRecipeFragmentDelegate();
    }

    private void initEditRecipeFragmentDelegate()
    {
        editRecipeFragment.setDelegate(new NewRecipeFragment.Delegate()
        {
            @Override
            public void onSaveButtonClick(final Recipe recipe)
            {
                showSpinner();
                Model.getInstance().editRecipe(recipe, new EditListener()
                {
                    @Override
                    public void onEditFinished(boolean success, String errorMsg)
                    {
                        if (success)
                        {
                            Toast.makeText(MainActivity.this, "Changes saved", Toast.LENGTH_SHORT).show();
                            hideSpinner();
                            switchToFragment(recipeDetailsFragment);
                            recipeDetailsFragment.displayRecipeDetails(recipe);
                        }
                        else
                            Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancelButtonClick()
            {
                Toast.makeText(MainActivity.this, "Edit canceled", Toast.LENGTH_SHORT).show();
                switchToFragment(recipeDetailsFragment);
            }
        });
    }

    private void initRecipesListFragmentDelegate()
    {
        recipesListFragment.setDelegate(new RecipesListFragment.Delegate()
        {
            @Override
            public void onItemClicked(int position, final Recipe recipe)
            {
                switchToFragment(recipeDetailsFragment);
                if (recipeDetailsFragment.isFragmentCreated())
                    recipeDetailsFragment.displayRecipeDetails(recipe);
                else
                {
                    showSpinner();
                    recipeDetailsFragment.setFragmentCreationListener(new RecipeDetailsFragment.FragmentCreationListener()
                    {
                        @Override
                        public void onFragmentCreated()
                        {
                            recipeDetailsFragment.displayRecipeDetails(recipe);
                            hideSpinner();
                        }
                    });
                }
            }

            @Override
            public void onAddButtonClicked()
            {
                switchToFragment(newRecipeFragment);
                newRecipeFragment.clearScreen();
            }
        });
        recipesListFragment.setRecipes(Model.getInstance().getRecipes());
        Model.getInstance().setRecipesListListener(new RecipesListListener()
        {
            @Override
            public void datasetChanged(List<Recipe> recipes)
            {
                recipesListFragment.refreshListView();
            }
        });
    }

    private void initRecipeDetailsFragmentDelegate()
    {
        recipeDetailsFragment.setDelegate(new RecipeDetailsFragment.Delegate()
        {
            @Override
            public void onDeletePressed(final Recipe recipe)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Are you sure you want to remove " + recipe.getName() + " recipe?")
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Model.getInstance().removeRecipe(recipe, new RemoveRecipeListener()
                                {
                                    @Override
                                    public void onRecipeRemoved(boolean success, String errorMsg)
                                    {
                                        if (success)
                                        {
                                            Toast.makeText(MainActivity.this, "Recipe deleted", Toast.LENGTH_SHORT).show();
                                            switchToFragment(recipesListFragment);
                                            recipesListFragment.refreshListView();
                                        }
                                        else
                                            Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).setPositiveButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(MainActivity.this, "Recipe " + recipe.getName() + " will not be removed", Toast.LENGTH_SHORT).show();
                    }
                }).create().show();
            }

            @Override
            public void onEditPressed(Recipe recipe, Bitmap recipeImage)
            {
                switchToFragment(editRecipeFragment);
                editRecipeFragment.displayRecipeEdit(recipe, recipeImage);
            }

            @Override
            public boolean isOwner(Recipe recipe)
            {
                User currentUser = Model.getInstance().getUser();
                if (currentUser == null || currentUser.getUserId() == null || recipe == null)
                    return false;
                return currentUser.getUserId().equals(recipe.getUserId());
            }
        });
    }

    private void initNewRecipeFragmentDelegate()
    {
        newRecipeFragment.setDelegate(new NewRecipeFragment.Delegate()
        {
            @Override
            public void onSaveButtonClick(Recipe recipe)
            {
                Model.getInstance().addRecipe(recipe, new UploadListener()
                {
                    @Override
                    public void onRecipeAdded(boolean success, String errorMsg)
                    {
                        if (success)
                        {
                            Toast.makeText(MainActivity.this, "Recipe added", Toast.LENGTH_SHORT).show();
                            switchToFragment(recipesListFragment);
                            recipesListFragment.refreshListView();
                        }
                        else
                            Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancelButtonClick()
            {
                switchToFragment(recipesListFragment);
                recipesListFragment.refreshListView();
            }
        });
    }

    private void initRegisterFragmentDelegate()
    {
        registerFragment.setDelegate(new RegisterFragment.Delegate()
        {
            @Override
            public void onRegisterButtonClick(final User user)
            {
                showSpinner();
                Model.getInstance().register(user, new RegisterListener()
                {
                    @Override
                    public void onRegisterFinished(boolean register, String errorMsg)
                    {
                        if (register)
                        {
                            Model.getInstance().setUser(user);
                            newRecipeFragment.setCurrentUserId(user.getUserId());
                            switchToFragment(recipesListFragment);
                            recipesListFragment.refreshListView();
                            Model.getInstance().setUser(user);
                        }
                        else
                            Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        hideSpinner();
                    }
                });
            }
        });
    }

    private void initSignInFragmentDelegate()
    {
        signInFragment.setDelegate(new SignInFragment.Delegate()
        {
            @Override
            public void onSignInPressed(final String email, String password)
            {
                showSpinner();
                Model.getInstance().login(email, password, new LoginListener()
                {
                    @Override
                    public void onLoginFinished(boolean login, String errorMsg)
                    {
                        if (login)
                        {
                            Model.getInstance().setUserByEmail(email, new DownloadListener()
                            {
                                @Override
                                public void onDownloadFinished(User user)
                                {
                                    newRecipeFragment.setCurrentUserId(user.getUserId());
                                    Model.getInstance().setUser(user);
                                }
                            });
                            switchToFragment(recipesListFragment);
                            recipesListFragment.refreshListView();
                        }
                        else
                            Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        hideSpinner();
                    }
                });
            }
        });
    }

    private void initMainFragmentDelegate()
    {
        mainFragment.setDelegate(new MainFragment.Delegate()
        {
            @Override
            public void onSignInPressed()
            {
                switchToFragment(signInFragment);
            }

            @Override
            public void onRegisterPressed()
            {
                switchToFragment(registerFragment);
            }

        });
    }

    private void hideSpinner()
    {
        this.spinner.setVisibility(View.INVISIBLE);
    }

    private void showSpinner()
    {
        this.spinner.setVisibility(View.VISIBLE);
    }

    private void switchToFragment(Fragment fragment)
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        for (Fragment frag : this.allFragments)
        {
            transaction.hide(frag);
        }
        transaction.show(fragment);
        transaction.commit();
    }

    private void backButton()
    {
        if (mainFragment.isVisible())
        {
            super.onBackPressed();
        }
        else if (newRecipeFragment.isVisible())
        {
            switchToFragment(recipesListFragment);
        }
        else if (recipeDetailsFragment.isVisible())
        {
            switchToFragment(recipesListFragment);
        }
        else if (recipesListFragment.isVisible())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure you want to disconnect?")
                    .setNegativeButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            switchToFragment(mainFragment);
                        }
                    }).setPositiveButton("No", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                }
            }).show();
        }
        else if (registerFragment.isVisible())
        {
            switchToFragment(mainFragment);
        }
        else if (signInFragment.isVisible())
        {
            switchToFragment(mainFragment);
        }
        else if (editRecipeFragment.isVisible())
        {
            switchToFragment(recipeDetailsFragment);
        }
    }

    @Override
    public void onBackPressed()
    {
        backButton();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                backButton();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
