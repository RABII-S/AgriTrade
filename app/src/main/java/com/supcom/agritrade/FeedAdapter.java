package com.supcom.agritrade;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import static androidx.core.content.ContextCompat.startActivity;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    Context ct;
    private List<PostData> captions;

    private int[] imageIds;
    Dialog dialog;
    private LinearLayout post;

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        private CardView cardView;
        private TextView type;
        private TextView prix;
        private TextView quantite;
        private ImageView image;
        private TextView date;
        public ViewHolder(View v) {
            super(v);
            cardView=v.findViewById(R.id.cardView);
            image=v.findViewById(R.id.taswira);
            type=v.findViewById(R.id.typeex);
            prix=v.findViewById(R.id.prixdelunitex);
            date=v.findViewById(R.id.datex);
            quantite=v.findViewById(R.id.quantitedisponiblex);
        }
    }

    public FeedAdapter(List<PostData> captions, Context ct) {
        this.captions = captions;
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

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ct ,commande.class);

                PostData postData=captions.get(position);

                intent.putExtra("postD",postData);
                intent.putExtra("image",captions.get(position).getImage());
                v.getContext().startActivity(intent);


            }
        });

        final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(captions.get(position).getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ct)
                        .load(uri)
                        .into(holder.image);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        holder.type.setText(captions.get(position).getType());
        holder.prix.setText(captions.get(position).getPrice()+" DT/"+captions.get(position).getUnite());
        holder.quantite.setText(captions.get(position).getDescription()+" "+captions.get(position).getUnite());
        holder.date.setText(captions.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return captions.size();

    }
}

