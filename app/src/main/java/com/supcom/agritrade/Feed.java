package com.supcom.agritrade;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.android.material.navigation.NavigationView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class Feed extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView rv;
    private ImageView f1,f2;
    ArrayList<PostData> contacts = new ArrayList<>();
    final FeedAdapter adapter = new FeedAdapter( this);
    private TextView txt;
    public int x=0,i=0;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final FirebaseUser currentUser = mAuth.getCurrentUser();
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    @SuppressLint("ResourceType")
    public void onClick(View v){
        switch (v.getId()){
            case R.id.gotopost:
                Intent intent = new Intent(Feed.this, Post.class);
                startActivity(intent);
                break;
            case R.id.flesh2:
                x++;
            case R.id.flesh1:
                x++;
                x %= 3;
                contacts.clear();
                CollectionReference query = db.collection("Posts");
                CollectionReference query2 = db.collection("commandes");
                String[] S = getResources().getStringArray(R.array.ghala);
                if (i == 1)
                    S = getResources().getStringArray(R.array.batata);
                else if (i == 2)
                    S = getResources().getStringArray(R.array.autre);
                if (x == 1) {
                    txt.setText("MY ORDERS");
                    query2.whereEqualTo("UserID", currentUser.getUid());
                    query2.whereIn("Type", Arrays.asList(S))
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            PostData p = new PostData(document.getData().get("Type").toString(), document.getData().get("TotalPrice").toString(),
                                                    document.getData().get("quantite").toString(), document.getData().get("image").toString(),
                                                    document.getData().get("unite").toString(), document.getData().get("Date").toString());
                                            p.setId(document.getId());
                                            contacts.add(p);
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                }
                else if(x==2) {
                    txt.setText("MY POSTS");
                    query.whereEqualTo("UserID",currentUser.getUid());
                    query.whereIn("Type", Arrays.asList(S))
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            PostData p = new PostData(document.getData().get("Type").toString(), document.getData().get("Price").toString(),
                                                    document.getData().get("Description").toString(), document.getData().get("image").toString(),
                                                    document.getData().get("unite").toString(), document.getData().get("Date").toString());
                                            p.setId(document.getId());
                                            contacts.add(p);
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                }
                else {
                    txt.setText("PUBLICATIONS");
                    query.whereIn("Type", Arrays.asList(S))
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                            PostData p = new PostData(document.getData().get("Type").toString(), document.getData().get("Price").toString(),
                                                    document.getData().get("Description").toString(), document.getData().get("image").toString(),
                                                    document.getData().get("unite").toString(), document.getData().get("Date").toString());
                                            p.setId(document.getId());
                                            contacts.add(p);
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                }
                break;
            default:
                break;

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        rv = findViewById(R.id.recyclerview);
        adapter.setCaptions(contacts);
        rv.setAdapter(adapter);
        FloatingActionButton fab = findViewById(R.id.gotopost);
        fab.setOnClickListener(this);
        ArrayList<String> L=new ArrayList<>();L.add("tofeh");
        CollectionReference query=db.collection("Posts");
        query.whereIn("Type", Arrays.asList(getResources().getStringArray(R.array.ghala)))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                PostData p = new PostData(document.getData().get("Type").toString(), document.getData().get("Price").toString(),
                                        document.getData().get("Description").toString(), document.getData().get("image").toString(),
                                        document.getData().get("unite").toString(), document.getData().get("Date").toString());
                                p.setId(document.getId());
                                contacts.add(p);
                                adapter.notifyDataSetChanged();

                            }
                        }
                    }
                });
        /*
        PostData.AddToContactsList(p);
        contacts = PostData.getContactsList();
        */
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        txt=findViewById(R.id.textView);
        f1=findViewById(R.id.flesh1);
        f1.setOnClickListener(Feed.this);
        f2=findViewById(R.id.flesh2);
        f2.setOnClickListener(Feed.this);
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.bringToFront();
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                CollectionReference query=db.collection("Posts");
                CollectionReference query2 = db.collection("commandes");

                String[] SS=new String[18];
                contacts.clear();
                switch (item.getItemId()) {
                    case R.id.fruitemenu:
                        i=0;
                        SS=getResources().getStringArray(R.array.ghala);
                        break;
                    case R.id.legumemenu:
                        i=1;
                        SS=getResources().getStringArray(R.array.batata);
                        break;
                    case R.id.autremenu:
                        i=2;
                        SS=getResources().getStringArray(R.array.autre);
                        break;
                }
                if(x==1) {
                    query2.whereEqualTo("UserID", currentUser.getUid());
                    query2.whereIn("Type", Arrays.asList(SS))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        PostData p = new PostData(document.getData().get("Type").toString(), document.getData().get("TotalPrice").toString(),
                                                document.getData().get("quantite").toString(), document.getData().get("image").toString(),
                                                document.getData().get("unite").toString(), document.getData().get("Date").toString());
                                        p.setId(document.getId());
                                        contacts.add(p);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                }
                else if(x==2) {
                    query.whereEqualTo("UserID",currentUser.getUid());
                    query.whereIn("Type", Arrays.asList(SS))
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            PostData p = new PostData(document.getData().get("Type").toString(), document.getData().get("Price").toString(),
                                                    document.getData().get("Description").toString(), document.getData().get("image").toString(),
                                                    document.getData().get("unite").toString(), document.getData().get("Date").toString());
                                            p.setId(document.getId());
                                            contacts.add(p);
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                }
                else {
                    query.whereIn("Type", Arrays.asList(SS))
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                            PostData p = new PostData(document.getData().get("Type").toString(), document.getData().get("Price").toString(),
                                                    document.getData().get("Description").toString(), document.getData().get("image").toString(),
                                                    document.getData().get("unite").toString(), document.getData().get("Date").toString());
                                            p.setId(document.getId());
                                            contacts.add(p);
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                }
                return true;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.lol:
                Intent intent = new Intent(Feed.this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.profile:
                Toast.makeText(getApplicationContext(), "still not created", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}