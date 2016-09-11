package com.ivonliu.runaway;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment
        implements FetchFilesTask.OnFetchFilesCompleteListener, AdapterView.OnItemClickListener,
                    FileDownloadTask.OnFileDownloadCompleteListener, OnFabClickListener,
                    SaveFileTask.OnSaveFileCompleteListener {

    private static final int PICKFILE_RESULT_CODE = 0;

    private ListView mListView;
    private FileListAdapter mAdapter;

    private String mCurrentDir = "/";

    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ((MainActivity) getActivity()).setFabClickListener(this);

        mListView = (ListView) rootView.findViewById(R.id.list);

        List<FileListItem> files = new ArrayList<>();
        mAdapter = new FileListAdapter(getActivity(), files);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        refresh();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.action_refresh) {
            refresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    public void onFileDownloadComplete(String fileName) {
        Toast.makeText(getActivity(), "Successfully downloaded " + fileName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFileSaveComplete(String response) {
        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
        refresh();
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
        } else {
            new FileDownloadTask(this).execute(mCurrentDir + item.name);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICKFILE_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                String fileName = uri.getPath();
                Log.i("TAG", "You picked " + fileName);
                new SaveFileTask(this).execute(fileName, mCurrentDir);
            }
        }
    }

    @Override
    public void onFabClick(FloatingActionButton fab) {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
    }

}
