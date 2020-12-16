package com.supcom.agritrade;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class Post extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final EditText Type = (EditText) findViewById(R.id.Type);
        final EditText Price = (EditText) findViewById(R.id.Price);
        final EditText Description = (EditText) findViewById(R.id.Description);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        Button Post = (Button) findViewById(R.id.Post);

        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
}