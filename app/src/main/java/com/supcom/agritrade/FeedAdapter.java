package com.supcom.agritrade;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static androidx.core.content.ContextCompat.startActivity;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    Context ct;
    private List<PostData> captions;
    int cType;
    private int[] imageIds;
    Dialog dialog;
    private LinearLayout post;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl;
        private TextView type;
        private TextView prix;
        private TextView quantite;
        private ImageView image;
        private TextView date;
        private TextView quantiteCP;
        private TextView prixCP;
        private RatingBar stars;
        private FirebaseFirestore db;
        private TextView location;

        public ViewHolder(View v) {
            super(v);
            rl = v.findViewById(R.id.layoutofcard);
            image = v.findViewById(R.id.taswira);
            type = v.findViewById(R.id.typeex);
            prix = v.findViewById(R.id.prixdelunitex);
            date = v.findViewById(R.id.datex);
            quantiteCP = v.findViewById(R.id.quantitedisponible);
            prixCP = v.findViewById(R.id.prixdelunite);
            quantite = v.findViewById(R.id.quantitedisponiblex);
            stars = v.findViewById(R.id.stars);
            location = v.findViewById(R.id.location);
        }
    }

    public void setCaptions(ArrayList<PostData> L, int cType) {
        captions = L;
        this.cType = cType;
        notifyDataSetChanged();
    }

    public void setcType(int cType) {
        this.cType = cType;
        notifyDataSetChanged();
    }

    public FeedAdapter(Context ct) {
        this.ct = ct;
    }

    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cv = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_card, parent, false);
        return new ViewHolder(cv);
    }


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
    public void onBindViewHolder(@NonNull final FeedAdapter.ViewHolder holder, final int position) {
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(captions.get(position).getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ct)
                        .load(uri)
                        .centerCrop()
                        .into(holder.image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        if (cType == 1) {
            holder.quantiteCP.setText("Order Quantity:");
            holder.prixCP.setText("Total Price:");
            holder.type.setText(captions.get(position).getType());
            holder.prix.setText(captions.get(position).getPrice() + " DT");
            holder.quantite.setText(captions.get(position).getDescription() + " " + captions.get(position).getUnite());
            holder.date.setText(captions.get(position).getDate());
            holder.rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean ratingState = captions.get(position).getRatingState();
                    if (ratingState == false) {
                        Intent intent = new Intent(ct, rate.class);
                        PostData postData = captions.get(position);
                        intent.putExtra("postD", postData);
                        v.getContext().startActivity(intent);
                    } else {
                        Toast.makeText(ct, "Vous avez déja noté cette commande", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            String Uid = captions.get(position).getPosterID();
            db.collection("users").document(Uid).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String stars = documentSnapshot.getString("Stars");
                            holder.stars.setRating(Float.parseFloat(stars));
                            holder.location.setText(documentSnapshot.getString("Localisation"));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
            /*
            holder.stars.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    String s = String.valueOf(holder.stars.getRating());
                    Map<String, Object> map = new HashMap<>();
                    char c=s.charAt(0);
                    float a=c-48;
                    if(s.length()>=3){
                        a+=0.5;
                    }
                    System.out.println(a);
                    map.put("stars", s);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    final DocumentReference docRef = db.collection("users").document(captions.get(position).getPosterID());
                    docRef.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}
                            });
                    return true;
                }
            });
             */
        } else {

            holder.rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ct, commande.class);
                    PostData postData = captions.get(position);
                    intent.putExtra("postD", postData);
                    intent.putExtra("image", captions.get(position).getImage());
                    v.getContext().startActivity(intent);
                }
            });
            holder.quantiteCP.setText("Quantity:");
            holder.prixCP.setText("Unit Price:");
            holder.type.setText(captions.get(position).getType());
            holder.prix.setText(captions.get(position).getPrice() + " DT/" + captions.get(position).getUnite());
            holder.quantite.setText(captions.get(position).getDescription() + " " + captions.get(position).getUnite());
            holder.date.setText(captions.get(position).getDate());
            String Uid = captions.get(position).getPosterID();
            db.collection("users").document(Uid).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String stars = documentSnapshot.getString("Stars");
                            holder.stars.setRating(Float.parseFloat(stars));
                            holder.location.setText(documentSnapshot.getString("Localisation"));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return captions.size();

    }
}