package com.ivonliu.runaway;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements FetchFilesTask.OnFetchFilesCompleteListener {

    private ListView mListView;
    private FileListAdapter mAdapter;

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

        refresh();

        return rootView;
    }

    private void refresh() {
        new FetchFilesTask(this).execute("some/directory");
    }

    @Override
    public void onFetchFilesComplete(List<FileListItem> files) {
        Log.i("TAG", "In callback: " + files.toString());
        mAdapter.clear();
        mAdapter.addAll(files);
        mAdapter.notifyDataSetChanged();
    }
}
