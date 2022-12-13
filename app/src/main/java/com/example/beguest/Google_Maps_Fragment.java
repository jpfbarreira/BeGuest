package com.example.beguest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.beguest.CreateEventFragments.Create_Event_Fragment2;
import com.example.beguest.CreateEventFragments.Create_Event_ViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.ConnectionCallbacks;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Google_Maps_Fragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private View view;
    private GoogleMap mMap;
    private View locationButton;
    private LinearLayout currentLocation;
    private double latitude;
    private double longitude;
    private Marker marker;
    public String address;

    private ImageView searchLocationBtn;
    private AutoCompleteTextView searchLocationEditText;

    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private GoogleApiClient googleApiClient;

    private Create_Event_ViewModel createEventViewModelChild;

    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 168));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_google_maps, container, false);

        createEventViewModelChild = new ViewModelProvider(getActivity()).get(Create_Event_ViewModel.class);

        // Initialize map fragment
        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        supportMapFragment.getMapAsync(this);

        currentLocation = view.findViewById(R.id.user_current_location);
        searchLocationBtn = view.findViewById(R.id.search_location_btn);
        searchLocationEditText = view.findViewById(R.id.search_location_edit_text);

        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyLocation();
            }
        });

        searchLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = searchLocationEditText.getText().toString();
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

                if (marker != null){
                    marker.remove();
                }
                if(location == null){
                    Log.d("Error", "Type a location pls");
                }else {
                    try {
                        List<Address> addressList = geocoder.getFromLocationName(location, 1);
                        if(addressList.size() > 0){
                            LatLng latLng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title("event")
                            );
                            address = addressList.get(0).getAddressLine(0);
                            mMap.animateCamera(cameraUpdate);
                            Log.d("MAPSSS", address);
                            createEventViewModelChild.setLocation(address);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        googleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(this.getActivity(), this)
                .build();

        googleApiClient.connect();


        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(getContext(), googleApiClient, LAT_LNG_BOUNDS, null);

        searchLocationEditText.setAdapter(placeAutocompleteAdapter);

        return view;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json)));
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));
        latitude = location.getLatitude();
        longitude = location.getLongitude();


        if (location != null) {
            LatLng myLocation = new LatLng(latitude,
                    longitude);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                    18));
        }

        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    address = addressList.get(0).getAddressLine(0);
                    createEventViewModelChild.setLocation(address);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (marker != null){
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("event")
                );
                mMap.animateCamera(cameraUpdate);
                if(searchLocationEditText.getText().toString() != ""){
                    searchLocationEditText.setText("");
                }
            }
        });
    }

    private void getMyLocation() {
        LatLng latLng = new LatLng((latitude), (longitude));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
    }
}