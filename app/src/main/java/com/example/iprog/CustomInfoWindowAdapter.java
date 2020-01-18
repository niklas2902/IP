package com.example.iprog;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private MapsActivity context;
    FragmentMaps maps;
    int[] frei1;
    int[] frei2;
    int max1 = 300;
    int max2 = 100;
    public CustomInfoWindowAdapter(Activity context){
        this.context = (MapsActivity)context;
        maps = ((MapsActivity) context).main.fragmentMaps;
        frei1 =new int[]{290,290,280,280,270,250,240,160,130,120,100,80,50,30,20,40,80,100,140,180,200,240,270,280,280};

        frei2 =new int[]{90,90,80,80,70,50,40,60,30,20,10,8,5,3,2,4,80,10,40,80,20,40,70,80,80};
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.custominfowindow, null);

        TextView belegt = (TextView) view.findViewById(R.id.belegt);
        TextView frei = (TextView) view.findViewById(R.id.frei);
        TextView parkplatz = view.findViewById(R.id.title_parkplatz);

        if(marker.getTitle().equals("1")){
            belegt.setText(""+(max1 - frei1[maps.hour]));
            frei.setText("" + frei1[maps.hour]);

            parkplatz.setText("Parkplatz 1");
        }

        if(marker.getTitle().equals("2")){
            belegt.setText(""+(max2 - frei2[maps.hour]));
            frei.setText("" + frei2[maps.hour]);
            parkplatz.setText("Parkplatz 2");
        }
        Button button = view.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("hallo2");
            }
        });

        //tvTitle.setText(marker.getTitle());
        //tvSubTitle.setText(marker.getSnippet());

        return view;
    }
}