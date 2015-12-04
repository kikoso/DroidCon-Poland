package com.droidcon.snaphack.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.droidcon.snaphack.R;
import com.droidcon.snaphack.model.PhotoItem;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends BaseAdapter {
    private final List<PhotoItem> items = new ArrayList<>();
    private Context context;

    public PhotoAdapter(Context context) {
        this.context = context;
    }

    public void setItems(List<PhotoItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public PhotoItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.griditem, null);
        }

        PhotoItem photoItem = getItem(i);
        TextView textView1 = (TextView) convertView.findViewById(R.id.textView);
        textView1.setText(photoItem.getFilename());
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        imageView.setImageBitmap(photoItem.getImage());
        return convertView;
    }
}
