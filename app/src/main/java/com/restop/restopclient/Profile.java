package com.restop.restopclient;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_profile, container, false);

        logout = view.findViewById(R.id.logout);
        back = view.findViewById(R.id.back);
        arrow = view.findViewById(R.id.arrow);
        profilepic = view.findViewById(R.id.userpic);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        aSwitch = view.findViewById(R.id.switchBox);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        query = reference.orderByChild("email").equalTo(user.getEmail());

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonalData.class);
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

        return view;
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