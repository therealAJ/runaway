package com.ivonliu.runaway;

import android.os.AsyncTask;
import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private static final String URL_BASE = "http://10.103.226.116:23523/data";

    private WeakReference<OnFetchFilesCompleteListener> listener;

    public FetchFilesTask(OnFetchFilesCompleteListener listener) {
        this.listener = new WeakReference<>(listener);
    }

    private void extractToFileList(String s, List<FileListItem> files) throws JSONException {
        JSONArray arr = new JSONArray(s);
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            boolean isDirectory = obj.getBoolean("isDirectory");
            String name = obj.getString("name");
            files.add(new FileListItem(isDirectory, name));
        }
    }

    @Override
    protected List<FileListItem> doInBackground(String... strings) {

        String url = URL_BASE + strings[0];

        HttpResponse<String> response = null;
        String s = "";
        List<FileListItem> files = new ArrayList<>();

        try {
            response = Unirest.get(url).asString();
            if (response != null) {
                s = response.getBody();
                extractToFileList(s, files);
            }
        } catch (UnirestException | JSONException e) {
            e.printStackTrace();
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
