package com.custom.list.session402;

import android.app.Activity;
import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by Johny on 08-01-2016.
 */
public class CustomBaseAdapter extends BaseAdapter {
    Context context;
    List<MainActivity.row> rowItems;

    public CustomBaseAdapter(Context context, List<MainActivity.row> items) {
        this.context = context;
        this.rowItems = items;
    }

    /*private view holder class*/
    private class ViewHolder {

        TextView txtname;
        TextView txtnum;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.txtname = (TextView) convertView.findViewById(R.id.name);
            holder.txtnum = (TextView) convertView.findViewById(R.id.num);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        MainActivity.row rowItem = (MainActivity.row) getItem(position);

        holder.txtname.setText(rowItem.getNames());
        holder.txtnum.setText(rowItem.getNum());

        return convertView;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }
}

