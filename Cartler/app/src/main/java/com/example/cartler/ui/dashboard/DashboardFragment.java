package com.example.cartler.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.cartler.R;

public class DashboardFragment extends Fragment {

    private Context context;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        context = container.getContext();
        ImageView frag1 = root.findViewById(R.id.dashboard_content);
        Glide.with(context).load(R.drawable.arduino).into(frag1);
        return root;
    }
}