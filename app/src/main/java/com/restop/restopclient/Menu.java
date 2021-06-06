package com.restop.restopclient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class Menu extends Fragment {

    ArrayList<Category> AllCategories;
    Menu1.CustomAdapter adapter;
    Menu1.CustomAdapter adapter2;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    ProgressBar progressBar;
    LinearLayout VMenu;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view =inflater.inflate(R.layout.fragment_menu, container, false);

        FirebaseMessaging.getInstance().subscribeToTopic("Notification");
        //=================================


        progressBar=view.findViewById(R.id.progressBar);
        VMenu=view.findViewById(R.id.VMenu);


        recyclerView = view.findViewById(R.id.Menu);
        recyclerView2 = view.findViewById(R.id.AllCategories);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));

        final Boolean[] c = {false};
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected && !c[0]) {
                    Refresh();
                    c[0] =true;
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    VMenu.setVisibility(View.INVISIBLE);
                    c[0]=false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });




        return view;

    }
    public void Refresh(){
        Category.ReadCategories(new Category.CategoriesStatus() {
            @Override
            public void isLoaded(ArrayList<Category> allCategories) {
                AllCategories = Category.OrderCategories(allCategories);
                adapter = new Menu1.CustomAdapter();
                adapter2 = new Menu1.CustomAdapter(2);
                recyclerView.setAdapter(adapter);
                recyclerView2.setAdapter(adapter2);
                progressBar.setVisibility(View.INVISIBLE);
                VMenu.setVisibility(View.VISIBLE);
            }
        });
    }

    public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private int ViewType=0;
        private int CategoryIndex;

        public class ViewHolder0 extends RecyclerView.ViewHolder {
            private final TextView CategoryName;
            private final RecyclerView CategoryOptions;

            public ViewHolder0(View view) {
                super(view);

                CategoryName = view.findViewById(R.id.CategoryName);
                CategoryOptions = view.findViewById(R.id.CategoryOptions);
            }

            public RecyclerView getCategoryOptions() { return CategoryOptions;    }
            public TextView getCategoryName() {  return CategoryName;  }
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

        public CustomAdapter() {
        }
        public CustomAdapter(int viewType) {
            ViewType=viewType;
        }
        public CustomAdapter(int viewType, int categoryIndex) {
            ViewType=viewType;
            CategoryIndex=categoryIndex;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view0 = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_category, parent, false);
            View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_option, parent, false);
            View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_category, parent, false);

            switch (ViewType){
                case 1:
                    return new Menu.CustomAdapter.ViewHolder1(view1);
                case 2:
                    return new Menu.CustomAdapter.ViewHolder2(view2);
                default:
                    return new Menu.CustomAdapter.ViewHolder0(view0);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder Holder, final int position) {
            switch (ViewType) {
                case 1:
                    Menu1.CustomAdapter.ViewHolder1 viewHolder1=(Menu1.CustomAdapter.ViewHolder1) Holder;
                    viewHolder1.getOptionName().setText(AllCategories.get(CategoryIndex).getAllOptions().get(position).getName());
                    viewHolder1.getOptionDescription().setText(AllCategories.get(CategoryIndex).getAllOptions().get(position).getDescription());
                    String price = AllCategories.get(CategoryIndex).getAllOptions().get(position).getPrice() +" DZD";
                    viewHolder1.getOptionPrice().setText(price);
                    Option.getImg(AllCategories.get(CategoryIndex).getAllOptions().get(position).getImgName(), new Option.ImgStatus() {
                        @Override
                        public void isLoaded(Bitmap img) {
                            viewHolder1.getOptionImg().setImageBitmap(img);
                        }
                    });


                    break;
                case 2:
                    Menu1.CustomAdapter.ViewHolder2 viewHolder2=(Menu1.CustomAdapter.ViewHolder2) Holder;
                    viewHolder2.getCategoryName().setText(AllCategories.get(position).getName());
                    viewHolder2.getCategoryName().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,new RecyclerView.State(), position);
                        }
                    });
                    break;

                default:
                    Menu.CustomAdapter.ViewHolder0 viewHolder0=(Menu.CustomAdapter.ViewHolder0) Holder;
                    viewHolder0.getCategoryName().setText(AllCategories.get(position).getName());

                    viewHolder0.getCategoryOptions().setLayoutManager(new LinearLayoutManager(getActivity()));
                    Menu.CustomAdapter adapterO = new Menu.CustomAdapter(1,position);
                    viewHolder0.getCategoryOptions().setAdapter(adapterO);
                    break;
            }

        }

        @Override
        public int getItemCount() {
            switch (ViewType){
                case 1:
                    return AllCategories.get(CategoryIndex).getAllOptions().size();
                default:
                    return AllCategories.size();
            }
        }
    }
}