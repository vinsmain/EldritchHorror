package ru.mgusev.eldritchhorror.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import java.util.List;
import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.model.Expansion;

public class ExpansionListAdapter extends RecyclerView.Adapter<ExpansionListAdapter.ExpansionViewHolder> implements CompoundButton.OnCheckedChangeListener {

    static class ExpansionViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemExpansionLayout;
        ImageView itemExpansionImage;
        Switch itemExpansionSwitch;

        public ExpansionViewHolder(View itemView) {
            super(itemView);
            itemExpansionLayout = itemView.findViewById(R.id.item_expansion_layout);
            itemExpansionImage = itemView.findViewById(R.id.item_expansion_image);
            itemExpansionSwitch = itemView.findViewById(R.id.item_expansion_switch);
        }
    }

    private List<Expansion> listStorage;
    private Context context;

    public ExpansionListAdapter(Context context, List<Expansion> listStorage) {
        this.context = context;
        this.listStorage = listStorage;
    }

    @Override
    public ExpansionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expansion, parent, false);
        return new ExpansionListAdapter.ExpansionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExpansionViewHolder holder, int position) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(listStorage.get(position).imageResource, "drawable", context.getPackageName());
        holder.itemExpansionImage.setImageResource(resourceId);
        holder.itemExpansionSwitch.setText(listStorage.get(position).name);
        if (listStorage.get(position).isEnable) holder.itemExpansionSwitch.setChecked(true);
        else holder.itemExpansionSwitch.setChecked(false);
        holder.itemExpansionSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public int getItemCount() {
        return listStorage.size();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int position = 0;
        for (int i = 0; i < listStorage.size(); i++) {
            if (listStorage.get(i).name.equals(compoundButton.getText())) {
                listStorage.get(i).isEnable = b;
                position = i;
                break;
            }
        }

        System.out.println(listStorage.get(position).isEnable);
    }

    public List<Expansion> getListStorage() {
        return listStorage;
    }
}