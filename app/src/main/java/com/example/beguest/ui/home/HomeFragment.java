package com.example.beguest.ui.home;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beguest.Adapters.HomeEventsAdapter;
import com.example.beguest.CircleTransform;
import com.example.beguest.CreateEventFragments.Event;
import com.example.beguest.R;
import com.example.beguest.ReadWriteUserDetails;
import com.example.beguest.SharedViewModel;
import com.example.beguest.databinding.FragmentHomeBinding;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private TextView textViewUsername;

    private String username;
    private ImageView profilePic;
    private String email;
    private  int points;
    private Uri userProfilePic;
    private TextView default_profile_pic;

    private TextView recommendedEventTitle, recommendedEventDescription, recommendedEventLocation, recommendedEventDate;

    private SharedViewModel sharedViewModel;
    private FirebaseAuth auth;
    private StorageReference storageReference;

    private RecyclerView recyclerView;
    public static HomeEventsAdapter eventAdpter;
    private DatabaseReference reference;
    private ArrayList<Event> events;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //
        textViewUsername = root.findViewById(R.id.usernameHomeScreen);
        profilePic = root.findViewById(R.id.home_profile_pic);
        default_profile_pic = root.findViewById(R.id.default_profile_pic);
        recyclerView = root.findViewById(R.id.events_recycle_view);
        recommendedEventTitle = root.findViewById(R.id.recommended_card_event_title);
        recommendedEventDescription = root.findViewById(R.id.recommended_card_event_description);
        recommendedEventLocation = root.findViewById(R.id.recommended_card_event_location);
        recommendedEventDate = root.findViewById(R.id.recommended_card_event_date);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference("User Pics");

        userProfilePic = currentUser.getPhotoUrl();

        sharedViewModel.getUserProfilePic().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                if (uri != null){
                    Picasso.get().load(uri).transform(new CircleTransform()).into(profilePic);
                    default_profile_pic.setText(null);
                }else {
                    profilePic.setBackgroundResource(R.drawable.rounded_borders_shape);
                    default_profile_pic.setText(String.valueOf(username.charAt(0)).toUpperCase(Locale.ROOT));
                }
            }
        });

        if(currentUser == null){
            Toast.makeText(getContext(), "User invalid", Toast.LENGTH_SHORT).show();
        }else {
            getUserInfo(currentUser);
        }

        //Events horizontal and recomended
        reference = FirebaseDatabase.getInstance("https://beguest-4daae-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Registered Events");


        events = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        eventAdpter = new HomeEventsAdapter(this.getContext(), events);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                events.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String eventID = dataSnapshot.getKey();
                    Event event = dataSnapshot.getValue(Event.class);
                    Log.d("Maria", String.valueOf(events));
                    event.setEventID(eventID);

                    if (event.creatorId.equals(currentUser.getUid())){
                        if(!events.contains(event)){
                            events.add(event);
                            Log.d("Maria Events", String.valueOf(events));
                            recyclerView.setAdapter(eventAdpter);
                        }
                    }

                    DatabaseReference eventReference = reference.child(eventID);
                    DatabaseReference registeredUsersRef = eventReference.child("Registered Users");

                    registeredUsersRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //events.clear();
                            for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                String userId = String.valueOf(dataSnapshot.getValue(String.class));

                                //add to home events that the user is registered in
                                if (userId.equals(currentUser.getUid())){
                                    if(!events.contains(event)){
                                        events.add(event);
                                        recyclerView.setAdapter(eventAdpter);
                                    }
                                }
                            }
                            eventAdpter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    //recommmended event
                    //Event with most people registred in the area gets the recommended position, for now it's the event with this title
                    if(Objects.equals(event.title, "Trico's Party")){
                        String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                        recommendedEventTitle.setText(event.title);
                        recommendedEventDescription.setText(event.description);
                        recommendedEventLocation.setText(event.location);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
                        try {
                            Date eventDate = simpleDateFormat.parse(event.date);
                            String eventDay = String.valueOf(eventDate.getDate());
                            int eventMonth = eventDate.getMonth();
                            String eventTime = event.time;

                            String hour = eventTime.split(":")[0];

                            if(Integer.parseInt(hour) > 12){
                                recommendedEventDate.setText(eventDay + " " +  months[eventMonth]
                                        + ", " + eventTime + " " + "PM");
                            }else {
                                recommendedEventDate.setText(eventDay + " " +  months[eventMonth]
                                        + ", " + eventTime + " " + "AM");
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                eventAdpter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }

    public void getUserInfo(FirebaseUser currentUser) {
        String userId = currentUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://beguest-4daae-default-rtdb.europe-west1.firebasedatabase.app").getReference("Registered Users");
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);

                if(readUserDetails != null){
                    username = readUserDetails.username;
                    email = currentUser.getEmail();
                    points = readUserDetails.points;

                    textViewUsername.setText(username);
                    sharedViewModel.setData(username, email, String.valueOf(points), userProfilePic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}