package com.example.cartler;

import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.zerokol.views.joystickView.*;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    private TextView angleTextView;
    private TextView powerTextView;
    private TextView directionTextView;
    private JoystickView joystick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] pattern = {500, 500, 1000, 1000, 2000, 2000};
        vibrator.vibrate(pattern, 0);
        setContentView(R.layout.result);

        Button force = findViewById(R.id.force_btn);
        force.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.control);
                angleTextView = (TextView) findViewById(R.id.angleTextView);
                powerTextView = (TextView) findViewById(R.id.powerTextView);
                directionTextView = (TextView) findViewById(R.id.directionTextView);

                //Referencing also other views
                joystick = (JoystickView) findViewById(R.id.joystickView);

                //Event listener that always returns the variation of the angle in degrees, motion power in percentage and direction of movement
                joystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {
                    @Override
                    public void onValueChanged(int angle, int power, int direction) {
                        // TODO Auto-generated method stub
                        angleTextView.setText(" " + String.valueOf(angle) + "°");
                        powerTextView.setText(" " + String.valueOf(power) + "%");
                        switch (direction) {
                            case JoystickView.FRONT:
                                directionTextView.setText(R.string.front_lab);
                                break;
                            case JoystickView.FRONT_RIGHT:
                                directionTextView.setText(R.string.front_right_lab);
                                break;
                            case JoystickView.RIGHT:
                                directionTextView.setText(R.string.right_lab);
                                break;
                            case JoystickView.RIGHT_BOTTOM:
                                directionTextView.setText(R.string.right_bottom_lab);
                                break;
                            case JoystickView.BOTTOM:
                                directionTextView.setText(R.string.bottom_lab);
                                break;
                            case JoystickView.BOTTOM_LEFT:
                                directionTextView.setText(R.string.bottom_left_lab);
                                break;
                            case JoystickView.LEFT:
                                directionTextView.setText(R.string.left_lab);
                                break;
                            case JoystickView.LEFT_FRONT:
                                directionTextView.setText(R.string.left_front_lab);
                                break;
                            default:
                                directionTextView.setText(R.string.center_lab);
                        }
                    }
                }, JoystickView.DEFAULT_LOOP_INTERVAL);
            }
        });
    }
}