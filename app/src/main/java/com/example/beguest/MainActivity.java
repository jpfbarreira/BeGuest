package com.example.beguest;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.beguest.ui.dashboard.DashboardFragment;
import com.example.beguest.ui.home.HomeFragment;
import com.example.beguest.ui.notifications.NotificationsFragment;
import com.example.beguest.ui.profile.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.beguest.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.squareup.picasso.Picasso;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private GoogleSignInAccount acct;

    private SharedViewModel sharedViewModel;

    private final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        requestLocationPermission();

        ChipNavigationBar navigationViewCustom = findViewById(R.id.nav_view_costum);
        BottomNavigationView navigationView = findViewById(R.id.nav_view);
        navigationViewCustom.setItemSelected(R.id.navigation_home, true);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        navigationViewCustom.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                switch (id){
                    case R.id.navigation_dashboard:
                        navigationView.setSelectedItemId(id);
                        break;
                    case R.id.navigation_home:
                        navigationView.setSelectedItemId(id);
                        break;
                    case R.id.navigation_notifications:
                        navigationView.setSelectedItemId(id);
                        break;
                    case R.id.user_profile:
                        navigationView.setSelectedItemId(id);
                        break;
                }
            }
        });
        NavigationUI.setupWithNavController(binding.navView, navController);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.user_profile)
                .build();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {
            Log.d("Permissions", "Permission already granted");
        }
        else {
            Log.d("Permissions", "Please grant the location permission");
        }
    }
}