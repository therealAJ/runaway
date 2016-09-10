package com.ivonliu.runaway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ivon on 9/10/16.
 */
public class FileListAdapter extends ArrayAdapter<FileListItem> {

    private List<FileListItem> mList;

    public FileListAdapter(Context context, List<FileListItem> list) {
        super(context, android.R.layout.simple_list_item_1, list);
        mList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FileListItem item = mList.get(position);

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.file_list_item, null);
        }

        TextView textView = (TextView) view.findViewById(R.id.file_list_text);
        textView.setText(item.name);

        return view;
    }
}
