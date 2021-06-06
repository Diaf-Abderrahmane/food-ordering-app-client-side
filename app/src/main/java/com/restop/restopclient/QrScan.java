package com.restop.restopclient;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class QrScan extends Fragment {

    private ConstraintLayout ptsL,balL;
    private Button morePts,moreBal;
    private MaterialCardView pts,bal;
    private TextView txtPts,txtDzd;
    private FirebaseDatabase fb;
    private DatabaseReference Rusers,Rqr;
    private FirebaseAuth auth;
    private final String[] uid=new String[1];
    private MaterialCardView ptsDialogue,balDialogue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_qr_scan, container, false);
        fb=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        Rusers=fb.getReference().child("Users");
        uid[0]=auth.getCurrentUser().getUid();
        txtPts=view.findViewById(R.id.ptsBal);
        txtDzd=view.findViewById(R.id.dzdBal);
        ptsL=view.findViewById(R.id.layout_more_points);
        balL=view.findViewById(R.id.layout_more_balance);
        pts=view.findViewById(R.id.points_help);
        bal=view.findViewById(R.id.balance_help);
        moreBal=view.findViewById(R.id.more);
        morePts=view.findViewById(R.id.morePts);
        ptsDialogue=view.findViewById(R.id.points);
        balDialogue=view.findViewById(R.id.balance);
        ptsDialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                View view = getLayoutInflater().inflate(R.layout.pts_dialogue, null);
                alertDialog.setView(view);
                final AlertDialog alertDialog1 = alertDialog.create();
                alertDialog1.setCanceledOnTouchOutside(true);
                alertDialog1.show();
            }
        });
        balDialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                View view = getLayoutInflater().inflate(R.layout.bal_dialogue, null);
                alertDialog.setView(view);
                final AlertDialog alertDialog1 = alertDialog.create();
                alertDialog1.setCanceledOnTouchOutside(true);
                alertDialog1.show();
            }
        });
        FloatingActionButton scanBtn = view.findViewById(R.id.scanBtn);
        Rusers.child(uid[0]).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    int points =Integer.parseInt(task.getResult().child("Points").getValue().toString());
                    txtDzd.setText(String.valueOf(points));
                    txtPts.setText(String.valueOf(points/10));

                }
            }
        });

        morePts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ptsL.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(pts,new AutoTransition());
                    ptsL.setVisibility(View.VISIBLE);
                    morePts.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                } else {
                    TransitionManager.beginDelayedTransition(pts,new AutoTransition());
                    ptsL.setVisibility(View.GONE);
                    morePts.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);

                }
            }
        });
        moreBal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (balL.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(bal,new AutoTransition());
                    balL.setVisibility(View.VISIBLE);
                    moreBal.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                } else {
                    TransitionManager.beginDelayedTransition(bal,new AutoTransition());
                    balL.setVisibility(View.GONE);
                    moreBal.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);

                }
            }
        });
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),scan.class));
            }
        });
        return view;
    }
}