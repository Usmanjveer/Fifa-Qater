package com.fifaqater.cup.adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.fifaqater.cup.Middle;
import com.fifaqater.cup.model.ParseItemModel1;
import com.squareup.picasso.Picasso;
import com.fifaqater.cup.R;

import java.util.List;

public class ParseItemAdapter1 extends RecyclerView.Adapter<ParseItemAdapter1.ViewHolder> {

    private final List<ParseItemModel1> parseItemModel1ArrayList;
    private final Context context;

    public ParseItemAdapter1(List<ParseItemModel1> parseItemModel1ArrayList, Context context) {
        this.parseItemModel1ArrayList = parseItemModel1ArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_itm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ParseItemModel1 parseItemModel1 = parseItemModel1ArrayList.get(position);
        holder.title.setText(parseItemModel1.getTitle());
        Picasso.get().load(parseItemModel1.getImageLink()).into(holder.imageView);

        Log.e("TAG", "onBindViewHolder: " + parseItemModel1ArrayList);

        /*if (parseItemModel1.getGif().startsWith("http")) {
            Glide.with(context)
                    .load(parseItemModel1.getGif())
                    .into(holder.imageView3);
            holder.imageView3.setVisibility(View.VISIBLE);
        } else {
            holder.imageView3.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return parseItemModel1ArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_id);
            title = itemView.findViewById(R.id.title_goal);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ParseItemModel1 parseItem = parseItemModel1ArrayList.get(position);
            String postLink = parseItem.getPostLink();
            if (postLink == null || postLink.isEmpty()) {
                Toast.makeText(context, "Link will be available on match time", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(context, Middle.class);
                intent.putExtra("title", parseItem.getTitle());
                intent.putExtra("postLink", parseItem.getPostLink());
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        }
    }
}

