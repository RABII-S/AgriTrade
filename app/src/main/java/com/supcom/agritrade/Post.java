package com.supcom.agritrade;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.ProgressDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Post extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 22;
    private Button btnChoose, Post;
    private ImageView imageView;

    private Uri filePath;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();





        final EditText Type = (EditText) findViewById(R.id.Type);
        final EditText Price = (EditText) findViewById(R.id.Price);
        final EditText Description = (EditText) findViewById(R.id.Description);
         btnChoose = (Button) findViewById(R.id.btnChoose);
         imageView = (ImageView) findViewById(R.id.imgView);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
         Post = (Button) findViewById(R.id.Post);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();


            }
        });





        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                    if(filePath != null) {
                        Toast.makeText(Post.this, filePath.toString(), Toast.LENGTH_SHORT).show();
                        pd.show();

                        StorageReference childRef = storageRef.child("image.jpg");

                        //uploading the image
                        UploadTask uploadTask = childRef.putFile(filePath);

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                pd.dismiss();
                                Toast.makeText(Post.this, "Upload successful", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(Post.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        Toast.makeText(Post.this, "Select an image", Toast.LENGTH_SHORT).show();
                    }
*/
                uploadImage();
                Map<String, Object> Post = new HashMap<>();
                Post.put("Type", Type.getText().toString());
                Post.put("Price", Price.getText().toString());
                Post.put("Description", Description.getText().toString());
                Post.put("UserID", currentUser.getUid());

                db.collection("Posts").document()
                        .set(Post)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Posted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Post.this, Feed.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


            }
        });
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
             filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void uploadImage() {
        if (filePath != null) {
            Toast.makeText(Post.this, "haha" + filePath.toString(), Toast.LENGTH_SHORT).show();
            // Code for showing progressDialog while uploading


            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + filePath);

            // adding listeners on upload
            // or failure of image

            ref.putFile(filePath).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast
                            .makeText(Post.this,
                                    "Failed ",
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast
                            .makeText(Post.this,
                                    "Image Uploaded!!",
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            });

        } else {
            Toast.makeText(Post.this, "reeeeeeeeeer" + filePath.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}