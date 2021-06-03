package com.restop.restopclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {


    ImageView back;
    ImageView arrow;
    private CircleImageView profilepic;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private DatabaseReference reference;
    Query query;
    ImageView logout;
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout = findViewById(R.id.logout);
        back = findViewById(R.id.back);
        arrow = findViewById(R.id.arrow);
        profilepic = findViewById(R.id.userpic);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        aSwitch = findViewById(R.id.switchBox);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        query = reference.orderByChild("email").equalTo(user.getEmail());

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, PersonalData.class);
                startActivity(intent);
            }
        });



       /* aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true){
                    User.SubscribeNotification(reference.child(user.getUid()));
                }
                else {

                    User.unSubscribeNotification(reference.child(user.getUid()));
                }
            }
        });*/
        //Picasso.get().load(R.drawable.profile_pic).into(profilepic);

        getUserInfo();



    }


   private void getUserInfo() {
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    if (!snapshot.hasChild("image")) {
                       Picasso.get().load(R.drawable.profile_pic).into(profilepic);
                        /*reference.child("Default_Img").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                  Uri defaultUri = snapshot.getValue(Uri.class);
                                  profilepic.setImageURI(defaultUri);
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });*/
                   }else{
                        String image = snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(profilepic);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
}