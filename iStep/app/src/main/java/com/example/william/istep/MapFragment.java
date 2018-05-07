package com.example.william.istep;

import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * This class contains the app's map functionality. A terrain - type google map is inflated and presents
 * the user with a number of marker pins, identifying local walks in Belfast and their corresponding length.
 */
public class MapFragment extends Fragment {

    /**
     * Declare MapView and GoogleMap instances.
     */
    MapView mapView;
    private GoogleMap googleMap;

    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;



    /**
     * Default constructor.
     */
    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for the map fragment
        View inflatedView = inflater.inflate(R.layout.map_fragment, container, false);

        mapView = (MapView) inflatedView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);


        mapView.onResume();

        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }

        /**
         * Call the getMapASync method to automatically initialise the maps system and view. The method will
         * contain variables for the latitude and longitude coordinates of local walks in Belfast. The method then places markers on the
         * map which correspond with the coordinates. Finally, the method places the initial camera view at one of the markers (Belvoir Forest Park).
         * on the map.
         */
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

                /**
                 * declare the latitude and longitude of a sample of local walks in Belfast
                 */
                LatLng belvoir_park_forest = new LatLng(54.557569, -5.928073);
                LatLng belfast_castle = new LatLng(54.642885, -5.942280);
                LatLng botanic_gardens = new LatLng(54.583152, -5.936382);
                LatLng clement_wilson = new LatLng(54.553897, -5.953029);
                LatLng colin_glen = new LatLng(54.566277, -6.014403);
                LatLng divis_heath = new LatLng(54.600926, -6.041597);
                LatLng lagan_meadows = new LatLng(54.562855, -5.936816);
                LatLng ormeau_park = new LatLng(54.585101, -5.914634);


                /**
                 * add a marker for each walk on the map, referring to the latitude and longitude coordinates above
                 */
                googleMap.addMarker(new MarkerOptions().position(belvoir_park_forest).title("Belvoir Park Forest").snippet("1.5 miles"));
                googleMap.addMarker(new MarkerOptions().position(belfast_castle).title("Belfast Castle & Cave Hill").snippet("2.4 miles"));
                googleMap.addMarker(new MarkerOptions().position(botanic_gardens).title("Botanic Gardens").snippet("0.8 miles"));
                googleMap.addMarker(new MarkerOptions().position(clement_wilson).title("Clement Wilson").snippet("1.2 miles"));
                googleMap.addMarker(new MarkerOptions().position(colin_glen).title("Colin Glen").snippet("4 miles"));
                googleMap.addMarker(new MarkerOptions().position(divis_heath).title("Divis Heath").snippet("4 miles"));
                googleMap.addMarker(new MarkerOptions().position(lagan_meadows).title("Lagan Meadows").snippet("2.2 miles"));
                googleMap.addMarker(new MarkerOptions().position(ormeau_park).title("Ormeau Park").snippet("1.3 miles"));


                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(belvoir_park_forest).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return inflatedView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


}