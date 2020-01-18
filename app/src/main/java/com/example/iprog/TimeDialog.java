package com.example.iprog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class TimeDialog extends DialogFragment {
    View v;
    long date;
    Calendar c;
    Calendar c_compare;
    FragmentMaps maps;
    EditText hidden;
    TimeDialog(FragmentMaps m){
        maps = m;

    }


    public void show(FragmentManager manager) {
        super.show(manager, "dialog");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_picker, container,
                false);
        v = rootView;
        final MapsActivity activity = (MapsActivity)getActivity();
        Window window = getDialog().getWindow();

        // set "origin" to top left corner, so to speak
        window.setGravity(Gravity.TOP);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // after that, setting values for x and y works "naturally"
        WindowManager.LayoutParams params = window.getAttributes();
        params.y = 100;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button accept = rootView.findViewById(R.id.button_accept);
        final TimePicker timeDialog = v.findViewById(R.id.timePicker);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = timeDialog.getHour();
                int minute = timeDialog.getMinute();
                maps.minute = minute;
                maps.hour = hour;
                maps.editText.setText(hour + ":" + (minute <10 ?"0":"")+minute);
                TimeDialog.this.dismiss();
                //maps.editText.clearFocus();
                maps.mapView.onPause();
                maps.hidden.requestFocus();
                maps.rebuild_loction_markers();
            }
        });

        Button deny = rootView.findViewById(R.id.button_deny);
        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeDialog.this.dismiss();
                maps.hidden.requestFocus();
            }
        });
        return rootView;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        maps.hidden.requestFocus();
        System.out.println("Destroy");
    }
}
