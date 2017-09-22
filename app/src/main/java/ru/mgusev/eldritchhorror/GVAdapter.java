package ru.mgusev.eldritchhorror;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class GVAdapter extends BaseAdapter {

    static class InvestigatorViewHolder {
        ImageView invPhoto;
        TextView invName;
        TextView invOccupation;

        public InvestigatorViewHolder(View view) {
            invPhoto = (ImageView) view.findViewById(R.id.invPhoto);
            invName = (TextView) view.findViewById(R.id.invName);
            invOccupation = (TextView) view.findViewById(R.id.invOccupation);
        }
    }

    private LayoutInflater layoutinflater;
    private List<Investigator> listStorage;
    private Context context;
    InvestigatorViewHolder viewHolder;

    public GVAdapter(Context context, List<Investigator> listStorage) {
        this.context = context;
        this.listStorage = listStorage;
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public Object getItem(int i) {
        return listStorage.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            Resources resources = context.getResources();
            // TODO final int resourceId = resources.getIdentifier(HelperFactory.getStaticHelper().getInvestigatorLocalDAO().getAncientOneImageResourceByID(gameList.get(position).ancientOneID), "drawable", context.getPackageName());
            view = LayoutInflater.from(context).inflate(R.layout.item_investigator, viewGroup, false);
            viewHolder = new InvestigatorViewHolder(view);
            // TODO viewHolder.invPhoto.setImageResource(resourceId);
        } else viewHolder = (InvestigatorViewHolder) view.getTag();
        return view;
    }
}
