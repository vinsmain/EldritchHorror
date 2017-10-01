package ru.mgusev.eldritchhorror;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.util.List;

public class InvRVAdapter extends RecyclerView.Adapter<InvRVAdapter.InvDetailViewHolder> {

    static class InvDetailViewHolder extends RecyclerView.ViewHolder {
        CardView invCardView;
        ImageView invPhoto;

        public InvDetailViewHolder(View itemView) {
            super(itemView);
            invCardView = itemView.findViewById(R.id.invCardView);
            invPhoto = itemView.findViewById(R.id.invPhoto);
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
        holder.invPhoto.setImageResource(resourceId);
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
