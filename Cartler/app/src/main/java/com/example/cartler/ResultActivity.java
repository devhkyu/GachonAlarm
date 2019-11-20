package com.example.cartler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] pattern = {500, 500, 1000, 1000, 2000, 2000};
        vibrator.vibrate(pattern, -1);
        setContentView(R.layout.result);

        Button force = findViewById(R.id.force_btn);
        force.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, ControlActivity.class);
                startActivity(intent);
            }
        });
    }
}