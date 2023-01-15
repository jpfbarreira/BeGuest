package com.example.beguest;

import static com.example.beguest.EventActivity.userAdpter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.beguest.Adapters.RegisteredUsersAdapter;
import com.example.beguest.Adapters.RegisteredUsersListAdapter;

import java.util.ArrayList;

public class RegisteredUsersListActivity extends AppCompatActivity {

    public static RegisteredUsersListAdapter userAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_users_list);

        ImageView backbtn = findViewById(R.id.back_toAtivity_btn);

        ArrayList<String> userIds =  (ArrayList<String>)getIntent().getSerializableExtra("usersRegistered");

        Log.d("UserArray", String.valueOf(userIds));

        RecyclerView recyclerView = findViewById(R.id.interested_users_recycleView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        userAdpter = new RegisteredUsersListAdapter(getApplicationContext(), userIds);
        recyclerView.setAdapter(userAdpter);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}