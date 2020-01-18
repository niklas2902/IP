package com.example.iprog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentLogin extends Fragment {

    View view;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.login, container, false);
        Button login = view.findViewById(R.id.btn_login);
        final MapsActivity activity = (MapsActivity)getActivity();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.change(activity.main);
            }
        });

        TextView textView = view.findViewById(R.id.clickable);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.change(activity.register);
            }
        });


        return view;
    }
}
