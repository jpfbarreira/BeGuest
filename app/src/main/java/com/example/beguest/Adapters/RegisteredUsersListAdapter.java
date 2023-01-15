package com.example.beguest.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beguest.R;
import com.example.beguest.ReadWriteUserDetails;
import com.example.beguest.UserPublicProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class RegisteredUsersListAdapter  extends RecyclerView.Adapter<RegisteredUsersListAdapter.RegisteredUsersListAdapterViewHolder>{
    Context context;
    ArrayList<String> usersIds;

    public RegisteredUsersListAdapter(Context context, ArrayList<String> usersIds) {
        this.context = context;
        this.usersIds = usersIds;
    }

    @NonNull
    @Override
    public RegisteredUsersListAdapter.RegisteredUsersListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.interested_users_list_layout, parent, false);

        return new RegisteredUsersListAdapter.RegisteredUsersListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegisteredUsersListAdapter.RegisteredUsersListAdapterViewHolder holder, int position) {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://beguest-4daae-default-rtdb.europe-west1.firebasedatabase.app").getReference("Registered Users");
        String userId = usersIds.get(position);

        StorageReference getImage = FirebaseStorage.getInstance().getReference("User Pics/" + userId + ".jpg");

        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);

                holder.username.setText(String.valueOf(readUserDetails.username));

                Log.d("key", String.valueOf(readUserDetails.username));

                holder.userCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, UserPublicProfile.class);
                        intent.putExtra("User", readUserDetails);
                        intent.putExtra("UserId", userId);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });

                getImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        holder.userPhoto.setVisibility(View.VISIBLE);
                        holder.userWithoutPhoto.setVisibility(View.INVISIBLE);
                        Picasso.get().load(uri).into(holder.userPhoto);
                    }
                });

                getImage.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        holder.userPhoto.setVisibility(View.INVISIBLE);
                        holder.userWithoutPhoto.setVisibility(View.VISIBLE);
                        holder.userWithoutPhoto.setText(String.valueOf(readUserDetails.username.charAt(0)).toUpperCase(Locale.ROOT));
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return usersIds.size();
    }

    public static class RegisteredUsersListAdapterViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout userCardView;
        TextView userWithoutPhoto;
        ImageView userPhoto;
        TextView username;

        public RegisteredUsersListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            userCardView = itemView.findViewById(R.id.user_layout);
            userWithoutPhoto = itemView.findViewById(R.id.user_without_photo);
            userPhoto = itemView.findViewById(R.id.user_photo);
            username = itemView.findViewById(R.id.user_name);
        }
    }
}
