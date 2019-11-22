package com.example.cartler.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.cartler.R;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class DashboardFragment extends Fragment {

    private Context context;
    private BluetoothSPP bt;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        context = container.getContext();
        return root;
    }
}