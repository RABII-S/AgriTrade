package com.supcom.agritrade;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        rv = (RecyclerView) findViewById(R.id.recyclerview);
        final FeedAdapter adapter = new FeedAdapter(contacts);
        rv.setAdapter(adapter);
        db.collection("Posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                PostData p = new PostData(document.getData().get("Type").toString(), document.getData().get("Price").toString(), document.getData().get("Description").toString());
                                contacts.add(p);
                                adapter.notifyDataSetChanged();

                            }
                        } else {


                        }
                    }
                });
        FloatingActionButton fab = findViewById(R.id.gotopost);
        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       Intent intent = new Intent(Feed.this, Post.class);
                                       startActivity(intent);
                                   }


                               });
        FloatingActionButton fab1 = findViewById(R.id.logout);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Feed.this, MainActivity.class);
                startActivity(intent);
            }


        });

        /*
        PostData.AddToContactsList(p);
        contacts = PostData.getContactsList();
        */

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

}