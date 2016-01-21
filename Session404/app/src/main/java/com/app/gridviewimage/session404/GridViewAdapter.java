package com.app.gridviewimage.session404;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter{
    private Context mContext;
    private final String[] Names;
    private final int[] Images;

    public GridViewAdapter(Context c,String[] Names,int[] Images ) {
        mContext = c;
        this.Images = Images;
        this.Names = Names;
    }

    @Override
    public int getCount() {

        return Names.length;
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.imagelist, null);
            TextView textView = (TextView) grid.findViewById(R.id.textView1);
            ImageView imageView = (ImageView)grid.findViewById(R.id.imageView1);
            textView.setText(Names[position]);
            imageView.setImageResource(Images[position]);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}