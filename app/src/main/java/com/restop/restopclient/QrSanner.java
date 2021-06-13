package com.restop.restopclient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class QrSanner extends Fragment {
//    private Object capture;
//    public class Capture extends CaptureActivity {
//    }
    private Button scan;
    private TextView fbP,fbB;
    private DatabaseReference user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_arcanner, container, false);

        scan=view.findViewById(R.id.scan);
        fbB=view.findViewById(R.id.fbB);
        fbP=view.findViewById(R.id.fbP);
        user= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        user.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    fbB.setText(task.getResult().child("Points").getValue().toString());
                    fbP.setText(String.valueOf(task.getResult().child("Points").getValue(int.class)/10));

                }
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator=new IntentIntegrator(
                        getActivity()
                );
                integrator.forSupportFragment(QrSanner.this)
                        .setBeepEnabled(true)
                        .setPrompt("For flash use volume up key")
                        .setOrientationLocked(false)
                        .setCaptureActivity(Capture.class)
                        .initiateScan();

              /* integrator.setPrompt("For flash use volume up key");
                integrator.setBeepEnabled(true);
                integrator.setOrientationLocked(true);
                integrator.setCaptureActivity(Capture.class);
                IntentIntegrator.forSupportFragment(this).initiateScan();*/


            }
        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (resultCode != 0 && data != null) {

            FirebaseAuth auth;
            DatabaseReference userFb, qrFb;
            auth = FirebaseAuth.getInstance();
            final int[] price = new int[1];
            int[] status = new int[1];
            final int[] points = new int[1];
            userFb = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());
            qrFb = FirebaseDatabase.getInstance().getReference().child("QrCode").child(result.getContents());
            qrFb.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        price[0] = task.getResult().child("price").getValue(int.class);
                        status[0] = task.getResult().child("status").getValue(int.class);
                        userFb.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    points[0] = task.getResult().child("Points").getValue(int.class);
                                    switch (status[0]) {
                                        case 0: {
                                            userFb.child("Points").setValue(points[0] + price[0]);
                                            qrFb.child("status").setValue(2);
                                            status[0] = 2;
                                            fbB.setText(String.valueOf(points[0] + price[0]));
                                            fbP.setText(String.valueOf((points[0] + price[0]) / 10));
                                            break;
                                        }
                                        case 1: {
                                            if (points[0] >= price[0]) {
                                                userFb.child("Points").setValue(points[0] - price[0]);
                                                qrFb.child("status").setValue(3);
                                                status[0] = 3;
                                                fbB.setText(String.valueOf(points[0] - price[0]));
                                                fbP.setText(String.valueOf((points[0] - price[0]) / 10));
                                            } else {qrFb.child("status").setValue(4);
                                                Toast.makeText(getActivity(), "no enough points", Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            });
            Calendar calendar=Calendar.getInstance();
            SimpleDateFormat format=new SimpleDateFormat("dd_MM_yyyy");
            String date=format.format(calendar.getTime());
            ArrayList<OptionStats> options=new ArrayList<>();
            DatabaseReference stats;

            stats=FirebaseDatabase.getInstance().getReference().child("statistics");


            qrFb.child("options").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String name = null,price = null,img = null;
                    int quantity = 0;
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        switch (snapshot1.getKey()){
                            case "imgName":img=snapshot1.getValue().toString();break;
                            case "name":name=snapshot1.getValue().toString();break;
                            case "price":price=snapshot1.getValue().toString();break;
                            case "quantity":quantity=snapshot1.getValue(int.class);break;
                        }
                        options.add(new OptionStats(name,price,quantity,img));

//                        options.add(new OptionStats(name,price,quantity,img));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            for (int i=0;i<options.size();i++) {
                String name=options.get(i).getName();
                String img=options.get(i).getImg();
                String Lprice=options.get(i).getPrice();
                int Lquantity=options.get(i).getQuantity();
                stats.child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(name)){
                            for (DataSnapshot snapshot1:snapshot.child(name).getChildren()){
                                int quantity,somme;
                                String url;
                                quantity=snapshot1.child("quantity").getValue(int.class);
                                stats.child(date).child(name).child("quantity").setValue(quantity+Lquantity);
                                somme=snapshot1.child("price").getValue(int.class);
                                stats.child(date).child(name).child("price").setValue(somme+(Integer.parseInt(Lprice))*Lquantity);
                                url=snapshot1.child("img").getValue().toString();
                                if (url!=img){stats.child(date).child(name).child("img").setValue(img);}
                            }
                        }else {
                            stats.child(date).child(name).child("quantity").setValue(Lquantity);
                            stats.child(date).child(name).child("price").setValue((Integer.parseInt(Lprice))*Lquantity);
                            stats.child(date).child(name).child("img").setValue(img);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        }
    }
}