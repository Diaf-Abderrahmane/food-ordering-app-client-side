package com.restop.restopclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;

public class AboutUs extends AppCompatActivity {

    private TextView emailTxt;
    private TextView phoneTxt;
    private TextView dayTxt;
    private TextView timeTxt;
    private DatabaseReference reference;
    private ImageView back;
    private ImageView logo;
    private ImageView facebook;
    private ImageView instagram;
    private String insta_url;
    private String fb_url;
    String fromDay,toDay,fromTime,toTime;
    String resto_name,email_txt,phone_nmb,logo_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        logo = findViewById(R.id.logo);
        back = findViewById(R.id.back);
        emailTxt = findViewById(R.id.emailtxt);
        phoneTxt = findViewById(R.id.phonetxt);
        facebook = findViewById(R.id.facebook);
        instagram = findViewById(R.id.instagram);
        dayTxt = findViewById(R.id.day);
        timeTxt = findViewById(R.id.time);
        reference = FirebaseDatabase.getInstance().getReference().child("About_Us");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot snap:snapshot.getChildren()) {
                    switch (snap.getKey()){
                        case "FromDay":
                            fromDay = snap.getValue().toString();
                            break;
                        case "ToDay":
                            toDay = snap.getValue().toString();
                            break;
                        case "FromTime":
                            fromTime = snap.getValue().toString();
                            break;
                        case "ToTime":
                            toTime = snap.getValue().toString();
                            break;
                        case "RestaurantName":
                            resto_name = snap.getValue().toString();
                            break;
                        case "FacebookPage":
                            fb_url = snap.getValue().toString();
                            break;
                        case "InstagramAccount":
                            insta_url = snap.getValue().toString();
                            break;
                        case "Phone":
                            phone_nmb = snap.getValue().toString();
                            break;
                        case "LogoUrl":
                            logo_url = snap.getValue().toString();
                            break;
                        case "Email":
                            email_txt = snap.getValue().toString();
                            break;
                    }
                    String day = fromDay + " - " + toDay ;
                    String time = fromTime + " - " +toTime;
                    dayTxt.setText(day);
                    timeTxt.setText(time);
                    emailTxt.setText(email_txt);
                    phoneTxt.setText(phone_nmb);
                    Picasso.get().load(logo_url).into(logo);
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutUs.this, MainActivity.class);
                intent.putExtra("key", 4);
                startActivity(intent);

            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl(fb_url);
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl(insta_url);
            }
        });
    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
}