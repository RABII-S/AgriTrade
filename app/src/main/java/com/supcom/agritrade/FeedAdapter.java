package com.supcom.agritrade;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Vector;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private List<PostData> captions;
    private int[] imageIds;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public FeedAdapter(List<PostData> captions) {
        this.captions = captions;
    }

    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_card, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView textView = (TextView) cardView.findViewById(R.id.TypeF);
        TextView textView2 = (TextView) cardView.findViewById(R.id.PriceF);
        TextView textView3 = (TextView) cardView.findViewById(R.id.DescriptionF);
      //  ImageView image=(ImageView).cardView.findViewById(R.id.imageView) ;
        textView.setText(captions.get(position).getType());
        textView2.setText(captions.get(position).getPrice());
        textView3.setText(captions.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return captions.size();

    }
}

