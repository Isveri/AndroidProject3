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
    MyCallback callback = null;

    public ZadanieAsynchroniczne(MyCallback callback) {
        this.callback = callback;
    }

    /**
     * Metoda działająca w tle ustawiające połączenie, pobierająca rozmiar
     * i typ pobieranego pliku, ustawia też wartości dla callback zeby potem z nich skorzystac
     *
     */
    @Override
    protected Integer doInBackground(String... adres) {
        String adres_url = adres[0];
        HttpsURLConnection polaczenie = null;
        try{
            URL url = new URL(adres_url);
            polaczenie = (HttpsURLConnection) url.openConnection();
            rozmiar = polaczenie.getContentLength();
            typ = polaczenie.getContentType();

            callback.updateTyp(typ);
            callback.updateSize(String.valueOf(rozmiar));
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

    /**
     * interfejs pomagający przekazac dane do głównego widoku
     */
    public interface MyCallback {
        // Declaration of the template function for the interface
        public void updateTyp(String typ);

        public void updateSize(String size);

    }
}

