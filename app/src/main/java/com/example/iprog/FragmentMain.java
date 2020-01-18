package com.example.iprog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.navigation.NavigationView;

public class FragmentMain extends Fragment implements com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener{
    View view;
    FragmentMaps fragmentMaps = new FragmentMaps();
    Fragment newFragment;
    FragmentMelden melden = new FragmentMelden();
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.main_view, container, false);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragmentMaps).commit();
        newFragment = fragmentMaps;

        com.google.android.material.bottomnavigation.BottomNavigationView navigationMenu = view.findViewById(R.id.bottomNavigationView);
        navigationMenu.setOnNavigationItemSelectedListener(this);


        return view;
    }

    public void change(Fragment f){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(newFragment.getId(),f).commit();
        newFragment = f;

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.navigation_map){
            change(fragmentMaps);
        }
        else{
            change(melden);
        }
        return true;
    }
}
