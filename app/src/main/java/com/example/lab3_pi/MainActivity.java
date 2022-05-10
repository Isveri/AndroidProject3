package com.example.lab3_pi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements ZadanieAsynchroniczne.MyCallback {

    private String adres_url;
    private TextView bytes;
    private ProgressBar progressBar;
    private TextView status;
    private TextView type;
    private TextView size;

    /**
     * Obiekt klasy BroadcastReceiver oraz nadpisanie metody onRecieve()
     * W przypadku gdy otrzymany zostanie broadcast pobrane zostaną przekazane dane
     * oraz ustawione odpowiednie pola na ekranie tj. ilosc pobranych bytow, progress bar
     * oraz aktualny status
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            ProgressInfo w = bundle.getParcelable(DownloadActivity.INFO);
            int downloadedData = w.getDownloadedBytes();
            int size = w.getSize();
            String st = w.getStatus();
            bytes = findViewById(R.id.bajty);
            bytes.setText(String.valueOf(downloadedData));
            progressBar = findViewById(R.id.progressBar);
            progressBar.setMax(size);
            progressBar.setProgress(downloadedData);
            status = findViewById(R.id.status);
            status.setText(st);
        }
    };

    @Override
    protected void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,new IntentFilter((DownloadActivity.NOTIFICATION)));
    }

    @Override
    protected void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }


    /**
     * ustawienie wszystkich niezbędnych pól, listenerów przycisków itp.
     * przy tworzeniu widoku.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextInputEditText adres = findViewById(R.id.adresText);
        adres.setText("https://cdn.kernel.org/pub/linux/kernel/v5.x/linux-5.4.36.tar.xz");
        adres_url = adres.getText().toString();
        Button pobierzInf = findViewById(R.id.pobierz_inf_btn);
        //ZadanieAsynchroniczne zadanie = new ZadanieAsynchroniczne(this);
        Button pobierzPlik = findViewById(R.id.pobierz_plik_btn);
        type = findViewById(R.id.textView5);
//        type.setText("0");
        size = findViewById(R.id.rozmiar);
//        size.setText("0");

        if(savedInstanceState!=null){
            String savedOne = savedInstanceState.getString("TYPE");
            type.setText(savedOne);
            String savedTwo = savedInstanceState.getString("SIZE");
            size.setText(savedTwo);
        }

        pobierzInf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZadanieAsynchroniczne zadanie = new ZadanieAsynchroniczne(MainActivity.this);
                adres_url = adres.getText().toString();
                zadanie.execute(adres_url);
            }
        });

        pobierzPlik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    askForPermission();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     *
     * Nadpisanie metod interfejsu stworzonego w klasie ZadanieAsynchroniczne
     * w celu ustawienia wartości na ekranie.
     */
    @Override
    public void updateTyp(String adres) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.textView5)).setText(adres);
            }
        });

    }

    @Override
    public void updateSize(String size) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.rozmiar)).setText(size);
            }
        });

    }

    /**
     * Metoda onSaveInstanceState dzięki której po przekręceniu ekranu nie znikną informacje
     * na temat typu i rozmiaru pliku.
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("SIZE", size.getText().toString());
        savedInstanceState.putString("TYPE", type.getText().toString());
    }

    /**
     * Funkcja pytająca o dostep do storage'a w telefonie
     */
    private void askForPermission() throws IOException {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            MyIntentService.startTask(MainActivity.this,adres_url);

        }else{
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            }
            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }

}