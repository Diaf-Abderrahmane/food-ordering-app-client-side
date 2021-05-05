package com.restop.restopclient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Option {
    private String Id;
    private String Name;
    private int Price;
    private String ImgName;
    private String Description;

    interface OptionsStatus{
        void isLoaded(ArrayList<Option> AllOptions);
    }
    interface ImgStatus{
        void isLoaded(Bitmap img);
    }

    public Option() {

    }

    public String getId() {
        return Id;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public int getPrice() {
        return Price;
    }
    public String getImgName() {
        return ImgName;
    }
    public String getDescription() {
        return Description;
    }

    public static void ReadOptions(String id,OptionsStatus optionsStatus) {
        ArrayList<Option> AllOptions = new ArrayList<>();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Menu").child(id).child("All");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()) {
                    Option o=ds.getValue(Option.class);
                    AllOptions.add(o);
                    if (snapshot.getChildrenCount()==AllOptions.size()) optionsStatus.isLoaded(AllOptions);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getImg(String imgName,ImgStatus imgStatus){

        String imgPath = "Menu/" + imgName;
        StorageReference SRef = FirebaseStorage.getInstance().getReference().child(imgPath);
        SRef.getBytes(2048 * 2048).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap img = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imgStatus.isLoaded(img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }



}
