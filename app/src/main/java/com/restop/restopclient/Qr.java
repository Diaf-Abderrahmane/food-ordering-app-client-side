package com.restop.restopclient;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Qr {
    private String id;
    private int Price;
    private int Status;

    interface QrCode {
        void isLoaded(Qr qr);
    }

    ;

    public Qr() {
    }

    public Qr(String id, int price, int status) {
        this.id = id;
        Price = price;
        Status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public void ReadQr(String id, QrCode qrCode) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("QrCode").child(id);
        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                qrCode.isLoaded(task.getResult().getValue(Qr.class));
            }
        });
    }
    public void UpDateQr(String id, Qr qr){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("QrCode").child(id);
        ref.setValue(qr);
    }
}
