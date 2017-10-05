package ru.mgusev.eldritchhorror;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.sql.SQLException;
import java.util.List;

class RVAdapter extends RecyclerSwipeAdapter<RVAdapter.GameViewHolder> {

    static class GameViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView background;
        TextView date;
        TextView ancientOne;
        TextView playersCount;
        TextView score;
        SwipeLayout swipeLayout;
        ImageView editImgSwipe;
        ImageView deleteImgSwipe;

        GameViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.itemCV);
            background = itemView.findViewById(R.id.background);
            date = itemView.findViewById(R.id.date);
            ancientOne = itemView.findViewById(R.id.ancientOne);
            playersCount = itemView.findViewById(R.id.playersCount);
            score = itemView.findViewById(R.id.score);
            editImgSwipe = itemView.findViewById(R.id.editImgSwipe);
            deleteImgSwipe = itemView.findViewById(R.id.deleteImgSwipe);
            swipeLayout =  itemView.findViewById(R.id.swipeLayout);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        }
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipeLayout;
    }

    //declare interface
    private OnItemClicked onClick;
    private List<Game> gameList;
    private Context context;
    private SwipeLayout currentSwipeLayout;

    public void setGameList(List<Game> gameList) {
        this.gameList = gameList;
    }

    //make interface like this
    interface OnItemClicked {
        void onItemClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
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
    public void onBindViewHolder(final GameViewHolder holder, final int position) {

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        holder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                if (currentSwipeLayout != null && currentSwipeLayout != layout) currentSwipeLayout.close(true);
                currentSwipeLayout = layout;
            }
        });

        holder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(context, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });




        /*holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.removeShownLayouts(holder.swipeLayout);
                mDataset.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mDataset.size());
                mItemManger.closeAllItems();
                Toast.makeText(view.getContext(), "Deleted " + holder.textViewData.getText().toString() + "!", Toast.LENGTH_SHORT).show();
            }
        });*/




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

        holder.editImgSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onEditClick(position);
            }
        });

        holder.deleteImgSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onDeleteClick(position);
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