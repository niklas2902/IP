package com.example.iprog;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.List;

public class FragmentMaps extends Fragment implements OnMapReadyCallback{
    public View view;
    GoogleMap mGoogleMap;
    MapView mapView;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker1;
    Marker mCurrLocationMarker2;
    FusedLocationProviderClient mFusedLocationClient;
    MapsActivity activity;
    EditText hidden ;
    EditText editText;
    int hour = 0;
    int minute = 0;
    int hour_res = 0;

    LinearLayout hidden_layout;

    int max1 = 300;
    int max2 = 100;

    int[] frei1;
    int[] frei2;
    TextView title;
    TextView belegt;
    TextView frei;

    int reserviert =-1;
    int selected = 0;
    Button reservieren;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.activity_maps, container, false);
        activity = (MapsActivity)getActivity();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient((MapsActivity) getActivity());

        title = view.findViewById(R.id.title);
        belegt = view.findViewById(R.id.belegt);
        frei = view.findViewById(R.id.frei);

        mapView = view.findViewById(R.id.mapView);;
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle("AIzaSyCmwX7PNTnArY1c9y4jMRRbRofi0uf883E");
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        final TimeDialog dialogTime = new TimeDialog(this);

        editText = view.findViewById(R.id.ankunft);
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        editText.setText(c.get(Calendar.HOUR_OF_DAY) + ":" + (c.get(Calendar.MINUTE) <10 ?"0":"")+c.get(Calendar.MINUTE));
        hidden = view.findViewById(R.id.hidden);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                    dialogTime.show(getChildFragmentManager());
            }
        });

        frei1 =new int[]{290,290,280,280,270,250,240,160,130,120,100,80,50,30,20,40,80,100,140,180,200,240,270,280,280};

        frei2 =new int[]{90,90,80,80,70,50,40,60,30,20,10,8,5,3,2,4,80,10,40,80,20,40,70,80,80};

        hidden_layout = view.findViewById(R.id.info);
        reservieren = view.findViewById(R.id.button2);

        reservieren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected == reserviert) {
                    reserviert = -1;
                    hour_res = hour;
                    reservieren.setText("Reservieren");
                }
                else {
                    reserviert = selected;
                    hour_res = hour;
                    reservieren.setText("Freigeben");
                }
                hour_res = hour;
                System.out.println("hallo:" + reserviert);
                if(reserviert!= -1) {
                    Snackbar.make(activity.main.view, "Parkplatz " + reserviert + " wurde für 2 Stunden reserviert", Snackbar.LENGTH_LONG).show();
                }
                else{
                    Snackbar.make(activity.main.view, "Parkplatz " + selected + " wurde freigegeben", Snackbar.LENGTH_LONG).show();

                }
                rebuild_loction_markers();
            }
        });

        return view;
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle("AIzaSyCmwX7PNTnArY1c9y4jMRRbRofi0uf883E");
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle("AIzaSyCmwX7PNTnArY1c9y4jMRRbRofi0uf883E", mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker2 != null) {
                    mCurrLocationMarker2.remove();
                }
                if(mCurrLocationMarker1!= null){
                    mCurrLocationMarker1.remove();
                }
                System.out.println("pos1:"+mLastLocation.getLongitude());
                System.out.println("pos2:"+mLastLocation.getLatitude());

                //Place current location marker
                LatLng latLng = new LatLng(49.1227, 9.208);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("1");
                if(reserviert == 1 && Math.abs(hour- hour_res) < 2){
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_res));
                }
                else if(frei1[hour] < max1/4) {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_neg));
                }
                else if (frei1[hour] < max2/2){
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_mid));
                }
                else{
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_pos));
                }
                mCurrLocationMarker1 = mGoogleMap.addMarker(markerOptions);

                //Place current location marker
                LatLng latLng2 = new LatLng(49.1212, 9.2138);
                MarkerOptions markerOptions2 = new MarkerOptions();
                markerOptions2.position(latLng2);
                markerOptions2.title("2");
                if(reserviert == 2 && Math.abs(hour- hour_res) < 2){
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_res));
                }
                else if(frei2[hour] < max2/4) {
                    markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_neg));
                }
                else if (frei2[hour] < max2/2){
                    markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_mid));
                }
                else{
                    markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_pos));
                }
                markerOptions2.snippet("hallo\nhallo2");
                mCurrLocationMarker2 = mGoogleMap.addMarker(markerOptions2);

                mGoogleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity()));
                //mGoogleMap.setInfoWindowAdapter(null);
                mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        System.out.println("hallo_" + marker.getTitle());
                        hidden_layout.setVisibility(View.VISIBLE);
                        if(marker.getTitle().equals("1")){
                            title.setText("Parkplatz 1");
                            frei.setText(""+ frei1[hour]);
                            belegt.setText(""+ (max1- frei1[hour] ) );
                            selected = 1;
                            if(reserviert == 1){
                                reservieren.setText("Freigeben");
                            }
                            else{
                                reservieren.setText("Reservieren");
                            }
                        }


                        else if(marker.getTitle().equals("2")){
                            title.setText("Parkplatz 2");
                            frei.setText(""+ frei2[hour]);
                            belegt.setText(""+ (max2- frei2[hour] ) );
                            selected = 2;

                            if(reserviert == 2){
                                reservieren.setText("Freigeben");
                            }
                            else{
                                reservieren.setText("Reservieren");
                            }
                        }
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                        return false;
                    }
                });
                mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        hidden_layout.setVisibility(View.GONE);
                    }
                });
                //move map camera
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                System.out.println("hallo");
            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    public void rebuild_loction_markers(){
        mCurrLocationMarker1.remove();
        mCurrLocationMarker2.remove();

        //Place current location marker
        LatLng latLng = new LatLng(49.1227, 9.208);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("1");
        if(reserviert == 1 && Math.abs(hour- hour_res) < 2){
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_res));
        }
        else if(frei1[hour] < max1/4) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_neg));
        }
        else if (frei1[hour] < max2/2){
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_mid));
        }
        else{
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_pos));
        }
        mCurrLocationMarker1 = mGoogleMap.addMarker(markerOptions);

        //Place current location marker
        LatLng latLng2 = new LatLng(49.1212, 9.2138);
        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(latLng2);
        markerOptions2.title("2");
        if(reserviert == 2 && Math.abs(hour- hour_res) < 2){
            markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_res));
        }
        else if(frei2[hour] < max2/4) {
            markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_neg));
        }
        else if (frei2[hour] < max2/2){
            markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_mid));
        }
        else{
            markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_pos));
        }
        markerOptions2.snippet("hallo\nhallo2");
        mCurrLocationMarker2 = mGoogleMap.addMarker(markerOptions2);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Activity mapsActivity = getActivity();
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(mapsActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                }
                return;
            }
        }
    }

    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {

        View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    private Bitmap getMarkerBitmapFromView2(@DrawableRes int resId) {

        View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker_pos, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }
}
