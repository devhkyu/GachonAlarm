package com.example.cartler.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.cartler.R;
import com.example.cartler.ResultActivity;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        context = container.getContext();
        Calendar calendar = Calendar.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences("daily alarm", MODE_PRIVATE);
        long date = sharedPreferences.getLong("nextNotifyTime", (long) calendar.getTimeInMillis());
        Toast.makeText(context, "" + date, Toast.LENGTH_SHORT).show();

        Button test = root.findViewById(R.id.test_btn);
        test.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ResultActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}