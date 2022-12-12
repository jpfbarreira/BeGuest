package com.example.beguest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.beguest.CreateEventFragments.Create_Event_Fragment1;
import com.example.beguest.CreateEventFragments.Create_Event_Fragment2;
import com.example.beguest.CreateEventFragments.Create_Event_Fragment3;
import com.example.beguest.CreateEventFragments.Create_Event_ViewModel;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.kofigyan.stateprogressbar.components.StateItem;
import com.kofigyan.stateprogressbar.listeners.OnStateItemClickListener;

import java.util.ArrayList;
import java.util.List;


public class CreateNewEvent extends AppCompatActivity {

    static List<String> fragments = new ArrayList<String>();
    public Button nextbtn, back_btn;
    private ImageView backToActivityBtn;

    private Create_Event_ViewModel createEventViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);

        createEventViewModel = new ViewModelProvider(this).get(Create_Event_ViewModel.class);

        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.step_view);

        nextbtn = findViewById(R.id.next_btn);
        back_btn = findViewById(R.id.back_btn);
        backToActivityBtn = findViewById(R.id.back_toAtivity_btn);

        Fragment fragment1 = new Create_Event_Fragment1();
        Fragment fragment2 = new Create_Event_Fragment2();
        Fragment fragment3 = new Create_Event_Fragment3();
        FragmentReplacer(fragment1);

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragment1.isVisible()){
                    FragmentReplacer(fragment2);
                    back_btn.setVisibility(View.VISIBLE);
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                }else if(fragment2.isVisible()){
                    FragmentReplacer(fragment3);
                    nextbtn.setVisibility(View.INVISIBLE);
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragment2.isVisible()){
                    FragmentReplacer(fragment1);
                    back_btn.setVisibility(View.INVISIBLE);
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                }else if(fragment3.isVisible()){
                    FragmentReplacer(fragment2);
                    nextbtn.setVisibility(View.VISIBLE);
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                }
            }
        });

        backToActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        stateProgressBar.setOnStateItemClickListener(new OnStateItemClickListener() {
            @Override
            public void onStateItemClick(StateProgressBar stateProgressBar, StateItem stateItem, int stateNumber, boolean isCurrentState) {
                if(stateNumber == 1){
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                    FragmentReplacer(fragment1);
                } else if(stateNumber == 2){
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                    FragmentReplacer(fragment2);
                }else if(stateNumber == 3){
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                    FragmentReplacer(fragment3);
                }
            }
        });
    }

    private void FragmentReplacer(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentManager manager = getSupportFragmentManager();
        //fragment not in back stack, create it.
        FragmentTransaction ft = manager.beginTransaction();
        if (!fragments.contains(backStateName)) {
            // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            // ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            ft.replace(R.id.frame_layout, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
            fragments.add(backStateName);
            Log.d("backStateName", String.valueOf(fragments));
        } else {
            ft.replace(R.id.frame_layout, fragment, String.valueOf(fragment.getId()));
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}