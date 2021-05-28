package com.restop.restopclient;


import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;




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

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em = email.getText().toString();
                String pass = password.getText().toString();
                User user = new User("hani", em,0,1);
                User.AddUser(user, pass);
                Intent intent = new Intent(MainActivity.this, Profile.class);
                intent.putExtra("a", em);
                startActivity(intent);

            }
        });
        Intent intent=new Intent(MainActivity.this,Menu.class);
        startActivity(intent);
    }
}