package com.example.beguest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.beguest.CreateEventFragments.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

public class DancingActivity extends AppCompatActivity {
    private ImageView backBtn;
    private TextView scoreTextView;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 1000;
    int points;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dancing);

        backBtn = findViewById(R.id.back_btn);
        scoreTextView = findViewById(R.id.user_points);

        Shaker shaker = new Shaker();
        shaker.Accelerometer(getApplicationContext());

        UserPoints userPoints = (UserPoints) getIntent().getSerializableExtra("UserPoints");

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
                                int currentPoints = shaker.getShakeScore();
                                userPoints.setPoints(String.valueOf(currentPoints));


                                DatabaseReference reference = FirebaseDatabase.getInstance("https://beguest-4daae-default-rtdb.europe-west1.firebasedatabase.app")
                                        .getReference("Registered Users");
                                DatabaseReference eventReference = reference.child(userPoints.getId());

                                eventReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);

                                        points = readUserDetails.points + currentPoints;

                                        eventReference.child("points").setValue(points);
                                        Log.d("userdetails", String.valueOf(readUserDetails.points));
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                Intent intent = new Intent(DancingActivity.this, EventActivity.class);
                                intent.putExtra("userPoints", String.valueOf(currentPoints));
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        })
                        .show();


            }
        });
    }


}