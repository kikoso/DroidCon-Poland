package com.droidcon.snaphack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dropbox.sync.android.DbxFileInfo;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends BaseAdapter {
    private final List<DbxFileInfo> items = new ArrayList<>();
    private Context context;

    public PhotoAdapter(Context context){
        this.context = context;
    }

    public void setItems(List<DbxFileInfo> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public DbxFileInfo getItem(int i) {
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
            convertView = inflater.inflate(android.R.layout.simple_expandable_list_item_2, null);
        }

        DbxFileInfo item = getItem(i);

        TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);
        textView1.setText(item.iconName);
        return convertView;
    }
}
