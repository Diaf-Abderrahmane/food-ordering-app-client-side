package com.restop.restopclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    private DatabaseReference reference;
    Switch aSwitch;
    Intent intent;
    TextView username;
    TextView email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.profile_fragment, container, false);

        ConstraintLayout myAccount = view.findViewById(R.id.personal_data);
        ConstraintLayout aboutUs = view.findViewById(R.id.aboutUs);
        ConstraintLayout logOut= view.findViewById(R.id.Logout);
        TextView nameProfile = view.findViewById(R.id.nameProfile);
        profilepic = view.findViewById(R.id.userpic);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        aSwitch = view.findViewById(R.id.switch1);
        username = view.findViewById(R.id.nameProfile);
        email = view.findViewById(R.id.textEmail);

        reference.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    email.setText(task.getResult().child("Email").getValue(String.class));
                    username.setText(task.getResult().child("Username").getValue(String.class));
                }

            }
        });
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("Email") && snapshot.hasChild("Username")){
                    email.setText(snapshot.child("Email").getValue(String.class));
                    username.setText(snapshot.child("Username").getValue(String.class));
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


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


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("save", Context.MODE_PRIVATE);
        aSwitch.setChecked(sharedPreferences.getBoolean("value",true));

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true){
                    SharedPreferences.Editor editor = getActivity()
                            .getSharedPreferences("save",Context.MODE_PRIVATE).edit();
                    editor.putBoolean("value",true);
                    editor.apply();
                    aSwitch.setChecked(true);
                    User.SubscribeNotification(reference.child(user.getUid()));
                }
                else {
                    SharedPreferences.Editor editor = getActivity()
                            .getSharedPreferences("save",Context.MODE_PRIVATE).edit();
                    editor.putBoolean("value",false);
                    editor.apply();
                    aSwitch.setChecked(false);
                    User.unSubscribeNotification(reference.child(user.getUid()));
                }
            }
        });
       getUserInfo();
        return view;
    }


    private void getUserInfo() {
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    if (!snapshot.hasChild("image")) {
                        Picasso.get().load(R.drawable.final_profile_picture).into(profilepic);
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