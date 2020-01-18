package com.example.iprog;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

public class FragmentMelden extends Fragment {

    View view;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.melden, container, false);
        final Spinner parkplatz = view.findViewById(R.id.spinner_parkplatz);
        Button button = view.findViewById(R.id.btn_signup);
        parkplatz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getChildAt(0)!= null)
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE); /* if you want your item to be white */
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        parkplatz.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,new String[]{"Parkplatz 1", "Parkplatz 2"}));

        final MapsActivity activity = (MapsActivity)getActivity();
        TextView text = view.findViewById(R.id.clickable);

        final EditText kennzeichen = view.findViewById(R.id.input_email);

        final EditText melden = view.findViewById(R.id.verstoß);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(activity.main.view, "Fahrzeug wurde gemeldet", Snackbar.LENGTH_LONG).show();
                parkplatz.setSelection(0);
                kennzeichen.setText("");
                melden.setText("");
            }
        });

        return view;
    }
}
