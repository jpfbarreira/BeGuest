package com.example.beguest.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beguest.R;
import com.example.beguest.ReadWriteUserDetails;
import com.example.beguest.databinding.FragmentDashboardBinding;
import com.example.beguest.databinding.FragmentNotificationsBinding;
import com.example.beguest.databinding.FragmentProfileBinding;
import com.example.beguest.ui.dashboard.DashboardViewModel;
import com.example.beguest.ui.notifications.NotificationsViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends Fragment {

    private FragmentProfileBinding binding;
    private String username;
    private String email;
    private  int points;
    private TextView textViewUsername;
    private TextView textViewEmail;
    private TextView textViewPoints;
    private FirebaseAuth auth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel ProfileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textViewUsername = root.findViewById(R.id.profile_username);
        textViewEmail = root.findViewById(R.id.profile_user_email);
        textViewPoints = root.findViewById(R.id.user_points);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if(currentUser == null){
            Toast.makeText(getContext(), "User invalid", Toast.LENGTH_SHORT).show();
        }else {
            getUserInfo(currentUser);
        }
        return root;
    }

    private void getUserInfo(FirebaseUser currentUser) {
        String userId = currentUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://beguest-4daae-default-rtdb.europe-west1.firebasedatabase.app").getReference("Registered Users");

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);

                if(readUserDetails != null){
                    email = currentUser.getEmail();
                    username = readUserDetails.username;
                    points = readUserDetails.points;

                    textViewEmail.setText(email);
                    textViewUsername.setText(username);
                    textViewPoints.setText(String.valueOf(points));
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