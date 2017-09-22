package ru.mgusev.eldritchhorror;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.sql.SQLException;
import java.util.List;

class RVAdapter extends RecyclerView.Adapter<RVAdapter.GameViewHolder> {

    static class GameViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView background;
        TextView date;
        TextView ancientOne;
        TextView playersCount;
        TextView score;

        GameViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.itemCV);
            background = itemView.findViewById(R.id.background);
            date = itemView.findViewById(R.id.date);
            ancientOne = itemView.findViewById(R.id.ancientOne);
            playersCount = itemView.findViewById(R.id.playersCount);
            score = itemView.findViewById(R.id.score);
        }
    }

    //declare interface
    private OnItemClicked onClick;
    private List<Game> gameList;
    private Context context;

    //make interface like this
    interface OnItemClicked {
        void onItemClick(int position);
    }

    RVAdapter(Context context, List<Game> gameList) {
        this.context = context;
        this.gameList = gameList;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, final int position) {
        holder.date.setText(gameList.get(position).date);
        try {
            Resources resources = context.getResources();
            final int resourceId = resources.getIdentifier(HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneImageResourceByID(gameList.get(position).ancientOneID), "drawable", context.getPackageName());
            holder.background.setImageResource(resourceId);
            holder.ancientOne.setText(HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneNameByID(gameList.get(position).ancientOneID));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        holder.playersCount.setText(String.valueOf(gameList.get(position).playersCount));
        holder.score.setText(String.valueOf(gameList.get(position).score));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }
}