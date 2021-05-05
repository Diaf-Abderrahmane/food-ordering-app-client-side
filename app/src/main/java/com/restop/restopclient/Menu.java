package com.restop.restopclient;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Menu extends AppCompatActivity {
    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayout VMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        progressBar=findViewById(R.id.progressBar);
        VMenu=findViewById(R.id.VMenu);


        recyclerView = findViewById(R.id.Menu);
        RecyclerView recyclerView2 = findViewById(R.id.AllCategories);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));

        Category.ReadCategories(new Category.CategoriesStatus() {
            @Override
            public void isLoaded(ArrayList<Category> AllCategories) {
                CustomAdapter adapter = new CustomAdapter(AllCategories);
                CustomAdapter adapter2 = new CustomAdapter(2,AllCategories);
                recyclerView.setAdapter(adapter);
                recyclerView2.setAdapter(adapter2);

                progressBar.setVisibility(View.INVISIBLE);
                VMenu.setVisibility(View.VISIBLE);
            }
        });

    }

    public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Category> AllCategories;
        private ArrayList<Option> AllOptions;
        private int ViewType=0;

        public class ViewHolder0 extends RecyclerView.ViewHolder {
            private final TextView CategoryName;
            private final RecyclerView CategoryOptions;

            public ViewHolder0(View view) {
                super(view);

                CategoryName = view.findViewById(R.id.CategoryName);
                CategoryOptions = view.findViewById(R.id.CategoryOptions);
            }

            public TextView getCategoryName() {
                return CategoryName;
            }
        }

        public class ViewHolder1 extends RecyclerView.ViewHolder {
            private final TextView OptionName;
            private final TextView OptionDescription;
            private final TextView OptionPrice;
            private final ImageView OptionImg;

            public ViewHolder1(View view) {
                super(view);

                OptionName = view.findViewById(R.id.OptionName);
                OptionDescription = view.findViewById(R.id.OptionDescription);
                OptionPrice = view.findViewById(R.id.OptionPrice);
                OptionImg = view.findViewById(R.id.OptionImg);

            }

            public TextView getOptionName() { return OptionName;  }
            public TextView getOptionDescription() { return OptionDescription;  }
            public TextView getOptionPrice() { return OptionPrice;  }
            public ImageView getOptionImg() { return OptionImg;  }
        }


        public class ViewHolder2 extends RecyclerView.ViewHolder {
            private final TextView CategoryName;

            public ViewHolder2(View view) {
                super(view);

                CategoryName = view.findViewById(R.id.CategoryName);
            }

            public TextView getCategoryName() {
                return CategoryName;
            }
        }

        public CustomAdapter(ArrayList<Category> allCategories) {
            AllCategories = allCategories;
        }
        public CustomAdapter(int viewType, ArrayList<Category> allCategories) {
            AllCategories = allCategories;
            ViewType=viewType;
        }
        public CustomAdapter(ArrayList<Option> allOptions,int viewType) {
            AllOptions = allOptions;
            ViewType=viewType;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view0 = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_category, parent, false);
            View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_option, parent, false);
            View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_category, parent, false);

            switch (ViewType){
                case 1:
                    return new ViewHolder1(view1);
                case 2:
                    return new ViewHolder2(view2);
                default:
                    return new ViewHolder0(view0);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder Holder, final int position) {
            switch (ViewType) {
                case 1:
                    ViewHolder1 viewHolder1=(ViewHolder1) Holder;
                    viewHolder1.getOptionName().setText(AllOptions.get(position).getName());
                    viewHolder1.getOptionDescription().setText(AllOptions.get(position).getDescription());
                    String price = AllOptions.get(position).getPrice() +" DZD";
                    viewHolder1.getOptionPrice().setText(price);
                    Option.getImg(AllOptions.get(position).getImgName(), new Option.ImgStatus() {
                        @Override
                        public void isLoaded(Bitmap img) {
                            viewHolder1.getOptionImg().setImageBitmap(img);
                        }
                    });
                    break;
                case 2:
                    ViewHolder2 viewHolder2=(ViewHolder2) Holder;
                    viewHolder2.getCategoryName().setText(AllCategories.get(position).getName());
                    viewHolder2.getCategoryName().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,new RecyclerView.State(), position);
                        }
                    });
                    break;

                default:
                    ViewHolder0 viewHolder0=(ViewHolder0) Holder;
                    viewHolder0.getCategoryName().setText(AllCategories.get(position).getName());

                    viewHolder0.CategoryOptions.setLayoutManager(new LinearLayoutManager(Menu.this));
                    CustomAdapter adapterO = new CustomAdapter(AllCategories.get(position).getAllOptions(),1);
                    viewHolder0.CategoryOptions.setAdapter(adapterO);

                    break;
            }

        }

        @Override
        public int getItemCount() {
            switch (ViewType){
                case 1:
                    return AllOptions.size();
                default:
                    return AllCategories.size();
            }
        }
    }

}


