package com.supcom.agritrade;

import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class rate extends AppCompatActivity {
    PostData postData;
    String s, fs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        postData = (PostData) getIntent().getSerializableExtra("postD");
        final RatingBar ratingbar;
        Button button;
        button = (Button) findViewById(R.id.Rate);
        ratingbar = (RatingBar) findViewById(R.id.ratingBar);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Getting the rating and displaying it on the toast
                s = String.valueOf(ratingbar.getRating());
                Map<String, Object> map = new HashMap<>();
                float f1 = Float.parseFloat(s);
                float f2 = Float.parseFloat(postData.getPosterStars());
                float f3 = Float.parseFloat(postData.getnbRatings());
                float f4 = (f1 * f3 + f2) / (f3 + 1);
                map.put("Stars", String.valueOf(f4));
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                final DocumentReference docRef = db.collection("users").document(postData.getPosterID());
                docRef.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

            }

        });

    }
}