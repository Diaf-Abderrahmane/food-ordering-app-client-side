package com.restop.restopclient;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QrSanner extends Fragment {

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
                integrator.setPrompt("For flash use volume up key");
                integrator.setBeepEnabled(true);
                integrator.setCaptureActivity(Capture.class);
                integrator.initiateScan();



            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result=IntentIntegrator.parseActivityResult(
                requestCode,resultCode,data
        );
        if (result.getContents()!=null){
            FirebaseAuth auth;
            DatabaseReference userFb,qrFb;
            auth=FirebaseAuth.getInstance();
            final int[] price=new int[1];
            final int[] status=new int[1];
            final int[] points=new int[1];
            userFb= FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());
            qrFb=FirebaseDatabase.getInstance().getReference().child("QrCode").child(result.getContents());
            qrFb.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()){
                        price[0]=task.getResult().child("price").getValue(int.class);
                        status[0]=task.getResult().child("status").getValue(int.class);
                        userFb.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()){
                                    points[0]=task.getResult().child("Points").getValue(int.class);
                                    switch(status[0]){
                                        case 0:{
                                            userFb.child("Points").setValue(points[0]+price[0]);
                                            qrFb.child("status").setValue(1);
                                            break;
                                        }
                                        case 1:{if (points[0]>=price[0]){
                                            userFb.child("Points").setValue(points[0]-price[0]);
                                            qrFb.child("status").setValue(2);}else{
                                            qrFb.child("status").setValue(3);
                                            Toast.makeText(getActivity(), "no enough points", Toast.LENGTH_SHORT).show();}

                                            break;
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            });
//            AlertDialog.Builder builder=new AlertDialog.Builder(
//                    MainActivity.this
//            );
//            builder.setTitle("Result");
//            builder.setMessage(result.getContents());
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.dismiss();
//                }
//            });
//            builder.show();

        }else {
            Toast.makeText(getActivity().getApplicationContext(), "no scan", Toast.LENGTH_SHORT).show();

        }
    }
}