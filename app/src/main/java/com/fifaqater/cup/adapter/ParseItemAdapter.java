package com.fifaqater.cup.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fifaqater.cup.Player;
import com.fifaqater.cup.R;
import com.fifaqater.cup.model.ParseItemModel;

import java.util.ArrayList;
import java.util.List;

public class ParseItemAdapter extends RecyclerView.Adapter<ParseItemAdapter.ViewHolder> {

    private final List<ParseItemModel> parseItemModelArrayList;
    private final Context context;

    public ParseItemAdapter(List<ParseItemModel> parseItemModelArrayList, Context context) {
        this.parseItemModelArrayList = parseItemModelArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ParseItemModel parseItemModel = parseItemModelArrayList.get(position);
        holder.time.setText(parseItemModel.getTime());
        holder.title_a.setText(parseItemModel.getTeama());
        holder.title_b.setText(parseItemModel.getTeamb());
        holder.league.setText(parseItemModel.getLeague());

        Glide.with(context).load(parseItemModel.getImageA()).into(holder.imageView);
        Glide.with(context).load(parseItemModel.getImageB()).into(holder.imageView2);

        if (parseItemModel.getGif().startsWith("http")) {
            Glide.with(context).load(parseItemModel.getGif()).into(holder.imageView3);
            holder.imageView3.setVisibility(View.VISIBLE);
        } else {
            holder.imageView3.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return parseItemModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        ImageView imageView3;
        ImageView imageView2;
        TextView time;
        TextView title_a;
        TextView title_b;
        TextView league;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView2 = itemView.findViewById(R.id.image_B);
            imageView = itemView.findViewById(R.id.image_A);
            imageView3 = itemView.findViewById(R.id.gif);
            time = itemView.findViewById(R.id.time);
            title_a = itemView.findViewById(R.id.teama);
            title_b = itemView.findViewById(R.id.teamb);
            league = itemView.findViewById(R.id.league);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                ParseItemModel parseItem = parseItemModelArrayList.get(position);
                List<String> postLinks = parseItem.getPostLinkList();
                List<String> linkTitles = parseItem.getTitleLinkList();
                String label = parseItem.getTeama() + " Vs " + parseItem.getTeamb();

                if (postLinks == null || postLinks.isEmpty()) {
                    Toast.makeText(context, "Link will be available on match time", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(context, Player.class);
                    intent.putStringArrayListExtra("iframeLinks", (ArrayList<String>) postLinks);
                    intent.putStringArrayListExtra("iframeTitles", (ArrayList<String>) linkTitles);
                    intent.putExtra("title", label);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            }
        }
    }
}
