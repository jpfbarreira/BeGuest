package com.example.beguest.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beguest.R;

import java.util.ArrayList;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.TagsAdapterViewHolder> {
    private ArrayList<String> tags;
    private Context context;

    public TagsAdapter(Context context, ArrayList<String> tags) {
        this.context = context;
        this.tags = tags;
    }

    @NonNull
    @Override
    public TagsAdapter.TagsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_tags_layout, parent, false);

        return new TagsAdapter.TagsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagsAdapter.TagsAdapterViewHolder holder, int position) {
        String tag = tags.get(position);
        holder.tagsText.setText(tag);

//        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tags.remove(tags.get(position));
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public static class TagsAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView tagsText;
        ImageView deleteBtn;
        ConstraintLayout layout;

        public TagsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tagsText = itemView.findViewById(R.id.event_tag);
            deleteBtn = itemView.findViewById(R.id.delete_btn);
            layout = itemView.findViewById(R.id.event_tag_layout);
        }
    }
}
