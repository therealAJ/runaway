package com.ivonliu.runaway;

import android.os.AsyncTask;
import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by ivon on 9/11/16.
 */
public class SaveFileTask extends AsyncTask<String, Void, String> {

    public interface OnSaveFileCompleteListener {
        void onFileSaveComplete(String response);
    }

    private static final String URL_BASE = "http://10.103.226.116:23523/fileupload";

    private WeakReference<OnSaveFileCompleteListener> listener;

    public SaveFileTask(OnSaveFileCompleteListener listener) {
        this.listener = new WeakReference<>(listener);
    }

    @Override
    protected String doInBackground(String... params) {

        File file = new File(params[0]);
        String url = URL_BASE + params[1];

        HttpResponse<String> response = null;
        String s = "";
        try {
            response = Unirest.post(url)
                    .field("fileUpload", file)
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        if (response != null) {
            s = response.getBody();
        }
        return s;
    }

    @Override
    public void onPostExecute(String response) {
        if (listener.get() != null) {
            Log.i("TAG", "Received response: " + response);
            listener.get().onFileSaveComplete(response);
        }
    }
}
