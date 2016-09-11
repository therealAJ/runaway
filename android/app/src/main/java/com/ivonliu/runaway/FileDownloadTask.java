package com.ivonliu.runaway;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by ivon on 9/11/16.
 */
public class FileDownloadTask extends AsyncTask<String, Void, InputStream> {

    public interface OnFileDownloadCompleteListener {
        void onFileDownloadComplete(String fileName);
    }

    private static final String URL_BASE = "http://10.103.226.116:23523/data";

    private WeakReference<OnFileDownloadCompleteListener> listener;

    public FileDownloadTask(OnFileDownloadCompleteListener listener) {
        this.listener = new WeakReference<>(listener);
    }

    private String mFileName = "";

    @Override
    protected InputStream doInBackground(String... strings) {
        String url = URL_BASE + strings[0];

        String[] sections = strings[0].split("/");
        mFileName = sections[sections.length-1];

        InputStream content = null;

        try {
            content = Unirest.get(url).asBinary().getRawBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return content;
    }

    @Override
    protected void onPostExecute(InputStream is) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), mFileName));
            int read = 0;
            byte[] buffer = new byte[32768];
            while( (read = is.read(buffer)) > 0) {
                fos.write(buffer, 0, read);
            }
            fos.close();
            is.close();
            if (listener.get() != null) {
                Log.i("TAG", "Successfully downloaded " + mFileName);
                listener.get().onFileDownloadComplete(mFileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
