package com.supcom.agritrade;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    Button b1;
    EditText ed1, ed2, edN, edP, local;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        b1 = (Button) findViewById(R.id.button);
        edN = (EditText) findViewById(R.id.nom);
        edP = (EditText) findViewById(R.id.prenom);
        local = (EditText) findViewById(R.id.local);
        ed1 = (EditText) findViewById(R.id.editText);
        ed2 = (EditText) findViewById(R.id.editText2);

        auth = FirebaseAuth.getInstance();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = ed1.getText().toString();
                final String password = ed2.getText().toString();


                auth = FirebaseAuth.getInstance();

                final FirebaseFirestore db = FirebaseFirestore.getInstance();

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            intent.putExtra("email", email);
                            intent.putExtra("password", password);
                            startActivity(intent);
                            Map<String, Object> user = new HashMap<>();
                            user.put("name", edN.getText().toString());
                            user.put("prénom", edP.getText().toString());
                            user.put("email", ed1.getText().toString());
                            user.put("Localisation", local.getText().toString());
                            user.put("UserID", mAuth.getCurrentUser().getUid());
                            user.put("Stars", "0.0");
                            user.put("nbRatings", "0");

                            Date currentTime = Calendar.getInstance().getTime();
                            user.put("RegistrationDate", DateFormat.getDateInstance(DateFormat.FULL).format(currentTime));

                            db.collection("users").document(mAuth.getCurrentUser().getUid())
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });

                        }
                    }
                });





            }
        });

    }
}