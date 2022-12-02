package com.example.beguest;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<String> username;
    private final MutableLiveData<String> email;
    private final MutableLiveData<Integer> points;
    private final MutableLiveData<Uri> userProfilePic;


    public SharedViewModel() {
        username = new MutableLiveData<>();
        email = new MutableLiveData<>();
        points = new MutableLiveData<>();
        userProfilePic = new MutableLiveData<>();
    }

    public LiveData<String> getUsername() {
        return username;
    }
    public LiveData<String> getUserEmail() {
        return email;
    }
    public LiveData<Integer> getUserPoints() {
        return points;
    }
    public LiveData<Uri> getUserProfilePic() {
        return userProfilePic;
    }

    public void setData(String name, String userEmail, String userPoints, Uri userPic){
        username.setValue(name);
        email.setValue(userEmail);
        points.setValue(Integer.valueOf(userPoints));
        userProfilePic.setValue(userPic);
    }

    public void changeUserProfilePic(Uri userPic){
        userProfilePic.setValue(userPic);
    }
}
