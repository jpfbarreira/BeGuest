package com.example.beguest.ui.home;

import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
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

import com.example.beguest.CircleTransform;
import com.example.beguest.DataBinderMapperImpl;
import com.example.beguest.MainActivity;
import com.example.beguest.R;
import com.example.beguest.ReadWriteUserDetails;
import com.example.beguest.SharedViewModel;
import com.example.beguest.databinding.FragmentHomeBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private TextView textViewUsername;

    private String username;
    private ImageView profilePic;
    private String email;
    private  int points;
    private Uri userProfilePic;
    private TextView default_profile_pic;

    private SharedViewModel sharedViewModel;
    private FirebaseAuth auth;
    private StorageReference storageReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //
        textViewUsername = root.findViewById(R.id.usernameHomeScreen);
        profilePic = root.findViewById(R.id.home_profile_pic);
        default_profile_pic = root.findViewById(R.id.default_profile_pic);

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