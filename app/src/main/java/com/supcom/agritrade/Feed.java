package com.supcom.agritrade;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Feed extends AppCompatActivity {
    private RecyclerView rv;
    private FeedAdapter adapter;
    private List<PostData> dataList;
    private PostData dope;


    ArrayList<PostData> contactsm = new ArrayList<PostData>();
    ArrayList<PostData> contacts = new ArrayList<PostData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        rv = (RecyclerView) findViewById(R.id.recyclerview);
        final FeedAdapter adapter = new FeedAdapter(contacts, getApplicationContext());
        rv.setAdapter(adapter);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();


        FloatingActionButton fab = findViewById(R.id.gotopost);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Feed.this, Post.class);
                startActivity(intent);
            }


        });

        db.collection("Posts")
                .whereIn("Type", Arrays.asList(getResources().getStringArray(R.array.ghala)))
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
                        } else {


                        }
                    }
                });


        /*
        PostData.AddToContactsList(p);
        contacts = PostData.getContactsList();
        */

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.bringToFront();
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                contacts.clear();
                switch (item.getItemId()) {
                    case R.id.fruitemenu:

                        db.collection("Posts")
                                .whereIn("Type", Arrays.asList(getResources().getStringArray(R.array.ghala)))
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
                                        } else {


                                        }
                                    }
                                });
                        return true;
                    case R.id.legumemenu:
                        db.collection("Posts")
                                .whereIn("Type", Arrays.asList(getResources().getStringArray(R.array.batata)))
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
                                        } else {


                                        }
                                    }
                                });
                        return true;
                    case R.id.autremenu:
                        db.collection("Posts")
                                .whereIn("Type", Arrays.asList(getResources().getStringArray(R.array.autre)))
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
                                        } else {


                                        }
                                    }
                                });
                        return true;
                }
                return false;
            }
        });
        /*
        BottomNavigationView.OnNavigationItemSelectedListener  {
            item ->
                when(item.itemId) {
            R.id.item1 -> {
                // Respond to navigation item 1 click
                true
            }
            R.id.item2 -> {
                // Respond to navigation item 2 click
                true
           }
       else -> false
   }
      }

         */
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