package com.example.iprog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentRegister extends Fragment {

    View view;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.register, container, false);

        final MapsActivity activity = (MapsActivity)getActivity();
        TextView text = view.findViewById(R.id.clickable);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.change(activity.login);
            }
        });


        return view;
    }
}
