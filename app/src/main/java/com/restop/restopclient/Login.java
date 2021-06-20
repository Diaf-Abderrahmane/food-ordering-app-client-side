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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    private TextView res,top,toContinue,login;
    private TextInputLayout email, password;
    private Button loginBtn,toRegister,forgotPassword;
    private FirebaseAuth fAuth;
    private boolean b=true;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        res = findViewById(R.id.res);
        top = findViewById(R.id.top);
        toContinue = findViewById(R.id.toContinue);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        fAuth = FirebaseAuth.getInstance();
        loginBtn = findViewById(R.id.loginBtn);
        toRegister = findViewById(R.id.toRegister);
        forgotPassword = findViewById(R.id.forgotPassword);

        AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


        awesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.Linvalid_email);
        awesomeValidation.addValidation(this, R.id.password, ".{6,}", R.string.invalid_password);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ForgotPassword.class));

            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    String vemail = email.getEditText().getText().toString();
                    String vpassword = password.getEditText().getText().toString();

                    if (b)
                        fAuth.signInWithEmailAndPassword(vemail, vpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    b=false;
                                    ref = FirebaseDatabase.getInstance().getReference().child("Users").child(fAuth.getCurrentUser().getUid());
                                    ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if ((task.isSuccessful()) && (task.getResult().getChildrenCount()>0)) {
                                                Intent intent=new Intent(Login.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(Login.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                } else {
                    Toast.makeText(Login.this, "Validation failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Sign_up.class));
                finish();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ForgotPassword.class));
            }
        });
    }
}