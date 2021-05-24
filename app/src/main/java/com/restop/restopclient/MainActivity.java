package com.restop.restopclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.restop.restopclient.R.drawable.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText email=(EditText) findViewById(R.id.Email);
        EditText password=(EditText) findViewById(R.id.Password);
        Button go=(Button) findViewById(R.id.go);
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.golden));
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em=email.getText().toString();
                String pass=password.getText().toString();
                User user=new User("hani",em);
                User.AddUser(user,pass);
                Intent intent=new Intent(MainActivity.this,Profile.class);
                intent.putExtra("a",em);
                startActivity(intent);


            }
        });
        Intent intent=new Intent(MainActivity.this,Menu.class);
        startActivity(intent);
    }
}