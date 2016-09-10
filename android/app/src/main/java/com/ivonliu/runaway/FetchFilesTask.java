package com.ivonliu.runaway;

import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivon on 9/10/16.
 */
public class FetchFilesTask extends AsyncTask<String, Void, List<FileListItem>> {

    public interface OnFetchFilesCompleteListener {
        void onFetchFilesComplete(List<FileListItem> files);
    }

    private WeakReference<OnFetchFilesCompleteListener> listener;

    public FetchFilesTask(OnFetchFilesCompleteListener listener) {
        this.listener = new WeakReference<>(listener);
    }

    @Override
    protected List<FileListItem> doInBackground(String... strings) {
        List<FileListItem> files = new ArrayList<>();
        for (int i=0; i<10; i++) {
            files.add(new FileListItem(i%2==0, "File (" + strings[0] + ") " + i));
        }
        return files;
    }

    @Override
    protected void onPostExecute(List<FileListItem> files) {
        if (listener.get() != null) {
            Log.i("TAG", files.toString());
            listener.get().onFetchFilesComplete(files);
        }
    }
}
