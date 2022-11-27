package com.example.beguest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.beguest.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Button logoutbtn;
    private GoogleSignInAccount acct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //google
        acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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


//        logoutbtn = findViewById(R.id.logout_btn);

//        logoutbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), GoogleSignInOptions.DEFAULT_SIGN_IN);
//                googleSignInClient.signOut();
//                googleSignInClient.revokeAccess();
//                Intent i = new Intent(MainActivity.this,Login.class);
//                startActivity(i);
//            }
//        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.user_profile)
                .build();

    }

}