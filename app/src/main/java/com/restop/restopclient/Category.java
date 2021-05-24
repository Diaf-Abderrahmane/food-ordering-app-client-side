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
    private int Index;
    private ArrayList<Option> AllOptions;
    interface CategoriesStatus{
        void isLoaded(ArrayList<Category> allCategories);
    }

    interface CategoriesStatusC{
        void onDataChange();
    }

    public Category() {
    }
    public Category(String name, ArrayList<Option> allOptions) {
        Name = name;
        AllOptions = allOptions;
    }
    public Category(String name, int index, ArrayList<Option> allOptions) {
        Name = name;
        Index = index;
        AllOptions = allOptions;
    }
    public Category(String id, String name, int index, ArrayList<Option> allOptions) {
        Id = id;
        Name = name;
        Index = index;
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

    public int getIndex() {  return Index;  }

    public void setIndex(int index) {  Index = index;  }

    public ArrayList<Option> getAllOptions() {
        return AllOptions;
    }

    public void setAllOptions(ArrayList<Option> allOptions) {
        AllOptions = allOptions;
    }

    public static void ReadCategories(final CategoriesStatus categoriesStatus) {
        final ArrayList<Category> AllCategories = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Menu");
        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    AllCategories.clear();

                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        String name = ds.child("name").getValue(String.class);
                        int index=ds.child("index").getValue(int.class);

                        if (ds.child("All").exists()) {
                            int finalIndex = index;
                            Option.ReadOptions(ds.getKey(), new Option.OptionsStatus() {
                                @Override
                                public void isLoaded(ArrayList<Option> AllOptions) {
                                    AllCategories.add(new Category(ds.getKey(), name, finalIndex, AllOptions));
                                    if (task.getResult().getChildrenCount() == AllCategories.size()) categoriesStatus.isLoaded(AllCategories);
                                }
                            });
                        }else{
                            AllCategories.add(new Category(ds.getKey(), name, index, new ArrayList<>()));
                            if (task.getResult().getChildrenCount() == AllCategories.size()) categoriesStatus.isLoaded(AllCategories);
                        }
                    }
                    if(!task.getResult().hasChildren()) categoriesStatus.isLoaded(AllCategories);}
            }
        });
    }


    public static ArrayList<Category> OrderCategories(ArrayList<Category> allCategories){
        Category category;
        for (int i=0;i<allCategories.size()-1;i++)
            for(int j=i+1;j<allCategories.size();j++)
                if(allCategories.get(i).getIndex()>=allCategories.get(j).getIndex()) {
                    category = allCategories.get(i);
                    allCategories.set(i,allCategories.get(j));
                    allCategories.set(j,category);
                }
        return allCategories;
    }

}
