package com.example.beguest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.beguest.CreateEventFragments.Event;
import com.example.beguest.ui.home.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UriRedirect extends AppCompatActivity {
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uri_redirect);

        Uri uri = getIntent().getData();
        if(uri!=null) {
            List<String> params = uri.getPathSegments();
            id = params.get(params.size()-1);
            getEventInfo(id);
            //Toast.makeText(this,"id: "+id, Toast.LENGTH_SHORT).show();
        }

    }

    public void getEventInfo(String eventId) {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://beguest-4daae-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Registered Events");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String eventID = dataSnapshot.getKey();
                    Event event = dataSnapshot.getValue(Event.class);

                    if (eventId.equals(eventID)){
                        Log.d("eventmyee", event.title);
                        Intent intent = new Intent(getApplicationContext(), EventActivity.class);
                        intent.putExtra("Event", event);
                        intent.putExtra("EventId", id);
                        intent.putExtra("from", "uri");
                        startActivity(intent);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}