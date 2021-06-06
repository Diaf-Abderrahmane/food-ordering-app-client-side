package com.restop.restopclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Qr_Scan extends AppCompatActivity {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr__scan);

        LinearLayout Menu=findViewById(R.id.nMenu);
        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Qr_Scan.this,Menu.class);
                startActivity(intent);
                finish();
            }
        });
        fb=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        Rusers=fb.getReference().child("Users");
        uid[0]=auth.getCurrentUser().getUid();
        txtPts=findViewById(R.id.ptsBal);
        txtDzd=findViewById(R.id.dzdBal);
        ptsL=findViewById(R.id.layout_more_points);
        balL=findViewById(R.id.layout_more_balance);
        pts=findViewById(R.id.points_help);
        bal=findViewById(R.id.balance_help);
        moreBal=findViewById(R.id.more);
        morePts=findViewById(R.id.morePts);
        FloatingActionButton scanBtn = findViewById(R.id.scanBtn);
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
                startActivity(new Intent(Qr_Scan.this,scan.class));
            }
        });

    }

}