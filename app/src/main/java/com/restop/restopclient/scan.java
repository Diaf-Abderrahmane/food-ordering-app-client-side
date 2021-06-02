package com.restop.restopclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class scan extends AppCompatActivity {
    private CodeScanner codeScanner;
    private CodeScannerView scannerView;
    private TextView textdata;
    private FirebaseDatabase fb;
    private DatabaseReference Rusers,Rqr;
    private FirebaseAuth auth;
    private final int[] price=new int[1];
    private final int[] status=new int[1];
    private final String[] uid=new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        scannerView = findViewById(R.id.scannerV);
        codeScanner = new CodeScanner(this, scannerView);
//        fb=FirebaseDatabase.getInstance();
//        auth=FirebaseAuth.getInstance();
//        Rusers=fb.getReference().child("users");
//        Rqr=fb.getReference().child("QrCode");
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fb=FirebaseDatabase.getInstance();
                        auth=FirebaseAuth.getInstance();
                        Rusers=fb.getReference().child("users");
                        Rqr=fb.getReference().child("QrCode");
                        uid[0]=auth.getCurrentUser().getUid();
                        Toast.makeText(scan.this, result.getText(), Toast.LENGTH_SHORT).show();
                        Rqr.child(result.getText()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()){
                                    price[0]=task.getResult().child("price").getValue(int.class);
                                    status[0]=task.getResult().child("status").getValue(int.class);
                                    Rusers.child(uid[0]).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if (task.isSuccessful()){
                                                int points;
                                                points=task.getResult().child("points").getValue(int.class);
                                                switch (status[0]){
                                                    case 0:{
                                                        Rusers.child(uid[0]).child("points").setValue(points+price[0]);
                                                        Rqr.child(result.getText()).child("status").setValue(2);
                                                        break;
                                                    }
                                                    case 1:{
                                                        if (points>=price[0]){
                                                            Rusers.child(uid[0]).child("points").setValue(points-price[0]);
                                                            Rqr.child(result.getText()).child("status").setValue(3);
                                                        }else{Rqr.child(result.getText()).child("status").setValue(4);}
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        });


                    }
                });


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        request();

    }

    private void request() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(scan.this, "camera permission required", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();

            }
        }).check();
    }

}