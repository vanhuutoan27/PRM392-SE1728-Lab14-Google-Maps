package com.example.lab14;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btnShowMap, btnSearchMap, btnTrackMap, btnMapType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnShowMap = findViewById(R.id.btnShow);
        btnSearchMap = findViewById(R.id.btnSearch);
        btnTrackMap = findViewById(R.id.btnTrack);
        btnMapType = findViewById(R.id.btnType);

        btnShowMap.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        });

        btnSearchMap.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchMapActivity.class);
            startActivity(intent);
        });

        btnTrackMap.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TrackMapActivity.class);
            startActivity(intent);
        });

        btnMapType.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapTypeActivity.class);
            startActivity(intent);
        });
    }
}
