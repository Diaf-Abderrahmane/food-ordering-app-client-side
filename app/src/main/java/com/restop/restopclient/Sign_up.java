package com.restop.restopclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Sign_up extends AppCompatActivity {
    private TextView register;
    private TextInputLayout username, email, password, confirmPassword;
    private Button regBtn,toLogin;
    private FirebaseAuth fAuth;
    private FirebaseDatabase fireb;
    private DatabaseReference racine;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        register = findViewById(R.id.register);
        toLogin = findViewById(R.id.toLogin);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        regBtn = findViewById(R.id.regBtn);
        fAuth = FirebaseAuth.getInstance();
        AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this,R.id.username, RegexTemplate.NOT_EMPTY,R.string.invalid_username);
        awesomeValidation.addValidation(this,R.id.email,Patterns.EMAIL_ADDRESS,R.string.invalid_email);
        awesomeValidation.addValidation(this,R.id.password,".{6,}",R.string.invalid_password);
        awesomeValidation.addValidation(this,R.id.confirmPassword,R.id.password,R.string.invalid_confirm_password);

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(REG.this,MainActivity.class));
            finish();
        }

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vusername = username.getEditText().getText().toString();
                String vemail = email.getEditText().getText().toString();
                String vpassword = password.getEditText().getText().toString();
                String vcPassword = confirmPassword.getEditText().getText().toString();



                if (awesomeValidation.validate()){
                    fAuth.createUserWithEmailAndPassword(vemail, vpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                fAuth.signInWithEmailAndPassword(vemail,vpassword);
                                String userId = fAuth.getCurrentUser().getUid();
                                Toast.makeText(REG.this, "Use Created", Toast.LENGTH_SHORT).show();

                                HashMap<String, Object> map = new HashMap<>();
                                map.put("Email", vemail);
                                map.put("Username", vusername);
                                map.put("Points", "0");

                                FirebaseDatabase.getInstance().getReference().child("users").child(userId).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(REG.this, "data added successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(REG.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Toast.makeText(REG.this, userId , Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(REG.this,MainActivity.class));
                            } else {
                                Toast.makeText(REG.this, "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(LOG.this,REG.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(REG.this, "Validation failed", Toast.LENGTH_SHORT).show();
                }
            }});

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(REG.this, LOG.class));
            }
        });

    }

}