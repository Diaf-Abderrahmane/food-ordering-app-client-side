package com.restop.restopclient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.jetbrains.annotations.NotNull;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalData extends AppCompatActivity {

    private TextView emailTv;
    private TextView usernameTv;
    private TextView passwordTv;
    private ImageView back;
    private CircleImageView profilepic;
    private ImageView editIv;
    private Button saveBtn;
    private Button add;
    private ImageView edit1;
    private ImageView edit2;
    private ImageView edit3;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private StorageReference storageReference;


    private Uri uri;
    private String myUri="";
    private StorageTask storageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        edit1 = findViewById(R.id.edit1);
        edit2 = findViewById(R.id.edit2);
        edit3 = findViewById(R.id.edit3);
        emailTv = findViewById(R.id.email);
        usernameTv = findViewById(R.id.username);
        passwordTv = findViewById(R.id.password);
        add = findViewById(R.id.add);
        saveBtn = findViewById(R.id.saveBtn);
        editIv = findViewById(R.id.editIv);
        storageReference= FirebaseStorage.getInstance().getReference().child("Profile_Pics");
        back = findViewById(R.id.back);
        profilepic = findViewById(R.id.profilepic);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        Query query = reference.orderByChild("email").equalTo(user.getEmail());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String email = ds.child("email").getValue().toString();
                    String username = ds.child("userName").getValue().toString();




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalData.this, Profile.class));
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfileImage();
            }
        });


        editIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1,1).
                        start(PersonalData.this);
                //crop image with aspect ratio 1:1
            }
        });




        Picasso.get().load(R.drawable.profile_pic).into(profilepic);

        getUserInfo();



      //getUserInfo();
    }

    private void getUserInfo() {
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    if (snapshot.hasChild("image")) {
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

    private void uploadProfileImage() {


           final ProgressDialog progressDialog =new ProgressDialog(PersonalData.this);
           progressDialog.setTitle("Set your profile image");
           progressDialog.setMessage("Please wait, while we are setting your data");
           progressDialog.show();

           if (uri != null){ //from here
               final StorageReference storageReference1 = storageReference
                       .child(user.getUid()+".jpg");
               storageTask = storageReference1.putFile(uri);
               storageTask.continueWithTask(new Continuation() {
                   @Override
                   public Object then(@NonNull @NotNull Task task) throws Exception {

                      if (!task.isSuccessful()){
                          throw task.getException();
                      }

                       return storageReference1.getDownloadUrl();
                   }
               }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                   @Override
                   public void onComplete(@NonNull @NotNull Task<Uri> task) {
                       if (task.isSuccessful()){
                           Uri downloadUri = task.getResult();
                           myUri =downloadUri.toString();

                           HashMap<String,Object> hashMap=new HashMap<>();
                           hashMap.put("image",myUri); //a modifier
                           reference.child(user.getUid()).updateChildren(hashMap);
                           progressDialog.dismiss();
                       }
                   }
               });

               }else {

                 progressDialog.dismiss();
                 Toast.makeText(PersonalData.this,"Image not selected",Toast.LENGTH_SHORT).show();

               }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            uri=result.getUri();
            profilepic.setImageURI(uri);
        }else {
            Toast.makeText(PersonalData.this,"Error,  Try again",Toast.LENGTH_SHORT).show();
        }

    }

}