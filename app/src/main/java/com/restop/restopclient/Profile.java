package com.restop.restopclient;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends Fragment {
    private CircleImageView profilepic;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private DatabaseReference reference;
    Switch aSwitch;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ConstraintLayout myAccount = view.findViewById(R.id.personal_data);
        ConstraintLayout aboutUs = view.findViewById(R.id.aboutUs);
        ConstraintLayout logOut= view.findViewById(R.id.Logout);
        TextView nameProfile = view.findViewById(R.id.nameProfile);
        profilepic = view.findViewById(R.id.userpic);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        aSwitch = view.findViewById(R.id.switch1);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

       /* reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                int i=0;
                for (DataSnapshot snap:snapshot.getChildren()){
                    switch (i){
                        case 3:
                            nameProfile.setText(snap.getValue().toString());
                            break;
                    }
                    i++;
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });*/

        String userName = user.getDisplayName();
        nameProfile.setText(userName);

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), AboutUs.class);
                startActivity(intent);

            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
        myAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), PersonalData.class);
                startActivity(intent);
            }
        });


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true){
                    User.SubscribeNotification(reference.child(user.getUid()));
                }
                else {
                    User.unSubscribeNotification(reference.child(user.getUid()));
                }
            }
        });
       //getUserInfo();
        return view;
    }
    private void getUserInfo() {
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    if (!snapshot.hasChild("image")) {
                        Picasso.get().load(R.drawable.ic_undraw_profile_pic_ic5t).into(profilepic);
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