package com.example.beguest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec;
import com.google.android.material.progressindicator.IndeterminateDrawable;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.regex.Pattern;

import kotlin.properties.ReadWriteProperty;

public class SignUp extends AppCompatActivity {

    TextView accountBtn;
    EditText editTextuserEmail, editTextuserName, editTextuserPass;
    ImageButton googlebtn;

    private GoogleSignInClient client;
    private GoogleSignInAccount googleAccount;
    private StorageReference storageReference;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    private ArrayList<String> userRegistedEvents = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //btns
        accountBtn = findViewById(R.id.have_account_btn);
        MaterialButton signUpBtn = findViewById(R.id.sign_up_btn);

        CircularProgressIndicatorSpec spec = new CircularProgressIndicatorSpec(this, null, 0, com.google.android.material.R.style.Widget_Material3_CircularProgressIndicator_ExtraSmall);
        IndeterminateDrawable<CircularProgressIndicatorSpec> progressIndicatorDrawable = IndeterminateDrawable.createCircularDrawable(this, spec);
        progressIndicatorDrawable.setColorFilter(getResources().getColor(R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);

        //editTexts
        editTextuserEmail = findViewById(R.id.userEmail);
        editTextuserName = findViewById(R.id.userUsername);
        editTextuserPass = findViewById(R.id.userPassword);

        googlebtn = findViewById(R.id.google_btn);
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this, options);

        ImageView showHidePwd = findViewById(R.id.show_hide_pwd);
        showHidePwd.setImageResource(R.drawable.eye_password_show);
        showHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextuserPass.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    //if pass visible the hide
                    editTextuserPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //change icon
                    showHidePwd.setImageResource(R.drawable.eye_password_show);
                }else {
                    editTextuserPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showHidePwd.setImageResource(R.drawable.eye_password_hide);
                }
            }
        });

        //sign up btn
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextuserEmail.getText().toString();
                String username = editTextuserName.getText().toString();
                String password = editTextuserPass.getText().toString();
                String instagram = "";
                String twitter = "";
                int points = 0;

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(SignUp.this, "Invalid email", Toast.LENGTH_LONG).show();
                    editTextuserEmail.setError("Email is required");
                    editTextuserEmail.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(SignUp.this, "Invalid email", Toast.LENGTH_LONG).show();
                    editTextuserEmail.setError("Valid email is required");
                    editTextuserEmail.requestFocus();
                } else if(TextUtils.isEmpty(username)){
                    Toast.makeText(SignUp.this, "Invalid username", Toast.LENGTH_SHORT).show();
                    editTextuserName.setError("Username is required");
                    editTextuserName.requestFocus();
                }else if(TextUtils.isEmpty(password)){
                    Toast.makeText(SignUp.this, "Invalid password", Toast.LENGTH_LONG).show();
                    editTextuserPass.setError("Password is required");
                    editTextuserPass.requestFocus();
                }else if(password.length() < 5){
                    Toast.makeText(SignUp.this, "Invalid password", Toast.LENGTH_LONG).show();
                    editTextuserPass.setError("Password has to have at least 6 digits");
                    editTextuserPass.requestFocus();
                } else {

                    signUpBtn.setText(null);
                    signUpBtn.setIcon(progressIndicatorDrawable);
                    registerUser(email, username, points, instagram, twitter,password, userRegistedEvents,signUpBtn, progressIndicatorDrawable);
                }
            }
        });

        //already have account button (Login)
        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(SignUp.this, Login.class);
                startActivity(login);
                finish();
            }
        });

        googlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = client.getSignInIntent();
                startActivityForResult(i,1234);
            }
        });

    }

    private void registerUser(String email, String username, int points, String instagram, String twitter, String password, ArrayList<String> registedEvents, MaterialButton signUpBtn, IndeterminateDrawable progressIndicatorDrawable) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUp.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //get current user id
                            FirebaseUser user = auth.getCurrentUser();

                            //Extrating User reference from database for registered users
                            DatabaseReference reference = FirebaseDatabase.getInstance("https://beguest-4daae-default-rtdb.europe-west1.firebasedatabase.app").getReference("Registered Users");

                            //save user information into Realtime database
                            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(username, points, instagram, twitter, email, registedEvents);

                                reference.child(user.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(SignUp.this, "User Registred successfully", Toast.LENGTH_SHORT).show();

                                        //start new activity
                                        Intent intent = new Intent(SignUp.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Toast.makeText(SignUp.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        Log.d("Error", task.getException().getMessage());
                                    }
                                }
                            });
                        }else{
                            signUpBtn.setText("Sign Up");
                            signUpBtn.setIcon(null);
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthUserCollisionException e){
                                editTextuserEmail.setError("Email already in use");
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d("ERROR", e.getMessage());
                            }
                        }
                    }
        });
    }


    //register user with google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1234){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                FirebaseAuth.getInstance().fetchSignInMethodsForEmail(account.getEmail()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                        if (isNewUser) {
                            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);

                            FirebaseAuth.getInstance().signInWithCredential(credential)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                String instagram = "";
                                                String twitter = "";
                                                int points = 0;

                                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                                //get current user id
                                                FirebaseUser user = auth.getCurrentUser();

                                                //Extrating User reference from database for registered users
                                                DatabaseReference reference = FirebaseDatabase.getInstance("https://beguest-4daae-default-rtdb.europe-west1.firebasedatabase.app").getReference("Registered Users");

                                                //save user information into Realtime database
                                                ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(account.getDisplayName(), points, instagram, twitter, account.getEmail(), userRegistedEvents);
                                                reference.child(user.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(SignUp.this, "User Registred successfully", Toast.LENGTH_SHORT).show();
                                                            uploadProfilePic(account);
                                                            //start new activity
                                                            Intent intent = new Intent(SignUp.this, MainActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }else {
                                                            Toast.makeText(SignUp.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                            Log.d("Error", task.getException().getMessage());
                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(SignUp.this, "User already registed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    //upload google photo to firebase
    private void uploadProfilePic(GoogleSignInAccount account) {
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("User Pics");

        if(account.getPhotoUrl() != null){
            //save image in database with id of user
            StorageReference fileReference = storageReference.child(auth.getCurrentUser().getUid() + "."
                    + getFileExtension(account.getPhotoUrl()));

            //upload to storage
            fileReference.putFile(account.getPhotoUrl()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}