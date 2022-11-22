package com.example.beguest.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.beguest.DataBinderMapperImpl;
import com.example.beguest.MainActivity;
import com.example.beguest.R;
import com.example.beguest.ReadWriteUserDetails;
import com.example.beguest.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private TextView textViewUsername;
    private String username;
    private FirebaseAuth auth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //android studio
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //
        textViewUsername = root.findViewById(R.id.usernameHomeScreen);

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
                    username = readUserDetails.username;

                    textViewUsername.setText(username);
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