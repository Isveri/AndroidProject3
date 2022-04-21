package com.example.lab3_pi;

import android.os.AsyncTask;
import android.widget.EditText;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ZadanieAsynchroniczne extends AsyncTask<String,Integer,Integer> {
    private int rozmiar;
    private String typ;
    @Override
    protected Integer doInBackground(String... adres) {
        String adres_url = adres[0];
        HttpsURLConnection polaczenie = null;
        try{
            URL url = new URL(adres_url);
            polaczenie = (HttpsURLConnection) url.openConnection();
            rozmiar = polaczenie.getContentLength();
            typ = polaczenie.getContentType();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(polaczenie!=null) polaczenie.disconnect();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

    }

    @Override
    protected void onPostExecute(Integer result) {

    }
}
