package com.example.hartshteinma.eatyourworld.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.hartshteinma.eatyourworld.model.interfaces.AuthManager;
import com.example.hartshteinma.eatyourworld.model.interfaces.CloudManager;
import com.example.hartshteinma.eatyourworld.model.interfaces.DownloadListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.DownloadRecipeListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.EditListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.GetImageListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.ImagesLoader;
import com.example.hartshteinma.eatyourworld.model.interfaces.LoginListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.RegisterListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.RemoveListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.SaveImageListener;
import com.example.hartshteinma.eatyourworld.model.interfaces.UploadListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hartshteinma on 26/07/2017.
 */

public class ModelFirebase implements CloudManager, AuthManager, ImagesLoader {
    private DatabaseReference recipesFirebase, usersFirebase;
    private FirebaseAuth mAuth;
    private String RECIPES_PATH = "recipes";
    private String USERS_PATH = "users";

    public ModelFirebase() {
        this.recipesFirebase = FirebaseDatabase.getInstance().getReference(RECIPES_PATH);
        this.usersFirebase = FirebaseDatabase.getInstance().getReference(USERS_PATH);
        this.mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void login(String email, String password, final LoginListener loginListener) {
        //"mayhart111@gmail.com", "123456"
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                loginListener.onLoginFinished(task.isSuccessful());
            }
        });
    }

    @Override
    public void register(final User user, final RegisterListener registerListener) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            usersFirebase.child(user.getUserId()).setValue(user, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    registerListener.onRegisterFinished(task.isSuccessful());
                                }
                            });
                        }
                    }
                });
    }

    @Override
    public void getUserByEmail(final String email, final DownloadListener downloadListener) {
        this.usersFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> users = dataSnapshot.getChildren();
                for (DataSnapshot userSnapshot : users) {
                    if (userSnapshot.child("email").getValue(String.class).equals(email)) {
                        downloadListener.onDownloadFinished(userSnapshot.getValue(User.class));
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void addRecipe(Recipe recipe, final UploadListener uploadListener) {

        this.recipesFirebase.child(recipe.getUserId()).child(recipe.getRecipeId()).setValue(recipe, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Log.d("MAMAMA", "onComplete: uploadListener=" + uploadListener + "," + databaseError + "=" + databaseError);
                if (uploadListener != null) {
                    uploadListener.onRecipeAdded(databaseError);
                }
            }
        });
    }

    @Override
    public void removeRecipe(Recipe recipe, final RemoveListener removeListener) {
        this.recipesFirebase.child(recipe.getUserId()).child(recipe.getRecipeId()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                removeListener.onRecipeRemoved(databaseError.toException());
            }
        });
    }

    @Override
    public void editRecipe(Recipe recipe, final EditListener editListener) {
        this.recipesFirebase.child(recipe.getUserId()).child(recipe.getRecipeId()).setValue(recipe, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                editListener.onEditFinished(firebaseError.toException());
            }
        });
    }

    @Override
    public void getAllRecipes(final DownloadRecipeListener downloadListener) {
        this.recipesFirebase.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                List<Recipe> recipes = new ArrayList<Recipe>();
                Iterable<com.google.firebase.database.DataSnapshot> users = dataSnapshot.getChildren();
                for (com.google.firebase.database.DataSnapshot userSnapshot : users) {
                    Iterable<com.google.firebase.database.DataSnapshot> recipesSnapshots = userSnapshot.getChildren();
                    for (com.google.firebase.database.DataSnapshot recipeSnapshot : recipesSnapshots) {
                        recipes.add(recipeSnapshot.getValue(Recipe.class));
                    }
                }
                downloadListener.onDownloadFinished(recipes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getRecipesByUser(String userId, final DownloadRecipeListener downloadListener) {
        this.recipesFirebase.child(userId).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                Iterable<com.google.firebase.database.DataSnapshot> children = dataSnapshot.getChildren();
                List<Recipe> recipes = new ArrayList<Recipe>();
                for (com.google.firebase.database.DataSnapshot snapshot : children) {
                    Recipe recipe = snapshot.getValue(Recipe.class);
                    recipes.add(recipe);
                }
                downloadListener.onDownloadFinished(recipes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void saveImage(Bitmap imageBmp, String name, final SaveImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imagesRef = storage.getReference().child("images").child(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.fail();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                listener.complete(downloadUrl.toString());
            }
        });
    }

    @Override
    public void getImage(String url, final GetImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(url);
        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(3 * ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                listener.onSccess(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                Log.d("TAG", exception.getMessage());
                listener.onFail();
            }
        });
    }
}
