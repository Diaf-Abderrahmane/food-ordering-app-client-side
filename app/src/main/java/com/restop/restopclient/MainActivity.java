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
        int i =intent.getIntExtra("key",0);
        if (i ==2 ){
            replace(new QrScan());
            findViewById(R.id.ReviewsImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.eblack)));
            findViewById(R.id.SettingImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.eblack)));
            findViewById(R.id.QrCodeImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimaryDark)));
            findViewById(R.id.MenuImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.eblack)));

        } else if (i ==4 ){
            replace(new Profile());
            findViewById(R.id.ReviewsImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.eblack)));
            findViewById(R.id.SettingImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimaryDark)));
            findViewById(R.id.QrCodeImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.eblack)));
            findViewById(R.id.MenuImg).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.eblack)));

        } else {
            replace(new Menu());
            LinearLayout qr = findViewById(R.id.nQR_generator);
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
            LinearLayout profile = findViewById(R.id.nSettings);
            profile.setOnClickListener(new View.OnClickListener() {
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
            LinearLayout Menu = findViewById(R.id.nMenu);
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
            LinearLayout reveiws = findViewById(R.id.nReviews);
            reveiws.setOnClickListener(new View.OnClickListener() {
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
    }
    private void replace(Fragment Fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLO, Fragment).commit();

    }
}