package com.ivonliu.runaway;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment
        implements FetchFilesTask.OnFetchFilesCompleteListener, AdapterView.OnItemClickListener {

    private ListView mListView;
    private FileListAdapter mAdapter;

    private String mCurrentDir = "/";

    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mListView = (ListView) rootView.findViewById(R.id.list);

        List<FileListItem> files = new ArrayList<>();
        mAdapter = new FileListAdapter(getActivity(), files);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        refresh();

        return rootView;
    }

    private void refresh() {
        new FetchFilesTask(this).execute(mCurrentDir);
    }

    @Override
    public void onFetchFilesComplete(List<FileListItem> files) {
        Log.i("TAG", "In callback: " + files.toString());
        mAdapter.clear();
        mAdapter.addAll(files);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        FileListItem item = mAdapter.getItem(pos);
        if (item.isDirectory) {
            if (item.name.equals("..")) {
                String[] sections = mCurrentDir.split("/");
                mCurrentDir = "";
                for (int i = 0; i < sections.length - 1; i++) {
                    mCurrentDir += sections[i] + "/";
                }
            } else {
                mCurrentDir += item.name + "/";
            }
            refresh();
        }
    }
}
