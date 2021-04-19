package com.example.fga;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class FullscreenActivity extends AppCompatActivity {
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        viewPager = findViewById(R.id.fuckingPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this) {
            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return false;
            }
        };

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(Integer.MAX_VALUE/2);
    }
}