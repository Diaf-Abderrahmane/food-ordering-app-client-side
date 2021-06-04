package com.restop.restopclient;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        EditText email = findViewById(R.id.Email);
        EditText password = findViewById(R.id.Password);
        Button go = findViewById(R.id.go);
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.golden));
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");

        if (!(FirebaseAuth.getInstance().getCurrentUser() == null)) {
            startActivity(new Intent(MainActivity.this, Menu.class));
            finish();
        } else {
            startActivity(new Intent(MainActivity.this,Login.class));
            finish();
        }

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // FirebaseMessaging.getInstance().subscribeToTopic("Notification");
                final String em = email.getText().toString();
                final String pass = password.getText().toString();
                User user = new User("hani", em,0);
                User.AddUser(user, pass);
                Intent intent = new Intent(MainActivity.this, Profile.class);
                startActivity(intent);

            }
        });
    }
}