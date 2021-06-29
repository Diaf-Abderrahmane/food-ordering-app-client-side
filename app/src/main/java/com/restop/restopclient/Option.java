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

    interface OptionsStatus {
        void isLoaded(ArrayList<Option> AllOptions);
    }

    interface OptionsStatusC {
        void onDataChange();
    }

    interface ImgStatus {
        void isLoaded(Bitmap img);
    }

    public Option() {

    }

    public Option(String name, int price, String imgName, String description) {
        Name = name;
        Price = price;
        ImgName = imgName;
        Description = description;
    }

    public Option(String id, String name, int price, String imgName, String description) {
        Id = id;
        Name = name;
        Price = price;
        ImgName = imgName;
        Description = description;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    public void setPrice(int price) {
        Price = price;
    }

    public String getImgName() {
        return ImgName;
    }

    public void setImgName(String imgName) {
        ImgName = imgName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public static void ReadOptions(String id, OptionsStatus optionsStatus) {
        ArrayList<Option> AllOptions = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Menu").child(id).child("All");
        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        Option o = ds.getValue(Option.class);
                        AllOptions.add(o);
                        if (task.getResult().getChildrenCount() == AllOptions.size())
                            optionsStatus.isLoaded(AllOptions);
                    }
                }
            }
        });
    }

    public static void getImg(String imgName, ImgStatus imgStatus) {

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
