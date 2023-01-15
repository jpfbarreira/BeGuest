package com.example.beguest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.beguest.ui.notifications.NotificationsFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.xw.repo.BubbleSeekBar;

import java.util.Random;

public class MapFilters extends AppCompatActivity {

    private ImageView back_btn;
    private BubbleSeekBar bubbleSeekBar;
    private Button save_btn;
    private int distance_progress;

    public MapFilters() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_filters);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        bubbleSeekBar = findViewById(R.id.demo_1_seek_bar);
        save_btn = findViewById(R.id.save_btn);
        back_btn = findViewById(R.id.back_btn);

        distance_progress = pref.getInt("distance_progress", 15);
        bubbleSeekBar.setProgress(distance_progress);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distance_progress = (int) bubbleSeekBar.getProgress();
                bubbleSeekBar.setProgress(distance_progress);
                editor.putInt("distance_progress", distance_progress);
                editor.commit();

                Intent intent = new Intent("data");
                intent.putExtra("distance", distance_progress);
                MapFilters.this.sendBroadcast(intent);

                onBackPressed();
            }
        });

    }
}