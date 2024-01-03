package com.fifaqater.cup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.fifaqater.cup.R;
import com.fifaqater.cup.model.ParseItemModel2;

import java.util.List;

public class ParseItemAdapter2 extends RecyclerView.Adapter<ParseItemAdapter2.ViewHolder> {

    private final List<ParseItemModel2> parseItemModelArrayList;
    private final Context context;

    public ParseItemAdapter2(List<ParseItemModel2> parseItemModelArrayList, Context context) {
        this.parseItemModelArrayList = parseItemModelArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ParseItemModel2 parseItemModel2 = parseItemModelArrayList.get(position);
        holder.bind(parseItemModel2);
    }

    @Override
    public int getItemCount() {
        return parseItemModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView match;
        TextView league;
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);

            match = itemView.findViewById(R.id.match);
            league = itemView.findViewById(R.id.league);
            time = itemView.findViewById(R.id.time);

            itemView.setOnClickListener(this);
        }

        public void bind(ParseItemModel2 parseItemModel2) {
            // Add null checks to prevent NullPointerException
            match.setText(parseItemModel2.getMatch() != null ? parseItemModel2.getMatch() : "");
            league.setText(parseItemModel2.getLeague() != null ? parseItemModel2.getLeague() : "");
            time.setText(parseItemModel2.getDay() != null ? parseItemModel2.getDay() : "");
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                // Handle item click if needed
            }
        }
    }
}
