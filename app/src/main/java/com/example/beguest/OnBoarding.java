package com.example.beguest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.HttpCookie;

public class OnBoarding extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotLayout;
    Button nextbtn, skipbtn;

    TextView[] dots;
    ViewPageAdapter viewPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        nextbtn = findViewById(R.id.nextButton);
        skipbtn = findViewById(R.id.skipButton);

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getItem(0) < 2){
                    viewPager.setCurrentItem(getItem(1), true);
                }else {
                    Intent intent = new Intent(OnBoarding.this, SignUp.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        skipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OnBoarding.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.slideViewPager);
        dotLayout = (LinearLayout) findViewById(R.id.indicator_layout);

        viewPageAdapter = new ViewPageAdapter(this);
        viewPager.setAdapter(viewPageAdapter);

        setUpIndicator(0);
        viewPager.addOnPageChangeListener(viewListener);
    }

    public void setUpIndicator(int position){
        dots = new TextView[3];
        dotLayout.removeAllViews();

        for(int i = 0; i<dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.inactive));
            dotLayout.addView(dots[i]);
        }
        dots[position].setTextColor(getResources().getColor(R.color.white));
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setUpIndicator(position);

            if (position > 1){
                skipbtn.setVisibility(View.INVISIBLE);
                nextbtn.setText(R.string.let_get_started);

            }else {
                skipbtn.setVisibility(View.VISIBLE);
                nextbtn.setText(R.string.next_btn);

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getItem(int i ){
        return viewPager.getCurrentItem() + i;
    }
}