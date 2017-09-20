package ru.mgusev.eldritchhorror;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class GVAdapter extends BaseAdapter {

    static class InvestigatorViewHolder {
        ImageView imageInListView;
        TextView textInListView;
    }

    private LayoutInflater layoutinflater;
    private List<Investigator> listStorage;
    private Context context;

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
        InvestigatorViewHolder viewHolder;
        if(view == null) {
            viewHolder = new InvestigatorViewHolder();
            //view = layoutinflater.inflate(R.layout.item_investigator, viewGroup, false);
            view = LayoutInflater.from(context).inflate(R.layout.item_investigator, viewGroup, false);
            view.setTag(viewHolder);
        } else viewHolder = (InvestigatorViewHolder) view.getTag();
        return view;
    }
}
