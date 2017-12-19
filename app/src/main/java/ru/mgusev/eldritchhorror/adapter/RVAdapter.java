package ru.mgusev.eldritchhorror.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.sql.SQLException;
import java.util.List;

import ru.mgusev.eldritchhorror.activity.MainActivity;
import ru.mgusev.eldritchhorror.database.HelperFactory;
import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.eh_interface.OnItemClicked;
import ru.mgusev.eldritchhorror.model.Game;

public class RVAdapter extends RecyclerSwipeAdapter<RVAdapter.GameViewHolder> {

    static class GameViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView background;
        ImageView expansionImage;
        TextView date;
        TextView ancientOne;
        TextView playersCount;
        TextView score;
        ImageView winImage;
        SwipeLayout swipeLayout;
        ImageView editImgSwipe;
        ImageView deleteImgSwipe;

        GameViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.itemCV);
            background = itemView.findViewById(R.id.background);
            expansionImage = itemView.findViewById(R.id.expansion_image);
            date = itemView.findViewById(R.id.date);
            ancientOne = itemView.findViewById(R.id.ancientOne);
            playersCount = itemView.findViewById(R.id.playersCount);
            score = itemView.findViewById(R.id.score);
            winImage = itemView.findViewById(R.id.win_image_main);
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
    private Animation animAlpha;

    public void setGameList(List<Game> gameList) {
        this.gameList = gameList;
    }

    public RVAdapter(Context context, List<Game> gameList) {
        this.context = context;
        this.gameList = gameList;
        animAlpha = AnimationUtils.loadAnimation(context, R.anim.alpha);
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
                if (currentSwipeLayout != layout) closeSwipeLayout();
                currentSwipeLayout = layout;
            }
        });

        holder.date.setText(MainActivity.formatter.format(gameList.get(position).date));
        try {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier(HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneImageResourceByID(gameList.get(position).ancientOneID), "drawable", context.getPackageName());
            holder.background.setImageResource(resourceId);
            if (HelperFactory.getStaticHelper().getExpansionDAO().getImageResourceByAncientOneID(gameList.get(position).ancientOneID) != null) {
                resourceId = resources.getIdentifier(HelperFactory.getStaticHelper().getExpansionDAO().getImageResourceByAncientOneID(gameList.get(position).ancientOneID), "drawable", context.getPackageName());
                holder.expansionImage.setImageResource(resourceId);
                holder.expansionImage.setVisibility(View.VISIBLE);
            } else holder.expansionImage.setVisibility(View.GONE);
            holder.ancientOne.setText(HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneNameByID(gameList.get(position).ancientOneID));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        holder.playersCount.setText(String.valueOf(gameList.get(position).playersCount));
        if (gameList.get(position).isWinGame) {
            holder.winImage.setImageResource(R.drawable.stars);
            holder.score.setText(String.valueOf(gameList.get(position).score));
            holder.score.setVisibility(View.VISIBLE);
        } else {
            if (gameList.get(position).isDefeatByAwakenedAncientOne) holder.winImage.setImageResource(R.drawable.skull);
            else if (gameList.get(position).isDefeatByElimination) holder.winImage.setImageResource(R.drawable.inestigators_out);
            else if (gameList.get(position).isDefeatByMythosDepletion) holder.winImage.setImageResource(R.drawable.mythos_empty);
            holder.score.setVisibility(View.GONE);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //v.startAnimation(animAlpha);
                onClick.onItemClick(position);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
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

    public void closeSwipeLayout() {
        if (currentSwipeLayout != null) currentSwipeLayout.close(true);
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }
}