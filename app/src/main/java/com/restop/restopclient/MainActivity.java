package com.restop.restopclient;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.zip.Inflater;


public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        int i = intent.getIntExtra("key", 0);
        if (i == 2) {
            replace(new QrSanner());
            findViewById(R.id.ReviewsImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
            findViewById(R.id.SettingImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
            findViewById(R.id.QrCodeImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
            findViewById(R.id.MenuImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
            Main();


        } else if (i == 3) {
            replace(new Profile());
            findViewById(R.id.ReviewsImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
            findViewById(R.id.SettingImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
            findViewById(R.id.QrCodeImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
            findViewById(R.id.MenuImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
            Main();


        } else if (i == 4) {
            replace(new Profile());
            findViewById(R.id.ReviewsImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
            findViewById(R.id.SettingImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
            findViewById(R.id.QrCodeImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
            findViewById(R.id.MenuImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
            Main();


        } else {
            replace(new Menu());
            Main();

        }
    }

    private void replace(Fragment Fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLO, Fragment).commit();

    }

    public void Main() {
        LinearLayout qr = findViewById(R.id.nQR_generator);
        qr.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                replace(new QrSanner());
                findViewById(R.id.SettingImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
                findViewById(R.id.QrCodeImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
                findViewById(R.id.MenuImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
                findViewById(R.id.ReviewsImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));

            }
        });
        LinearLayout profile = findViewById(R.id.nSettings);
        profile.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                replace(new Profile());
                findViewById(R.id.ReviewsImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
                findViewById(R.id.SettingImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
                findViewById(R.id.QrCodeImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
                findViewById(R.id.MenuImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));

            }
        });
        LinearLayout Menu = findViewById(R.id.nMenu);
        Menu.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                replace(new Menu());
                findViewById(R.id.SettingImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
                findViewById(R.id.QrCodeImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
                findViewById(R.id.ReviewsImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
                findViewById(R.id.MenuImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
            }
        });
        LinearLayout reveiws = findViewById(R.id.nReviews);
        reveiws.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                replace(new Reviews());
                findViewById(R.id.SettingImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
                findViewById(R.id.QrCodeImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
                findViewById(R.id.MenuImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
                findViewById(R.id.ReviewsImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
            }
        });

    }
}