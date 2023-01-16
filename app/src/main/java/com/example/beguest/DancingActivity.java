package com.example.beguest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DancingActivity extends AppCompatActivity {
    private ImageView backBtn;
    private TextView scoreTextView;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dancing);

        backBtn = findViewById(R.id.back_btn);
        scoreTextView = findViewById(R.id.user_points);

        Shaker shaker = new Shaker();
        shaker.Accelerometer(getApplicationContext());


        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, delay);
                scoreTextView.setText(String.valueOf(shaker.getShakeScore()));
            }
        },delay);

        scoreTextView.setText(String.valueOf(shaker.getShakeScore()));



        //sure you want to exit - not working
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog builder = new AlertDialog.Builder(DancingActivity.this)
                        .setMessage("Are you sure?")
                        .setNegativeButton("no", null)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                shaker.stopShaker();
                                handler.removeCallbacks(runnable);
                                onBackPressed();
                            }
                        })
                        .show();


            }
        });
    }


}