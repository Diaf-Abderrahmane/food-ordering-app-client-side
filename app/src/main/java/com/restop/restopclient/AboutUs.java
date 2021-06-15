package com.restop.restopclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    private TextView restoName;
    private TextView emailTxt;
    private TextView phoneTxt;
    private TextView fbUrl;
    private TextView instaUrl;
    private DatabaseReference reference;
    private ImageView back;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        logo = findViewById(R.id.logo);
        back = findViewById(R.id.back);
        restoName = findViewById(R.id.restoName);
        emailTxt = findViewById(R.id.emailtxt);
        phoneTxt = findViewById(R.id.phonetxt);
        fbUrl = findViewById(R.id.fburl);
        instaUrl = findViewById(R.id.instaurl);
        reference = FirebaseDatabase.getInstance().getReference().child("About_Us");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String resto_name = snapshot.child("RestaurantName").getValue(String.class);
                String email_txt = snapshot.child("Email").getValue(String.class);
                String phone_nmb = snapshot.child("Phone").getValue(String.class);
                String fb_url = snapshot.child("FacebookPage").getValue(String.class);
                String insta_url = snapshot.child("InstagramAccount").getValue(String.class);
                String logo_url = snapshot.child("LogoUrl").getValue(String.class);
                restoName.setText(resto_name);
                emailTxt.setText(email_txt);
                phoneTxt.setText(phone_nmb);
                fbUrl.setText(fb_url);
                instaUrl.setText(insta_url);
                Picasso.get().load(logo_url).into(logo);
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
    }
}