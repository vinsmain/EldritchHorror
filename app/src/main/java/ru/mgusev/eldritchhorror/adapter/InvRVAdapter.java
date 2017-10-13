package ru.mgusev.eldritchhorror.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.util.List;

import ru.mgusev.eldritchhorror.activity.Investigator;
import ru.mgusev.eldritchhorror.R;

public class InvRVAdapter extends RecyclerView.Adapter<InvRVAdapter.InvDetailViewHolder> {

    static class InvDetailViewHolder extends RecyclerView.ViewHolder {
        CardView invCardViewDetail;
        ImageView invPhotoDetail;
        ImageView invDeadDetail;

        public InvDetailViewHolder(View itemView) {
            super(itemView);
            invCardViewDetail = itemView.findViewById(R.id.invCardViewDetail);
            invPhotoDetail = itemView.findViewById(R.id.invPhotoDetail);
            invDeadDetail = itemView.findViewById(R.id.invDeadDetail);
        }
    }

    private List<Investigator> listStorage;
    private Context context;

    public InvRVAdapter(Context context, List<Investigator> listStorage) {
        this.context = context;
        this.listStorage = listStorage;
    }

    @Override
    public InvRVAdapter.InvDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_investigator_detail, parent, false);
        return new InvRVAdapter.InvDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final InvRVAdapter.InvDetailViewHolder holder, int position) {
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(listStorage.get(position).imageResource, "drawable", context.getPackageName());
        holder.invPhotoDetail.setImageResource(resourceId);

        int colorID = R.color.color_starting_investigator;
        if (listStorage.get(position).isReplacement) colorID = R.color.color_replacement_investigator;
        holder.invCardViewDetail.setCardBackgroundColor(ContextCompat.getColor(context, colorID));

        if (listStorage.get(position).isDead) {
            final int deadImgId = resources.getIdentifier("dead", "drawable", context.getPackageName());
            holder.invDeadDetail.setImageResource(deadImgId);
        }
    }

    @Override
    public int getItemCount() {
        return listStorage.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
