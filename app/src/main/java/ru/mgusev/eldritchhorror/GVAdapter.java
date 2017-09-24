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

        public InvestigatorViewHolder(ImageView invPhoto, TextView invName, TextView invOccupation) {
            this.invPhoto = invPhoto;
            this.invName = invName;
            this.invOccupation = invOccupation;
        }

        /*public InvestigatorViewHolder(View view) {
            invPhoto = (ImageView) view.findViewById(R.id.invPhoto);
            invName = (TextView) view.findViewById(R.id.invName);
            invOccupation = (TextView) view.findViewById(R.id.invOccupation);
        }*/
    }

    private List<InvestigatorLocal> listStorage;
    private Context context;
    private InvestigatorViewHolder viewHolder;

    public GVAdapter(Context context, List<InvestigatorLocal> listStorage) {
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

        final InvestigatorLocal investigatorLocal = listStorage.get(i);
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_investigator, viewGroup, false);
            final ImageView invPhoto = (ImageView) view.findViewById(R.id.invPhoto);
            final TextView invName = (TextView) view.findViewById(R.id.invName);
            final TextView invOccupation = (TextView) view.findViewById(R.id.invOccupation);

            final InvestigatorViewHolder viewHolder = new InvestigatorViewHolder(invPhoto, invName, invOccupation);
            view.setTag(viewHolder);
        }

        final InvestigatorViewHolder viewHolder = (InvestigatorViewHolder) view.getTag();

        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(listStorage.get(i).imageResource, "drawable", context.getPackageName());
        viewHolder.invPhoto.setImageResource(resourceId);
        viewHolder.invName.setText(investigatorLocal.name);
        System.out.println(investigatorLocal.name);
        viewHolder.invOccupation.setText(investigatorLocal.occupation);
        return view;
    }
}
