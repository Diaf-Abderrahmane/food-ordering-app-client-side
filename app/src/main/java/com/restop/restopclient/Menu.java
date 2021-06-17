package com.restop.restopclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class Menu extends Fragment {

    int CPosition=-1;
    ArrayList<Category> AllCategories;
    RecyclerView.SmoothScroller smoothScroller;
    RecyclerView.SmoothScroller smoothScroller2;
    CustomAdapter adapter;
    CustomAdapter adapter2;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    ProgressBar progressBar;
    TextView[] CategoryList;

    LinearLayout VMenu;

    TextView textView0=null;

    AlertDialog.Builder alertDialog;
    Dialog dialog;
    View viewP;
    ImageView PopUpOptionImg;
    TextView PopUpOptionName;
    TextView PopUpOptionDescription;
    TextView PopUpOptionPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_menu, container, false);


        smoothScroller = new LinearSmoothScroller(getActivity()) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        smoothScroller2 = new LinearSmoothScroller(getActivity()) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };

        progressBar=view.findViewById(R.id.progressBar);
        VMenu=view.findViewById(R.id.VMenu);


        recyclerView = view.findViewById(R.id.Menu);
        recyclerView2 = view.findViewById(R.id.AllCategories);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));

        final int[] position = {0};
        final int[] p = {0};

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                p[0] = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (position[0] != p[0] && CPosition==-1){
                    smoothScroller2.setTargetPosition(p[0]);
                    recyclerView2.getLayoutManager().startSmoothScroll(smoothScroller2);
                    SelectCategory(CategoryList[p[0]]);
                }
                if(p[0]==CPosition) CPosition=-1;
                position[0] = p[0];
            }
        });

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
                adapter = new CustomAdapter();
                adapter2 = new CustomAdapter(2);
                recyclerView.setAdapter(adapter);
                recyclerView2.setAdapter(adapter2);
                progressBar.setVisibility(View.INVISIBLE);
                VMenu.setVisibility(View.VISIBLE);
                CategoryList=new TextView[allCategories.size()];
            }
        });
    }
    public void SelectCategory(TextView textView1){
        if(textView1!=null && textView0!=textView1) {
            int py = textView1.getPaddingTop();
            int px = textView1.getPaddingLeft();
            textView1.setBackgroundResource(R.drawable.bg_view_list_category_s);
            textView1.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            textView1.setPadding(px, py, px, py);
            if (textView0 != null) {
                textView0.setBackgroundResource(R.drawable.bg_view_list_category);
                textView0.setTextColor(ContextCompat.getColor(getActivity(), R.color.eblack));
                textView0.setPadding(px, py, px, py);
            }
            textView0 = textView1;
        }
    }
    public void PopUpOption(int categoryIndex, int position, Drawable img) {
        alertDialog = new AlertDialog.Builder(getActivity());
        viewP = getLayoutInflater().inflate(R.layout.popup_option, null);
        PopUpOptionImg=viewP.findViewById(R.id.PopUpOption_img);
        PopUpOptionName=viewP.findViewById(R.id.PopUpOption_name);
        PopUpOptionDescription=viewP.findViewById(R.id.PopUpOption_description);

        /*LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(PopUpOptionImg.getDrawable().getIntrinsicWidth(), PopUpOptionImg.getDrawable().getIntrinsicWidth());
        PopUpOptionImg.setLayoutParams(layoutParams);*/

        PopUpOptionPrice=viewP.findViewById(R.id.PopUpOption_price);
        PopUpOptionName.setText(AllCategories.get(categoryIndex).getAllOptions().get(position).getName());
        PopUpOptionDescription.setText(AllCategories.get(categoryIndex).getAllOptions().get(position).getDescription());
        String price = AllCategories.get(categoryIndex).getAllOptions().get(position).getPrice() +" DZD";
        PopUpOptionPrice.setText(price);
        if(img!=null)PopUpOptionImg.setImageDrawable(img);

        alertDialog.setView(viewP);
        dialog = alertDialog.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
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
            private final CardView OptionView;
            private final TextView OptionName;
            private final TextView OptionDescription;
            private final TextView OptionPrice;
            private final ImageView OptionImg;

            public ViewHolder1(View view) {
                super(view);
                OptionView=view.findViewById(R.id.Option_v);
                OptionName = view.findViewById(R.id.OptionName);
                OptionDescription = view.findViewById(R.id.OptionDescription);
                OptionPrice = view.findViewById(R.id.OptionPrice);
                OptionImg = view.findViewById(R.id.OptionImg);

            }

            public CardView getOptionView() { return OptionView;  }
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
                    viewHolder1.getOptionName().setText(AllCategories.get(CategoryIndex).getAllOptions().get(position).getName());
                    viewHolder1.getOptionDescription().setText(AllCategories.get(CategoryIndex).getAllOptions().get(position).getDescription());
                    String price = AllCategories.get(CategoryIndex).getAllOptions().get(position).getPrice() +" DZD";
                    viewHolder1.getOptionPrice().setText(price);

                    final Bitmap[] Img=new Bitmap[1];
                    Option.getImg(AllCategories.get(CategoryIndex).getAllOptions().get(position).getImgName(), new Option.ImgStatus() {
                        @Override
                        public void isLoaded(Bitmap img) {
                            if(img!=null)viewHolder1.getOptionImg().setImageBitmap(img);
                            else Option.getImg("default.jpg", new Option.ImgStatus() {
                                @Override
                                public void isLoaded(Bitmap img) {
                                    Img[0] =img;
                                    viewHolder1.getOptionImg().setImageBitmap(img);
                                }
                            });
                        }
                    });
                    viewHolder1.getOptionView().setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            PopUpOption(CategoryIndex,position,viewHolder1.getOptionImg().getDrawable());
                            return false;
                        }
                    });
                    break;
                case 2:
                    ViewHolder2 viewHolder2=(ViewHolder2) Holder;
                    CategoryList[position]=viewHolder2.getCategoryName();
                    viewHolder2.getCategoryName().setText(AllCategories.get(position).getName());
                    if(position==0){
                        SelectCategory(viewHolder2.getCategoryName());
                    }
                    viewHolder2.getCategoryName().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CPosition=position;
                            SelectCategory(viewHolder2.getCategoryName());
                            smoothScroller2.setTargetPosition(position);
                            recyclerView2.getLayoutManager().startSmoothScroll(smoothScroller2);
                            smoothScroller.setTargetPosition(position);
                            recyclerView.getLayoutManager().startSmoothScroll(smoothScroller);
                        }
                    });
                    break;

                default:
                    ViewHolder0 viewHolder0=(ViewHolder0) Holder;
                    viewHolder0.getCategoryName().setText(AllCategories.get(position).getName());

                    viewHolder0.getCategoryOptions().setLayoutManager(new LinearLayoutManager(getActivity()));
                    CustomAdapter adapterO = new CustomAdapter(1,position);
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