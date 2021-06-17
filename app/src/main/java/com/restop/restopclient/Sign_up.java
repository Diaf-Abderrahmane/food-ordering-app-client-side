package com.restop.restopclient;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Sign_up extends AppCompatActivity {
    private TextView res,top,gettingStarted,toContinue;
    private TextInputLayout email, password, confirmPassword;
    private Button signUpBtn,toLogin;
    private FirebaseAuth fAuth;
    private FirebaseDatabase fireb;
    private DatabaseReference racine;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private TextWatcher SignUpTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String emailInput = email.getEditText().getText().toString().trim();
            String passwordInput = password.getEditText().getText().toString().trim();
            String confirmInput = confirmPassword.getEditText().getText().toString().trim();
            signUpBtn.setEnabled(!emailInput.isEmpty() && !passwordInput.isEmpty() && !confirmInput.isEmpty());
//            if (loginBtn.isEnabled())  {
//                loginBtn.setBackgroundColor(Color.BLACK);
//                loginBtn.setTextColor(Color.WHITE);
//            } else {
//                loginBtn.setBackgroundColor(Color.GRAY);
//                loginBtn.setTextColor(Color.BLACK);
//            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        res = findViewById(R.id.res);
        top = findViewById(R.id.top);
        gettingStarted = findViewById(R.id.gettingStarted);
        toContinue = findViewById(R.id.toContinue);
        toLogin = findViewById(R.id.toLogin);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        signUpBtn = findViewById(R.id.signUpBtn);
        fAuth = FirebaseAuth.getInstance();
        AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this,R.id.email,Patterns.EMAIL_ADDRESS,R.string.invalid_email);
        awesomeValidation.addValidation(this,R.id.password,".{6,}",R.string.invalid_password);
        awesomeValidation.addValidation(this,R.id.confirmPassword,R.id.password,R.string.invalid_confirm_password);
        email.getEditText().addTextChangedListener(SignUpTextWatcher);
        password.getEditText().addTextChangedListener(SignUpTextWatcher);
        confirmPassword.getEditText().addTextChangedListener(SignUpTextWatcher);

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(Sign_up.this,Menu.class));
            finish();
        }

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                Toast.makeText(Sign_up.this, "Admin Created", Toast.LENGTH_SHORT).show();

                                HashMap<String, Object> map = new HashMap<>();
                                map.put("Email", vemail);
                                map.put("NotificationActivation",1);
                                FirebaseDatabase.getInstance().getReference().child("Admins").child(userId).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Sign_up.this, "data added successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Sign_up.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Toast.makeText(Sign_up.this, userId , Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Sign_up.this,MainActivity.class));
                            } else {
                                Toast.makeText(Sign_up.this, "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(LOG.this,REG.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(Sign_up.this, "Validation failed", Toast.LENGTH_SHORT).show();
                }
            }});

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sign_up.this, Login.class));
            }
        });

    }

}