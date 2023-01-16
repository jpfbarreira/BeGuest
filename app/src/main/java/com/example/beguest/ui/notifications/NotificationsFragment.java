package com.example.beguest.ui.notifications;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beguest.Adapters.HomeEventsAdapter;
import com.example.beguest.CreateEventFragments.Event;
import com.example.beguest.EventActivity;
import com.example.beguest.MapFilters;
import com.example.beguest.PlaceAutocompleteAdapter;
import com.example.beguest.R;
import com.example.beguest.databinding.FragmentNotificationsBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NotificationsFragment extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private FragmentNotificationsBinding binding;
    private GoogleMap mMap;
    MapView mapView;
    FusedLocationProviderClient client;
    double userLat;
    double userLong;
    int distanceFilter;
    private ActivityResultLauncher<String[]> activityResultLauncher;
    private ImageView searchLocationBtn;
    private AutoCompleteTextView searchLocationEditText;
    private LinearLayout currentLocation;
    private LinearLayout filterBtn;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private GoogleApiClient googleApiClient;
    private Marker marker;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 168));
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    public static HomeEventsAdapter eventAdpter;
    private List<Marker> AllMarkers = new ArrayList<Marker>();
    private List<String> AlleventID = new ArrayList<String>();
    private List<Integer> AlleventPosition = new ArrayList<Integer>();
    private int id;
    private int position = 0;
    ArrayList<Event> events = new ArrayList<Event>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        IntentFilter filter = new IntentFilter("data");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                distanceFilter = intent.getIntExtra("distance", 15);
                Log.d("distanceFilter", String.valueOf(distanceFilter));
                getEventsLocations();
            }
        };
        getActivity().registerReceiver(receiver,filter);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mapView = root.findViewById(R.id.google_map);
        mapView.onCreate(savedInstanceState);
        client = LocationServices.getFusedLocationProviderClient(getActivity());

        Dexter.withContext(getActivity().getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getMyLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

        currentLocation = root.findViewById(R.id.user_current_location);
        filterBtn = root.findViewById(R.id.distance_filter);
        searchLocationBtn = root.findViewById(R.id.search_location_btn);
        searchLocationEditText = root.findViewById(R.id.search_location_edit_text);

        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyLocation();
            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationsFragment.this.getContext(), MapFilters.class);
                startActivity(intent);
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
                                    .title(addressList.get(0).getAddressLine(0))
                            );
                            mMap.animateCamera(cameraUpdate);
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

        return root;
    }

    public void getMyLocation() {


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {

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
                        if (location != null) {

                            userLat = location.getLatitude();
                            userLong = location.getLongitude();

                            LatLng latLng = new LatLng(userLat, userLong);
                            //MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("lat: "+String.valueOf(userLat) + " long: "+String.valueOf(userLong));
                            //googleMap.addMarker(markerOptions);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    int tag = (int)(marker.getTag());
                                    Event event = events.get(AlleventPosition.get(tag));
                                    Log.d("position", String.valueOf(tag));

                                    Intent intent = new Intent(getActivity(), EventActivity.class);
                                    intent.putExtra("Event", event);
                                    intent.putExtra("EventId", AlleventID.get(tag));
                                    getActivity().startActivity(intent);
                                    return true;
                                }
                            });

                            getEventsLocations();
                        }
                    }
                });
            }
        });
    }

    public void getEventsLocations() {

        removeAllMarkers();

        reference = FirebaseDatabase.getInstance("https://beguest-4daae-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Registered Events");

        //conseguir id dos eventos
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    String eventID = dataSnapshot.getKey();
                    Log.d("eventID", eventID);
                    Event event = dataSnapshot.getValue(Event.class);
                    events.add(event);

                    DatabaseReference eventReference = reference.child(eventID);
                    DatabaseReference locationsRef = eventReference.child("location");

                    //conseguir localizacao(string) dos eventos
                    eventReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                                Log.d("location", "Location is: " + map.get("location"));
                            try {
                                addressToCoords(map.get("location").toString(), eventID, position);
                                position++;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void addressToCoords(String address, String eventID, int position) throws IOException {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addressList = geocoder.getFromLocationName(address, 1);
        LatLng latLng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
        Log.d("latLng is ", latLng.toString());
        calcDistance(latLng, eventID, position);
    }

    public void calcDistance(LatLng latLng, String eventID, int position) {
        final double AVERAGE_RADIUS_OF_EARTH_KM = 6371;
        double latDistance = Math.toRadians(userLat - latLng.latitude);
        double lngDistance = Math.toRadians(userLong - latLng.longitude);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(latLng.latitude))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double km = Math.round(AVERAGE_RADIUS_OF_EARTH_KM * c);
        Log.d("calcDistance", "Distance is " + String.valueOf(km));

        if(km <= distanceFilter) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);

            Marker mLocationMarker = mMap.addMarker(markerOptions); // add the marker to Map
            mLocationMarker.setTag(id);

            AllMarkers.add(mLocationMarker); // add the marker to array
            AlleventID.add(eventID);
            AlleventPosition.add(position);
            Log.d("marker", "Marker added");
            Log.d("marker", eventID);
            id++;
        }
    }

    private void removeAllMarkers() {
        for (Marker mLocationMarker: AllMarkers) {
            mLocationMarker.remove();
        }
        AllMarkers.clear();
        AlleventID.clear();
        id = 0;

    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}

