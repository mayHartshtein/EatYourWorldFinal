package com.example.hartshteinma.eatyourworld.controller.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hartshteinma.eatyourworld.R;
import com.example.hartshteinma.eatyourworld.model.Model;
import com.example.hartshteinma.eatyourworld.model.Recipe;
import com.example.hartshteinma.eatyourworld.model.interfaces.SaveImageListener;

import java.io.IOException;
import java.util.Calendar;

public class NewRecipeFragment extends Fragment implements View.OnClickListener
{
    private AutoCompleteTextView countrySpinner;
    private EditText nameEditText;
    private EditText detailsEditText;
    private Button saveButton, cancelButton;
    private ImageView addImageButton;
    private Delegate delegate;
    private String userChoosenTask;
    private Bitmap currentBitmap;
    private String currentUserId;

    public NewRecipeFragment()
    {
    }

    public void setCurrentUserId(String currentUserId)
    {
        this.currentUserId = currentUserId;
    }

    public interface Delegate
    {
        void onSaveButtonClick(Recipe recipe);

        void onCancelButtonClick();
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_new_recipe, container, false);
        initWidgets(view);
        return view;
    }

    private void initWidgets(View view)
    {
        this.addImageButton = (ImageView) view.findViewById(R.id.recipe_imageView);
        this.addImageButton.setOnClickListener(this);

        this.countrySpinner = (AutoCompleteTextView) view.findViewById(R.id.country_name_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                getActivity().getResources().getStringArray(R.array.countries));
        this.countrySpinner.setAdapter(adapter);

        this.nameEditText = (EditText) view.findViewById(R.id.recipe_name_editText);

        this.detailsEditText = (EditText) view.findViewById(R.id.recipe_details_editText);

        this.saveButton = (Button) view.findViewById(R.id.save_button);
        this.saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getRecipeFromWidgets(new GeneratingRecipeListener()
                {
                    @Override
                    public void onRecipeGenerated(Recipe recipe)
                    {
                        delegate.onSaveButtonClick(recipe);
                    }
                });
            }
        });

        this.cancelButton = (Button) view.findViewById(R.id.cancel_button);
        this.cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                delegate.onCancelButtonClick();
            }
        });
    }

    interface GeneratingRecipeListener
    {
        void onRecipeGenerated(Recipe recipe);
    }

    private void getRecipeFromWidgets(final GeneratingRecipeListener recipeListener)
    {
        if (this.currentBitmap != null)
        {
            String imageName = this.nameEditText.getText().toString() + " - " + Calendar.getInstance().getTime().toString();
            Model.getInstance().saveImage(this.currentBitmap, imageName, new SaveImageListener()
            {
                @Override
                public void fail()
                {
                    Toast.makeText(getActivity(), "Image was not uploaded", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void complete(String s)
                {
                    recipeListener.onRecipeGenerated(generateRecipeFromWidgets(s));
                    Toast.makeText(getActivity(), "Image was uploaded", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            recipeListener.onRecipeGenerated(generateRecipeFromWidgets(null));
        }
    }

    public void clearScreen()
    {
        try
        {
            countrySpinner.setSelection(0);
            nameEditText.setText("");
            detailsEditText.setText("");
            addImageButton.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.picfood));
        }
        catch (Exception e)
        {
        }
    }

    private Recipe generateRecipeFromWidgets(String imageSrc)
    {
        Recipe recipe = new Recipe();
        recipe.setUserId(this.currentUserId);
        recipe.setCountry(countrySpinner.getText().toString());
        recipe.setDetails(detailsEditText.getText().toString());
        recipe.setImgSrc(imageSrc);
        recipe.setName(nameEditText.getText().toString());
        recipe.setRecipeId(String.valueOf(Calendar.getInstance().getTime()));
        return recipe;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.recipe_imageView:
                selectImage();
        }
    }

    private void selectImage()
    {
        userChoosenTask = null;
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {

                if (items[item].equals("Take Photo"))
                {
                    userChoosenTask = "Take Photo";
                    cameraIntent();

                }
                else if (items[item].equals("Choose from Library"))
                {
                    userChoosenTask = "Choose from Library";
                    galleryIntent();

                }
                else if (items[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, R.integer.request_camera);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), R.integer.select_file);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == R.integer.select_file)
                onSelectFromGalleryResult(data);
            else if (requestCode == R.integer.request_camera)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data)
    {

        Bitmap bm = null;
        if (data != null)
        {
            try
            {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        addImageButton.setImageBitmap(bm);
        this.currentBitmap = bm;
        Log.d("NGNGNG", "onSelectFromGalleryResult: this.currentBitmap = " + this.currentBitmap);
    }

    private void onCaptureImageResult(Intent data)
    {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        addImageButton.setImageBitmap(thumbnail);
        this.currentBitmap = thumbnail;
    }


}
