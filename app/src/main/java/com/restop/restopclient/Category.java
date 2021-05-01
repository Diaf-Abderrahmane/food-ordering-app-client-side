package com.restop.restopclient;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Category {
    private String Id;
    private String Name;
    private ArrayList<Option> AllOptions;
    interface Categories{
        void isLoaded(ArrayList<Category> AllCategories);
    }


    public Category() {
    }

    public Category(String id, String name, ArrayList<Option> allOptions) {
        Id = id;
        Name = name;
        AllOptions = allOptions;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public ArrayList<Option> getAllOptions() {
        return AllOptions;
    }
    public static void ReadCategory(Categories categories){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Menu");
        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ArrayList<Category> AllCategories=new ArrayList<>();
                for (DataSnapshot ds:task.getResult().getChildren()){
                    String name=task.getResult().child("Name").toString();
                    Option.ReadOptions(ds.getKey(), new Option.Options() {
                        @Override
                        public void isLoaded(ArrayList<Option> AllOptions) {
                            AllCategories.add(new Category(task.getResult().getKey().toString(),name,AllOptions));
                        }
                    });
                }
                categories.isLoaded(AllCategories);
            }
        });
    }
}
