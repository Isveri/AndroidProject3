package com.example.lab3_pi;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DownloadActivity extends AsyncTask<String,Integer,Integer> {
    private String adres;
    private int downloadedBytes = 0;
    private FileOutputStream stream = null;
    private File file=null;
    private HttpsURLConnection connection;
    public final static String NOTIFICATION = "com.example.intent_service.odbiornik";
    public final static String INFO = "info";
    private Context contextRef;

    public DownloadActivity(String adres,Context context){
        this.adres = adres;
        this.contextRef = context;
    }

    public void Download() {
        try {
            URL url = new URL(adres);
            connection = (HttpsURLConnection) url.openConnection();
            File temp = new File(url.getFile());
            file = new File(Environment.getExternalStorageDirectory() + File.separator + temp.getName());
            if (file.exists()) file.delete();
        }catch(Exception s) {
            s.printStackTrace();
        }
        try{
            ProgressInfo progress;
            DataInputStream reader = new DataInputStream(connection.getInputStream());
            stream = new FileOutputStream(file.getPath());
            byte bufor[] = new byte[100];
            int downloaded = reader.read(bufor, 0, 100);
            while (downloaded != -1) {
                stream.write(bufor, 0, downloaded);
                downloadedBytes += downloaded;
                downloaded = reader.read(bufor, 0, 100);
                progress = new ProgressInfo(downloadedBytes,"Pobieranie trwa",connection.getContentLength());
                sendInfo(progress,contextRef);
                Log.d("Pobrano",String.valueOf(downloadedBytes));
            }
            progress = new ProgressInfo(downloadedBytes,"Pobieranie zakonczone",connection.getContentLength());
            sendInfo(progress,contextRef);

        } catch (Exception e) {
            e.printStackTrace();
            ProgressInfo progress = new ProgressInfo(downloadedBytes,"Błąd",connection.getContentLength());
            sendInfo(progress,contextRef);
        }
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) connection.disconnect();
            }
        }
    }

    @Override
    protected Integer doInBackground(String... strings) {
        Download();
        return null;
    }

    private void sendInfo(ProgressInfo info, Context context){
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(INFO,info);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

}
