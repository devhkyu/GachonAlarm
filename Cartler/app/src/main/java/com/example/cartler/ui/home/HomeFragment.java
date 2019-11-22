package com.example.cartler.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.cartler.R;
import com.example.cartler.ResultActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {
    UIThread U;
    UIHandler u;
    String state;
    private Context context;
    private TextView tv1, tv2;
    private Button test;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        context = container.getContext();
        tv1 = root.findViewById(R.id.title_alarm_time);
        tv2 = root.findViewById(R.id.currentTime);
        test = root.findViewById(R.id.test_btn);

        u = new UIHandler();
        state = "Active";
        U = new UIThread();
        U.start();

        Calendar calendar = Calendar.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences("daily alarm", MODE_PRIVATE);
        long date = sharedPreferences.getLong("nextNotifyTime", (long) calendar.getTimeInMillis());
        String alarm_time = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(date);
        tv1.setText(alarm_time);

        test.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ResultActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
    public void onStop(){
        super.onStop();
        U.interrupt();
    }
    public void onResume(){
        super.onResume();
        state = "Active";
    }
    private class UIHandler extends Handler {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case 1:
                    if(state.equals("DeActive")) //Fragment가 숨겨진 상태일 때
                        break;
                    Calendar calendar = Calendar.getInstance();
                    Date currentDateTime = calendar.getTime();
                    String date_text = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(currentDateTime);
                    tv2.setText(date_text);
            }
        }
    }
    private class UIThread extends Thread{
        Message msg;
        boolean loop = true;
        public void run() {
            try {
                while (loop) {
                    Thread.sleep(100);
                    if(Thread.interrupted()){ //인터럽트가 들어오면 루프를 탈출합니다.
                        loop = false;
                        break;
                    }
                    msg = u.obtainMessage();
                    msg.arg1 = 1;
                    u.sendMessage(msg);
                }
            } catch (InterruptedException e) {//sleep 상태에서 인터럽트가 들어오면 exception 발생
                // TODO Auto-generated catch block
                loop = false;
            }
        }
    }
}