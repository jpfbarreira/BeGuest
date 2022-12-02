package com.example.beguest.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> username;

    public HomeViewModel() {
        username = new MutableLiveData<>();
    }

    public LiveData<String> getSelectedItem() {
        return username;
    }

    public void setData(String item){
        username.setValue(item);
    }
}