package com.restop.restopclient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalData extends AppCompatActivity {


    // private static final String TAG = "";
    private ImageView back;
    private CircleImageView profilepic;
    private ImageView editIv;
    private Button saveBtn;
    private Button update;
    private Button update1;
    private Button update2;
    private TextView emailTv;
    private TextView usernameTv;
    private EditText editName;
    private EditText editEmail;
    private ImageView edit1;
    private ImageView edit2;
    private ImageView edit3;
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private DatabaseReference reference;
    private StorageReference storageReference;
    private CircleImageView editPic;

    private Uri uri;
    private String myUri = "";
    private StorageTask storageTask;
    private String email = "";
    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        editPic = findViewById(R.id.editPic);
        usernameTv = findViewById(R.id.username);
        emailTv = findViewById(R.id.email);
        edit1 = findViewById(R.id.edit1);
        edit2 = findViewById(R.id.edit2);
        edit3 = findViewById(R.id.edit3);
        saveBtn = findViewById(R.id.saveBtn);
        //editIv = findViewById(R.id.editIv);
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile_Pics");
        back = findViewById(R.id.back);
        profilepic = findViewById(R.id.profilepic);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot snap : snapshot.getChildren()) {
                    switch (i) {
                        case 0:
                            emailTv.setText(snap.getValue().toString());
                            break;
                        case 3:
                            usernameTv.setText(snap.getValue().toString());
                            break;
                    }
                    i++;
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalData.this, MainActivity.class);
                intent.putExtra("key", 4);
                startActivity(intent);


            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfileImage();
            }
        });


        editPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1, 1).
                        start(PersonalData.this);
                //crop image with aspect ratio 1:1
            }
        });

        edit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PersonalData.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_edit_name, null);
                editName = view.findViewById(R.id.editname);
                update = view.findViewById(R.id.update);
                alertDialog.setView(view);
                final AlertDialog alertDialog1 = alertDialog.create();
                alertDialog1.setCanceledOnTouchOutside(true);
                alertDialog1.show();
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (changeUserName()) {
                            Toast.makeText(PersonalData.this, "Data has been changed", Toast.LENGTH_SHORT).show();
                        } else if (editName.getText().toString().equals("")) {
                            alertDialog1.dismiss();
                        }
                    }


                });
            }
        });

        edit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PersonalData.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_edit_email, null);
                editEmail = view.findViewById(R.id.editemail);
                update1 = view.findViewById(R.id.update1);
                alertDialog.setView(view);
                final AlertDialog alertDialog1 = alertDialog.create();
                alertDialog1.setCanceledOnTouchOutside(true);
                alertDialog1.show();
                update1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (changeUserEmail()) {
                            Toast.makeText(PersonalData.this, "Data has been changed", Toast.LENGTH_SHORT).show();
                        } else if (editEmail.getText().toString().equals("")) {
                            alertDialog1.dismiss();
                        }
                    }
                });
            }

        });

        edit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PersonalData.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_edit_password, null);
                EditText editPassword = view.findViewById(R.id.editpassword);
                EditText confirmPassword = view.findViewById(R.id.confirm_password);
                update2 = view.findViewById(R.id.update2);
                alertDialog.setView(view);
                final AlertDialog alertDialog1 = alertDialog.create();
                alertDialog1.setCanceledOnTouchOutside(true);
                alertDialog1.show();
                update2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         String edit_password = editPassword.getText().toString();
                         String confirm_password = confirmPassword.getText().toString();
                        if (changeUserPassword(edit_password, confirm_password)) {
                            user.updatePassword(edit_password).addOnCompleteListener(PersonalData.this,new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(PersonalData.this, "Data has been changed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else if (edit_password.equals("") || confirm_password.equals("")) {
                            alertDialog1.dismiss();
                        }
                    }
                });

            }
        });

        // Picasso.get().load(R.drawable.ic_undraw_profile_pic_ic5t).into(profilepic);

        getUserInfo();


    }


    private boolean changeUserPassword(String edit_password, String confirm_password) {
        Boolean isValidNewPassword, isValidConfirmPassword;
        ArrayList<String> errors = new ArrayList<>();
        isValidNewPassword = edit_password.length() >= 6;
        isValidConfirmPassword = (confirm_password.equals(edit_password));


        if (!isValidNewPassword) {
            errors.add("The Password is very Short");
        }
        if (!isValidConfirmPassword) {
            errors.add("Passwords is not equal");
        }
        if (!(isValidNewPassword && isValidConfirmPassword)) {
            show(PersonalData.this, errors);
        }

        return isValidNewPassword && isValidConfirmPassword;
    }

    private void show(Activity personalData, ArrayList<String> errors) {
        AlertDialog.Builder adb = new AlertDialog.Builder(personalData);
        LinearLayout L = new LinearLayout(personalData);
        L.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < errors.size(); i++) {
            TextView Tv = new TextView(personalData);
            Tv.setText(errors.get(i));
            L.addView(Tv);
        }
        adb.setView(L);
        Dialog d = adb.create();
        d.show();
    }

    private boolean changeUserEmail() {
        String email = emailTv.getText().toString();
        if (!email.equals(editEmail.getText().toString()) && !editEmail.getText().toString().equals("")) {
            reference.child(user.getUid()).child("Email").setValue(editEmail.getText().toString());
            user.sendEmailVerification();
            reference.child(user.getUid()).child("Email").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    emailTv.setText(snapshot.getValue(String.class));
                    user.updateEmail(snapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
            return true;
        }
        return false;
    }

    private boolean changeUserName() {
        String userName = usernameTv.getText().toString();
        if (!userName.equals(editName.getText().toString()) && !editName.getText().toString().equals("")) {
            reference.child(user.getUid()).child("Username").setValue(editName.getText().toString());
            reference.child(user.getUid()).child("Username").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    usernameTv.setText(snapshot.getValue().toString());
                    UserProfileChangeRequest nameUpdate1 = new UserProfileChangeRequest.Builder().setDisplayName(usernameTv.getText().toString()).build();
                    user.updateProfile(nameUpdate1);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
            return true;
        }
        return false;
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

        final ProgressDialog progressDialog = new ProgressDialog(PersonalData.this);
        progressDialog.setTitle("Set your profile image");
        progressDialog.setMessage("Please wait, while we are setting your data");
        progressDialog.show();

        if (uri != null) { //from here
            final StorageReference storageReference1 = storageReference
                    .child(user.getUid() + ".jpg");
            storageTask = storageReference1.putFile(uri);
            storageTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull @NotNull Task task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return storageReference1.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        myUri = downloadUri.toString();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("image", myUri); //a modifier
                        reference.child(user.getUid()).updateChildren(hashMap);
                        progressDialog.dismiss();
                    }
                }
            });

        } else {

            progressDialog.dismiss();
            Toast.makeText(PersonalData.this, "Image not selected", Toast.LENGTH_SHORT).show();

        }
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            uri = result.getUri();
            profilepic.setImageURI(uri);
        } else {
            Toast.makeText(PersonalData.this, "Error,  Try again", Toast.LENGTH_SHORT).show();
        }

    }
}


