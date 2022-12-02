package com.example.beguest.ui.profile;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;

import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beguest.CircleTransform;
import com.example.beguest.R;
import com.example.beguest.ReadWriteUserDetails;
import com.example.beguest.SharedViewModel;
import com.example.beguest.UserSettings;
import com.example.beguest.databinding.FragmentProfileBinding;
import com.example.beguest.ui.home.HomeViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Proxy;
import java.util.Objects;

public class Profile extends Fragment {

    private FragmentProfileBinding binding;

    private String username;
    private String email;
    private  int points;

    private ImageView settingsBtn;

    private TextView textViewUsername;
    private TextView textViewEmail;
    private TextView textViewPoints;
    public ImageView profilePic;
    private TextView default_profile_pic;

    private FirebaseAuth auth;
    private StorageReference storageReference;
    private FirebaseUser currentUser;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uriImage;
    private SharedViewModel sharedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel ProfileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textViewUsername = root.findViewById(R.id.profile_username);
        textViewEmail = root.findViewById(R.id.profile_user_email);
        textViewPoints = root.findViewById(R.id.user_points);

        settingsBtn = root.findViewById(R.id.setting_btn);

        //profile pic
        profilePic = root.findViewById(R.id.profile_pic);
        default_profile_pic = root.findViewById(R.id.default_profile_pic);


        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("User Pics");

        Uri uri = currentUser.getPhotoUrl();

        if (currentUser != null){
            sharedViewModel.getUsername().observe(getViewLifecycleOwner(), item -> {
                username = item;
                textViewUsername.setText(item);
            });
            sharedViewModel.getUserEmail().observe(getViewLifecycleOwner(), item -> {
                textViewEmail.setText(item);;
            });
            sharedViewModel.getUserPoints().observe(getViewLifecycleOwner(), item -> {
                textViewPoints.setText(String.valueOf(item));
            });
            sharedViewModel.getUserProfilePic().observe(getViewLifecycleOwner(), item -> {
                if(item != null){
                    profilePic.setBackground(null);
                    profilePic.setColorFilter(null);
                    default_profile_pic.setText(null);
                    Picasso.get().load(item).transform(new CircleTransform()).into(profilePic);
                }else{
                    profilePic.setBackgroundResource(R.drawable.rounded_borders_shape);
                    default_profile_pic.setText(String.valueOf(username.charAt(0)));
                }
            });

        }else {
            Toast.makeText(getContext(), "User invalid", Toast.LENGTH_SHORT).show();
        }

        //click listerners
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonShowPopupWindowClick(root);
            }
        });


        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this.getContext(), UserSettings.class);
                startActivity(intent);
            }
        });

        return root;
    }


    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.profile_pic_upload_popup, null);

        // create the popup window
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, 1100, 500, focusable);
        Button cancelBtn = popupView.findViewById(R.id.cancel_btn);
        Button uploadBtn = popupView.findViewById(R.id.upload_photo_btn);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setElevation(20);

        View container = (View) popupWindow.getContentView().getParent();
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();

        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.5f;
        wm.updateViewLayout(container, p);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser(popupWindow);
            }
        });

    }

    private void openFileChooser(PopupWindow popupWindow) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
        popupWindow.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null){
            uriImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uriImage);
                Bitmap newProfilePic = getCircular(bitmap);
                profilePic.setImageBitmap(newProfilePic);
                sharedViewModel.changeUserProfilePic(uriImage);
                UploadProfilePic();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("ERROR", "Erro na img");
            }

        }
    }

        private void UploadProfilePic() {
        if(uriImage != null){
            //save image in database with id of user
            StorageReference fileReference = storageReference.child(auth.getCurrentUser().getUid() + "."
                    + getFileExtension(uriImage));

            //upload to storage
            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;
                            currentUser = auth.getCurrentUser();

                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(downloadUri).build();
                            currentUser.updateProfile(profileChangeRequest);
                        }
                    });
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    public Bitmap getCircular(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        squaredBitmap.recycle();
        return bitmap;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}