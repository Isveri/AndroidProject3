package com.example.lab3_pi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String adres_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView adres = findViewById(R.id.adres);
        adres_url = adres.getText().toString();
        Button pobierzInf = findViewById(R.id.pobierz_inf_btn);

        pobierzInf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZadanieAsynchroniczne zadanie = new ZadanieAsynchroniczne();
                zadanie.execute(adres_url);
            }
        });

    }
}