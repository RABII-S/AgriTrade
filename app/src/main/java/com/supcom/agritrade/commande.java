package com.supcom.agritrade;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
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

    private static final String TAG = "ExamplesActivity";
    Button envoyer,back;
    FloatingActionButton adr;
    EditText editName, Mobile, quantite;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande);
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.command));
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ImageView img = (ImageView) findViewById(R.id.imagecom);

        postData = (PostData) getIntent().getSerializableExtra("postD");
        db1 = FirebaseFirestore.getInstance();
        im = getIntent().getStringExtra("image");
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(im).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(commande.this)
                        .load(uri)
                        .centerCrop()
                        .into(img);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        envoyer = (Button) findViewById(R.id.envoyer22);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(commande.this, Feed.class);
                startActivity(intent);
            }
        });
        adr = (FloatingActionButton) findViewById(R.id.map);
        adresse = (EditText) findViewById(R.id.adressey);

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

        editName = (EditText) findViewById(R.id.editNamey);
        Mobile = (EditText) findViewById(R.id.Mobile);
        quantite = (EditText) findViewById(R.id.quantitey);
        Date currentTime = Calendar.getInstance().getTime();
        final String formattedDta = DateFormat.getDateInstance(DateFormat.MEDIUM).format(currentTime);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> submit = new HashMap<>();
                submit.put("name", editName.getText().toString());
                submit.put("tel", Mobile.getText().toString());
                submit.put("quantite", quantite.getText().toString());
                submit.put("Date", formattedDta);
                submit.put("adresse", adresse.getText().toString());
                submit.put("image", im);
                submit.put("Type", postData.getType());
                submit.put("Price", postData.getPrice());
                submit.put("unite", postData.getUnite());
                submit.put("posterID", postData.getPosterID());
                Integer fdd = Integer.parseInt(quantite.getText().toString()) * Integer.parseInt(postData.getPrice());
                submit.put("TotalPrice", fdd.toString());
                submit.put("UserID", currentUser.getUid());
                submit.put("ratingState", false);


                final DocumentReference docRef = FirebaseFirestore.getInstance().collection("Posts").document(postData.getId());
                Map<String, Object> map = new HashMap<>();

                Integer n = Integer.parseInt(quantite.getText().toString());

                Integer currentN = Integer.parseInt(postData.getDescription());
                if (n <= currentN) {

                    map.put("Description", currentN - n);
                    docRef.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "on Success");
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "Onfailure", e);
                                }
                            });

                    db.collection("commandes").document()
                            .set(submit)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "une quantitée de " + quantite.getText().toString() + " a été commandée", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(commande.this, Feed.class);
                                    startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    Toast.makeText(getApplicationContext(), "La quantité ordonnée est supérieur a la quantitée disponible", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

                Location location = task.getResult();

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