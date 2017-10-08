package ru.mgusev.eldritchhorror;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class GVAdapter extends RecyclerView.Adapter<GVAdapter.InvestigatorViewHolder> {

    static class InvestigatorViewHolder extends RecyclerView.ViewHolder {
        CardView invCardView;
        ImageView invPhoto;
        TextView invName;
        TextView invOccupation;

        public InvestigatorViewHolder(View itemView) {
            super(itemView);
            invCardView = itemView.findViewById(R.id.invCardView);
            invPhoto = itemView.findViewById(R.id.invPhoto);
            invName = itemView.findViewById(R.id.invName);
            invOccupation = itemView.findViewById(R.id.invOccupation);
        }
    }

    private GVAdapter.OnItemClicked onClick;
    private List<Investigator> listStorage;
    private Context context;

    interface OnItemClicked {
        void onItemClick(int position);
    }

    public GVAdapter(Context context, List<Investigator> listStorage) {
        this.context = context;
        this.listStorage = listStorage;
    }

    @Override
    public InvestigatorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_investigator, parent, false);
        return new InvestigatorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final InvestigatorViewHolder holder, int position) {
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(listStorage.get(position).imageResource, "drawable", context.getPackageName());
        holder.invPhoto.setImageResource(resourceId);
        holder.invName.setText(listStorage.get(position).name);
        holder.invOccupation.setText(listStorage.get(position).occupation);

        System.out.println(listStorage.get(position).name);

        if (listStorage.get(position).isStarting || listStorage.get(position).isReplacement) {
            holder.invCardView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            holder.invName.setTextColor(ContextCompat.getColor(context, R.color.colorText));
            holder.invOccupation.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryText));
            System.out.println(listStorage.get(position).name);
        } else {
            holder.invCardView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorText));
            holder.invName.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryText));
            holder.invOccupation.setTextColor(ContextCompat.getColor(context, R.color.colorSecondaryText));
        }

        holder.invCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listStorage.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    void setOnClick(OnItemClicked onClick) {
        this.onClick=onClick;
    }
}
