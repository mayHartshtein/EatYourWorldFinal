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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hartshteinma.eatyourworld.R;
import com.example.hartshteinma.eatyourworld.controller.Constants;
import com.example.hartshteinma.eatyourworld.model.Model;
import com.example.hartshteinma.eatyourworld.model.Recipe;
import com.example.hartshteinma.eatyourworld.model.interfaces.SaveImageListener;

import java.io.IOException;
import java.util.Calendar;

public class EditRecipeFragment extends Fragment implements View.OnClickListener
{
    private EditText countryEditText, nameEditText, detailsEditText;
    private Button saveButton, cancelButton;
    private ImageView addImageButton;
    private NewRecipeFragment.Delegate delegate;
    private String userChoosenTask;
    private Bitmap currentBitmap;
    private Recipe recipe;

    public void displayRecipeEdit(Recipe recipe, Bitmap recipeImage)
    {
        if (recipe != null)
        {
            this.recipe = recipe;
            this.currentBitmap = recipeImage;
            if (recipeImage != null)
            {
                this.addImageButton.setImageBitmap(recipeImage);
            }
            else
                this.addImageButton.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.picfood));
            this.countryEditText.setText(recipe.getCountry());
            this.nameEditText.setText(recipe.getName());
            this.detailsEditText.setText(recipe.getDetails());
        }
    }

    public interface Delegate
    {
        void onSaveButtonClick(Recipe recipe);

        void onCancelButtonClick();
    }

    public void setDelegate(NewRecipeFragment.Delegate delegate)
    {
        this.delegate = delegate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_edit_recipe, container, false);
        initWidgets(view);
        return view;
    }

    private void initWidgets(View view)
    {
        this.addImageButton = (ImageView) view.findViewById(R.id.recipe_imageView);
        this.addImageButton.setOnClickListener(this);
        this.countryEditText = (EditText) view.findViewById(R.id.country_editText);
        this.nameEditText = (EditText) view.findViewById(R.id.recipe_name_editText);
        this.detailsEditText = (EditText) view.findViewById(R.id.recipe_details_editText);

        this.saveButton = (Button) view.findViewById(R.id.save_button);
        this.saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getRecipeFromWidgets(new NewRecipeFragment.GeneratingRecipeListener()
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

    private void getRecipeFromWidgets(final NewRecipeFragment.GeneratingRecipeListener recipeListener)
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

    private Recipe generateRecipeFromWidgets(String imageSrc)
    {
        recipe.setCountry(countryEditText.getText().toString());
        recipe.setDetails(detailsEditText.getText().toString());
        recipe.setImgSrc(imageSrc);
        recipe.setName(nameEditText.getText().toString());
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
                //boolean result = Utilities.checkPermission(getActivity());

                if (items[item].equals("Take Photo"))
                {
                    userChoosenTask = "Take Photo";
                    //if (result)
                    cameraIntent();

                }
                else if (items[item].equals("Choose from Library"))
                {
                    userChoosenTask = "Choose from Library";
                    //if (result)
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
        startActivityForResult(intent, Constants.REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), Constants.SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        if (resultCode == Activity.RESULT_OK)
        {
            switch (requestCode)
            {
                case Constants.SELECT_FILE:
                    onSelectFromGalleryResult(data);
                    break;
                case Constants.REQUEST_CAMERA:
                    onCaptureImageResult(data);
                    break;
            }
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

        //Uri tempUri = getImageUri(getActivity(), thumbnail);
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        if (thumbnail != null) {
//            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//        }
//
//
//        File destination = new File(Environment.getExternalStorageDirectory(),
//                          System.currentTimeMillis() + ".jpg");
//
//        FileOutputStream fo;
//        try {
//            destination.createNewFile();
//            fo = new FileOutputStream(destination);
//            fo.write(bytes.toByteArray());
//            fo.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Bitmap rotatedThumbnail=null;
//
//        try {
//            rotatedThumbnail=rotateImageIfRequired(thumbnail,tempUri);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        addImageButton.setImageBitmap(thumbnail);
        this.currentBitmap = thumbnail;
        Log.d("NGNGNG", "onCaptureImageResult: this.currentBitmap = " + this.currentBitmap);
    }
}
