package ru.mgusev.eldritchhorror;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PartyViewHolder> {

    public static class PartyViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView date;
        TextView ancientOne;
        TextView playersCount;
        TextView score;

        public PartyViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.itemCV);
            date = itemView.findViewById(R.id.date);
            ancientOne = itemView.findViewById(R.id.ancientOne);
            playersCount = itemView.findViewById(R.id.playersCount);
            score = itemView.findViewById(R.id.score);
        }
    }

    //declare interface
    private OnItemClicked onClick;
    List<Party> partyList;

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(int position);
    }

    public RVAdapter(List<Party> partyList) {
        this.partyList = partyList;
    }

    @Override
    public RVAdapter.PartyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        PartyViewHolder partyViewHolder = new PartyViewHolder(view);
        return partyViewHolder;
    }

    @Override
    public void onBindViewHolder(RVAdapter.PartyViewHolder holder, final int position) {
        holder.date.setText(partyList.get(position).date);
        holder.ancientOne.setText(partyList.get(position).ancientOne);
        holder.playersCount.setText(String.valueOf(partyList.get(position).playersCount));
        holder.score.setText(String.valueOf(partyList.get(position).score));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return partyList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }
}
