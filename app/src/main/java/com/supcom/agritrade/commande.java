package com.supcom.agritrade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;



public class commande extends AppCompatActivity {

    private static final String TAG ="ExamplesActivity";
    Button envoyer, adr;
    EditText editName,  Mobile, quantite;
    EditText adresse;
    PostData postData;
    FirebaseFirestore db1;
    FusedLocationProviderClient fusedLocationProviderClient;
    String im;
    @GlideModule
    public class MyAppGlideModule extends AppGlideModule {

        @Override
        public void registerComponents(Context context, Glide glide, Registry registry) {
            // Register FirebaseImageLoader to handle StorageReference
            registry.append(StorageReference.class, InputStream.class,
                    new FirebaseImageLoader.Factory());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ImageView img=(ImageView)findViewById(R.id.imagecom);

         postData=(PostData) getIntent().getSerializableExtra("postD");
         db1=FirebaseFirestore.getInstance();

        im=getIntent().getStringExtra("image");
        Toast.makeText(getApplicationContext(), im, Toast.LENGTH_SHORT).show();
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(im).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(commande.this)
                        .load(uri)
                        .into(img);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        envoyer = (Button) findViewById(R.id.envoyer22);
        adr = (Button) findViewById(R.id.map);
        adresse = (EditText) findViewById(R.id.adresse);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        adr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(commande.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();


                } else {
                    ActivityCompat.requestPermissions(commande.this
                            , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

        editName = (EditText) findViewById(R.id.editName);
        Mobile = (EditText) findViewById(R.id.Mobile);
        quantite = (EditText) findViewById(R.id.quantite);
        Date currentTime = Calendar.getInstance().getTime();
        final String formattedDta = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {

                Map<String, Object> submit = new HashMap<>();
                submit.put("name", editName.getText().toString());
                submit.put("tel", Mobile.getText().toString());
                submit.put("quantite", quantite.getText().toString());
                submit.put("date", formattedDta);
                submit.put("adresse",adresse.getText().toString());

                submit.put("UserID", currentUser.getUid());

                db.collection("commandes").document()
                        .set(submit)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Posted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(commande.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        final DocumentReference docRef =FirebaseFirestore.getInstance().collection("Posts").document(postData.getId());
        Map<String,Object> map=new HashMap<>();

        String n="120kg";
       map.put("Description",n);

        docRef.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG,"on Success");
            }
        })
          .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                  Log.e(TAG,"Onfailure",e);
              }
          });


    }

    private void getLocation() {
        Toast.makeText(getApplicationContext(), "yatik bechla", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "hahfzere", Toast.LENGTH_SHORT).show();
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Toast.makeText(getApplicationContext(), "naje7", Toast.LENGTH_SHORT).show();
                Location location = task.getResult();
                Toast.makeText(getApplicationContext(), location.toString(), Toast.LENGTH_SHORT).show();
                if (location != null) {
                //    Toast.makeText(getApplicationContext(), location.toString(), Toast.LENGTH_SHORT).show();

                    try {
                        Geocoder geocoder = new Geocoder(commande.this,
                                Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        adresse.setText(Html.fromHtml("<font color='#6200EE'><b>Addresses:</b><br></font>"
                                + addresses.get(0).getAddressLine(0)));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}