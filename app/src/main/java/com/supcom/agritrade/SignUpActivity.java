package com.supcom.agritrade;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {
    Button b1;
    EditText ed1,ed2;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        b1 = (Button)findViewById(R.id.button);

        ed1 = (EditText)findViewById(R.id.editText);
        ed2 = (EditText)findViewById(R.id.editText2);
        ed1.setText("rabibouhlel555@gmail.com");
        ed2.setText("123456");

        auth = FirebaseAuth.getInstance();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email=ed1.getText().toString();
                final String password=ed2.getText().toString();


                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registration Failed",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Registered",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                            intent.putExtra("email", email);
                            intent.putExtra("password", password);
                            startActivity(intent);

                        }
                    }
                });





            }
        });

    }
}