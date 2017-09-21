package ru.mgusev.eldritchhorror;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    private String[] ancientOneArray = {"Абхот", "Азатот", "Йиг", "Йог-сотот", "Ктулху", "Нефрен-Ка", "Шуб-ниггурат"};

    //make interface like this
    interface OnItemClicked {
        void onItemClick(int position);
    }

    RVAdapter(List<Game> gameList) {
        this.gameList = gameList;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, final int position) {
        holder.background.setImageResource(getBackground(gameList.get(position).ancientOne));
        holder.date.setText(gameList.get(position).date);
        holder.ancientOne.setText(gameList.get(position).ancientOne);
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

    private int getBackground(String name) {
        if (name.equals(ancientOneArray[0])) return R.drawable.abhoth;
        else if (name.equals(ancientOneArray[1])) return R.drawable.azathoth;
        else if (name.equals(ancientOneArray[2])) return R.drawable.yig;
        else if (name.equals(ancientOneArray[3])) return R.drawable.yogsothoth;
        else if (name.equals(ancientOneArray[4])) return R.drawable.cthulhu;
        else if (name.equals(ancientOneArray[5])) return R.drawable.nephren_ka;
        else if (name.equals(ancientOneArray[6])) return R.drawable.shub_niggurath;
        return 0;
    }
}