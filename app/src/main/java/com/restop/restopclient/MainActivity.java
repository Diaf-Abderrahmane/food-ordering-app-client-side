package com.restop.restopclient;


import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replace(new Menu());

        LinearLayout qr=findViewById(R.id.nQR_generator);
        qr.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                replace(new QrScan());
                findViewById(R.id.SettingImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.eblack)));
                findViewById(R.id.QrCodeImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimaryDark)));
                findViewById(R.id.MenuImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.eblack)));
                findViewById(R.id.ReviewsImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.eblack)));
            }
        });
        LinearLayout Setting=findViewById(R.id.nSettings);
        Setting.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                replace(new Profile());
                findViewById(R.id.ReviewsImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.eblack)));
                findViewById(R.id.SettingImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimaryDark)));
                findViewById(R.id.QrCodeImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.eblack)));
                findViewById(R.id.MenuImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.eblack)));

            }
        });
        LinearLayout Menu=findViewById(R.id.nMenu);
        Menu.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                replace(new Menu());
                findViewById(R.id.SettingImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.eblack)));
                findViewById(R.id.QrCodeImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.eblack)));
                findViewById(R.id.ReviewsImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.eblack)));
                findViewById(R.id.MenuImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimaryDark)));
            }
        });
        LinearLayout reviws=findViewById(R.id.nReviews);
        reviws.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                replace(new Reviews());
                findViewById(R.id.SettingImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.eblack)));
                findViewById(R.id.QrCodeImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.eblack)));
                findViewById(R.id.MenuImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.eblack)));
                findViewById(R.id.ReviewsImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimaryDark)));
            }
        });


    }
    private void replace(Fragment Fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLO, Fragment).commit();

    }
}