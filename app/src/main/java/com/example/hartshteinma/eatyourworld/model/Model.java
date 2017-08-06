package com.example.hartshteinma.eatyourworld.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.hartshteinma.eatyourworld.model.interfaces.AuthManager;
import com.example.hartshteinma.eatyourworld.model.interfaces.CloudManager;
import com.example.hartshteinma.eatyourworld.model.interfaces.DownloadListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.DownloadRecipeListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.EditListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.GetImageListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.ImagesLoader;
import com.example.hartshteinma.eatyourworld.model.interfaces.LocalManager;
import com.example.hartshteinma.eatyourworld.model.interfaces.LoginListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.RecipesListListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.RegisterListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.RemoveImageListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.RemoveRecipeListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.SaveImageListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.UploadListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hartshteinma on 26/07/2017.
 */

public class Model
{
    final private static Model instance = new Model();
    private CloudManager cloudManager;
    private AuthManager authManager;
    private ImagesLoader imagesLoader;
    private LocalManager localManager;
    private List<Recipe> recipes;
    private RecipesListListener recipesListListener;
    private User user;

    private Model()
    {
        init();
    }

    // TODO: 05/08/2017 add countries loader
    private void init()
    {
        ModelFirebase modelFirebase = new ModelFirebase();
        this.authManager = modelFirebase;
        this.cloudManager = modelFirebase;
        this.imagesLoader = modelFirebase;
        this.recipes = new ArrayList<>();
        this.cloudManager.getAllRecipes(new DownloadRecipeListener()
        {
            @Override
            public void onDownloadFinished(List<Recipe> recipes)
            {
                setRecipes(recipes);
                if (recipesListListener != null)
                {
                    recipesListListener.datasetChanged(recipes);
                }
            }
        });
        this.user = null;
    }

    public void setModelContext(Context context)
    {
        this.localManager = new ModelSQL(context);
    }

    private void setRecipes(List<Recipe> recipes)
    {
        this.recipes.clear();
        this.recipes.addAll(recipes);
    }

    public static Model getInstance()
    {
        return instance;
    }

    public void addRecipe(Recipe recipe, UploadListener uploadListener)
    {
        this.cloudManager.addRecipe(recipe, uploadListener);
    }

    public void login(String email, String password, LoginListener loginListener)
    {
        this.authManager.login(email, password, loginListener);
    }

    public void register(User user, RegisterListener registerListener)
    {
        this.authManager.register(user, registerListener);
    }

    public void removeRecipe(Recipe recipe, RemoveRecipeListener removeRecipeListener)
    {
        this.cloudManager.removeRecipe(recipe, removeRecipeListener);
    }

    public void editRecipe(Recipe recipe, EditListener editListener)
    {
        this.cloudManager.editRecipe(recipe, editListener);
    }

    public List<Recipe> getRecipes()
    {
        return this.recipes;
    }

    public void setRecipesListListener(RecipesListListener recipesListListener)
    {
        this.recipesListListener = recipesListListener;
    }

    public void setUser(User user)
    {
        this.user = user;
        this.localManager.setUser(user);
    }

    public User getUser()
    {
        return this.user;
    }

    public void setUserByEmail(String email, final DownloadListener downloadListener)
    {
        this.authManager.getUserByEmail(email, new DownloadListener()
        {
            @Override
            public void onDownloadFinished(User user)
            {
                setUser(user);
                downloadListener.onDownloadFinished(user);
            }
        });
    }

    public void removeImage(String url, RemoveImageListener listener)
    {
        this.imagesLoader.removeImage(url, listener);
    }

    public void getImage(String url, GetImageListener listener)
    {
        this.imagesLoader.getImage(url, listener);
    }

    public void saveImage(Bitmap imageBmp, String name, SaveImageListener listener)
    {
        this.imagesLoader.saveImage(imageBmp, name, listener);
    }

    public void dropTable()
    {
        this.localManager.dropTable();
    }

    public User getCurrentUser()
    {
        return this.localManager.getCurrentUser();
    }
}
