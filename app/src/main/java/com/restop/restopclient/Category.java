package com.restop.restopclient;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Category {
    private String Id;
    private String Name;
    private ArrayList<Option> AllOptions;
    interface CategoriesStatus{
        void isLoaded(ArrayList<Category> AllCategories);
    }

    public Category() {
    }
    public Category(String name, ArrayList<Option> allOptions) {
        Name = name;
        AllOptions = allOptions;
    }
    public Category(String id, String name, ArrayList<Option> allOptions) {
        Id = id;
        Name = name;
        AllOptions = allOptions;
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

    public ArrayList<Option> getAllOptions() {
        return AllOptions;
    }

    public void setAllOptions(ArrayList<Option> allOptions) {
        AllOptions = allOptions;
    }

    public static void ReadCategories(final CategoriesStatus categoriesStatus) {
        final ArrayList<Category> AllCategories = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Menu");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AllCategories.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name = ds.child("Name").getValue(String.class);
                    Option.ReadOptions(ds.getKey(), new Option.OptionsStatus() {
                        @Override
                        public void isLoaded(ArrayList<Option> AllOptions) {
                            AllCategories.add(new Category(ds.getKey(), name, AllOptions));
                            if(snapshot.getChildrenCount()==AllCategories.size()) categoriesStatus.isLoaded(AllCategories);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }



}
