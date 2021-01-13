package com.supcom.agritrade;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Post extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 22;
    private Button btnChoose, Post;
    private ImageView imageView;
    private Spinner sp,spkg;
    private String Type,unite;
    Uri filePath;
    String path;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    ProgressDialog pd;
    private RadioGroup radio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final EditText Price = (EditText) findViewById(R.id.prixx);
        final EditText Description = (EditText) findViewById(R.id.ecrire);
        btnChoose = (Button) findViewById(R.id.upload);
        imageView = (ImageView) findViewById(R.id.taswira);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        Post = (Button) findViewById(R.id.post);
        final TextView ty = (TextView) findViewById(R.id.tx);

        final TextView pr = (TextView) findViewById(R.id.px);
        final TextView qan = (TextView) findViewById(R.id.qx);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        radio = (RadioGroup) findViewById(R.id.radiox);
        sp = (Spinner) findViewById(R.id.sp);
        spkg = (Spinner) findViewById(R.id.spkg);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.batata, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        sp.setAdapter(adapter);

        //  sp.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        Price.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                pr.setText(s.toString() + " DT/" + unite);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        Description.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                qan.setText(s.toString() + " " + unite);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int i = radio.getCheckedRadioButtonId();
                // get selected radio button from radioGroup
                int selectedId = radio.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                String S = (String) radioButton.getText();
                Resources res = getResources();
                String[] d = {};
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Post.this,
                        R.array.batata, android.R.layout.simple_spinner_item);
                ;
                if (S.equals("Legumes ")) {
                    adapter = ArrayAdapter.createFromResource(Post.this,
                            R.array.batata, android.R.layout.simple_spinner_item);

                } else {
                    if (S.equals("fruit ")) {

                        adapter = ArrayAdapter.createFromResource(Post.this,
                                R.array.ghala, android.R.layout.simple_spinner_item);


                    } else {
                        if (S.equals("autre")) {

                            adapter = ArrayAdapter.createFromResource(Post.this, R.array.autre, android.R.layout.simple_spinner_item);

// Apply the adapter to the spinner

                        }
                    }
                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp.setAdapter(adapter);
            }
        });

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Type = parent.getItemAtPosition(position).toString();
                ty.setText(Type);

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spkg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unite = parent.getItemAtPosition(position).toString();
                pr.setText("    DT/" + unite);
                qan.setText("    " + unite);
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressBar p = findViewById(R.id.prog);
                p.setVisibility(View.VISIBLE);
                Date currentTime = Calendar.getInstance().getTime();
                final String formattedDta = DateFormat.getDateInstance(DateFormat.MEDIUM).format(currentTime);
                uploadImage();
                Map<String, Object> Post = new HashMap<>();
                Post.put("Type", Type);
                Post.put("Price", Price.getText().toString());
                Post.put("Description", Description.getText().toString());
                Post.put("image", path);
                Post.put("Date",formattedDta);
                Post.put("unite",unite);
                Post.put("UserID", currentUser.getUid());

                db.collection("Posts").document()
                        .set(Post)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Posted", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                p.setVisibility(View.GONE);
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

            // Code for showing progressDialog while uploading

            path = "images/" + UUID.randomUUID().toString();
            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(path);

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
                    Intent intent = new Intent(Post.this, Feed.class);
                    startActivity(intent);


                }

            });

        } else {
            Toast.makeText(Post.this, "filepath NULL" + filePath.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
