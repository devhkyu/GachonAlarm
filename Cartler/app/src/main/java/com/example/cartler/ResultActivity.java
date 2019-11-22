package com.example.cartler;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        final Intent service = new Intent(getApplicationContext(), ServiceClass.class);
        service.setPackage("com.example.cartler");
        Button force = findViewById(R.id.force_btn);
        Button start = findViewById(R.id.force_start);
        Button stop = findViewById(R.id.force_stop);
        force.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, DeviceScanActivity.class);
                startActivity(intent);
            }
        });
        start.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(service);
                startService(service);
                Toast.makeText(getApplicationContext(), "Service has started!", Toast.LENGTH_SHORT).show();
            }
        });
        stop.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(service);
                Toast.makeText(getApplicationContext(), "Alarm has stopped!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}