package com.example.lab3_pi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity implements ZadanieAsynchroniczne.MyCallback {

    private String adres_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextInputEditText adres = findViewById(R.id.adresText);
        adres.setText("https://cdn.kernel.org/pub/linux/kernel/v5.x/linux-5.4.36.tar.xz");
        adres_url = adres.getText().toString();
//        adres_url = "https://cdn.kernel.org/pub/linux/kernel/v5.x/linux-5.4.36.tar.xz";
        Button pobierzInf = findViewById(R.id.pobierz_inf_btn);
        ZadanieAsynchroniczne zadanie = new ZadanieAsynchroniczne(this);

        pobierzInf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adres_url = adres.getText().toString();
                zadanie.execute(adres_url);
            }
        });


    }

    @Override
    public void updateTyp(String adres) {
        new Handler(Looper.getMainLooper()).post(new Runnable(){
            @Override
            public void run() {
                ((TextView) findViewById(R.id.textView5)).setText(adres);
            }
        });

    }

    @Override
    public void updateSize(String size) {
        new Handler(Looper.getMainLooper()).post(new Runnable(){
            @Override
            public void run() {
                ((TextView) findViewById(R.id.rozmiar)).setText(size);
            }
        });

    }
}