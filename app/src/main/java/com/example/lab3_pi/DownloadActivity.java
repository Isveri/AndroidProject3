package com.example.lab3_pi;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DownloadActivity extends AsyncTask<String,Integer,Integer> {
    private String adres;
    private int downloadedBytes = 0;
    private FileOutputStream stream = null;
    private File file=null;
    private HttpsURLConnection connection;

    public DownloadActivity(String adres){
        this.adres = adres;
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
            DataInputStream reader = new DataInputStream(connection.getInputStream());
            stream = new FileOutputStream(file.getPath());
            byte bufor[] = new byte[100];
            int downloaded = reader.read(bufor, 0, 100);
            while (downloaded != -1) {
                stream.write(bufor, 0, downloaded);
                downloadedBytes += downloaded;
                downloaded = reader.read(bufor, 0, 100);
                Log.d("Pobrano",String.valueOf(downloadedBytes));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
}
