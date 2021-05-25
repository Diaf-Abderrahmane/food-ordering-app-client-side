package com.restop.restopclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Qr_Scan extends AppCompatActivity {
    private ConstraintLayout ptsL,balL;
    private Button morePts,moreBal;
    private MaterialCardView pts,bal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr__scan);
        ptsL=findViewById(R.id.layout_more_points);
        balL=findViewById(R.id.layout_more_balance);
        pts=findViewById(R.id.points_help);
        bal=findViewById(R.id.balance_help);
        moreBal=findViewById(R.id.more);
        morePts=findViewById(R.id.morePts);
        FloatingActionButton scanBtn = findViewById(R.id.scanBtn);
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