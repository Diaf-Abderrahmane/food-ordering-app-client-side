package com.restop.restopclient;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Option {
    private String Id;
    private String Name;
    private int Price;
    private String ImgName;
    private String Description;
    interface Options{
        void isLoaded(ArrayList<Option> AllOptions);
    }

    public Option() {
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

    public String getName() {
        return Name;
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
    public static void ReadOptions(String id,Options options){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(id);
        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ArrayList<Option> AllOptions = new ArrayList<>();
                for (DataSnapshot ds:task.getResult().getChildren()){
                    AllOptions.add(ds.getValue(Option.class));
                }
                options.isLoaded(AllOptions);

            }

        });


    }
}
