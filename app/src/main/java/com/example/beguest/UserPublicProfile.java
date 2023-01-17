package com.example.beguest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UserPublicProfile extends AppCompatActivity {
    TextView usernameTextView, userEmailTextView, userPointsTextView;
    ImageView backBtn, userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_public_profile);

        ReadWriteUserDetails user = (ReadWriteUserDetails) getIntent().getSerializableExtra("User");
        String userId = (String) getIntent().getExtras().getString("UserId");
        StorageReference getImage = FirebaseStorage.getInstance().getReference("User Pics/" + userId + ".jpg");
        DatabaseReference reference = FirebaseDatabase.getInstance("https://beguest-4daae-default-rtdb.europe-west1.firebasedatabase.app").getReference("Registered Users");

        Log.d("Userpor", userId);

        String name = user.username;
        String points = String.valueOf(user.points);
        String email = user.email;


        usernameTextView = findViewById(R.id.username);
        userEmailTextView = findViewById(R.id.user_email);
        userPointsTextView = findViewById(R.id.user_points);
        backBtn = findViewById(R.id.back_toAtivity_btn);
        userPhoto = findViewById(R.id.user_photo);

        usernameTextView.setText(name);
        userPointsTextView.setText(points);
        userEmailTextView.setText(email);


        getImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                userPhoto.setVisibility(View.VISIBLE);
                Picasso.get().load(uri).into(userPhoto);
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}