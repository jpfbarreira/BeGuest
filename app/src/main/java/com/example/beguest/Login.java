package com.example.beguest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec;
import com.google.android.material.progressindicator.IndeterminateDrawable;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {
    private EditText editTextLoginEmail, editTextLoginPwd;
    private FirebaseAuth auth;

    TextView noAccountBtn;
    MaterialButton loginbtn;
    ImageButton googlebtn;
    private GoogleSignInClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginbtn = findViewById(R.id.login_btn);
        noAccountBtn = findViewById(R.id.no_account_btn);

        editTextLoginEmail = findViewById(R.id.userEmail);
        editTextLoginPwd= findViewById(R.id.userPassword);

        //progress bar
        CircularProgressIndicatorSpec spec = new CircularProgressIndicatorSpec(this, null, 0, com.google.android.material.R.style.Widget_Material3_CircularProgressIndicator_ExtraSmall);
        IndeterminateDrawable<CircularProgressIndicatorSpec> progressIndicatorDrawable = IndeterminateDrawable.createCircularDrawable(this, spec);
        progressIndicatorDrawable.setColorFilter(getResources().getColor(R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);

        googlebtn = findViewById(R.id.google_btn);
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this, options);

        auth = FirebaseAuth.getInstance();

        noAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(Login.this, SignUp.class);
                startActivity(login);
                finish();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = editTextLoginEmail.getText().toString();
                String textPwd = editTextLoginPwd.getText().toString();

                if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(Login.this, "Invalid email", Toast.LENGTH_LONG).show();
                    editTextLoginEmail.setError("Email is required");
                    editTextLoginEmail.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(Login.this, "Invalid email", Toast.LENGTH_LONG).show();
                    editTextLoginEmail.setError("Valid email is required");
                    editTextLoginEmail.requestFocus();
                }else if(TextUtils.isEmpty(textPwd)){
                    Toast.makeText(Login.this, "Invalid password", Toast.LENGTH_LONG).show();
                    editTextLoginPwd.setError("Password is required");
                    editTextLoginPwd.requestFocus();
                } else {
                    loginbtn.setText(null);
                    loginbtn.setIcon(progressIndicatorDrawable);
                    loginUser(textEmail, textPwd);
                }
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

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    Intent home = new Intent(Login.this, MainActivity.class);
                    startActivity(home);
                    finish();

                }else{
                    Toast.makeText(Login.this, "Login Unsuccessful, try again", Toast.LENGTH_SHORT).show();
                }
                loginbtn.setText("Login");
                loginbtn.setIcon(null);
            }
        });
    }

    //login user with google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1234){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }




}