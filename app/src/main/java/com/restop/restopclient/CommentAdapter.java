package com.restop.restopclient;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private FirebaseAuth firebaseAuth;
    private Context mContext;

    private ArrayList<Comment> mData;

    public CommentAdapter(Context mContext, ArrayList<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_comment,parent,false);
        return new CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        ///////////////////////////

        firebaseAuth = FirebaseAuth.getInstance();
        holder.name.setText(mData.get(position).getUname());
        holder.content.setText(mData.get(position).getContent());
        holder.date.setText(timestampToString((long) mData.get(position).getTimestamp()));
        holder.commentRatingShow.setNumStars((int) mData.get(position).getRating());
        getUserInfo(holder,position);

        if(firebaseAuth.getCurrentUser().getUid().equals(mData.get(position).getKey())){
            holder.removeComment.setVisibility(View.VISIBLE);
            holder.removeComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child("Comments").child(mData.get(position).getKey());
                    commentRef.removeValue();
                    mData.remove(position);
                    notifyDataSetChanged();
                    notifyItemRangeChanged(position, mData.size());
                }
            });
        }


    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        ImageView userPhoto;
        TextView name,content,date,removeComment;
        RatingBar commentRatingShow;
        public CommentViewHolder(View itemView){
            super(itemView);
            userPhoto = itemView.findViewById(R.id.comment_img);
            name = itemView.findViewById(R.id.comment_username);
            content = itemView.findViewById(R.id.comment_content);
            date = itemView.findViewById(R.id.comment_date);
            commentRatingShow = itemView.findViewById(R.id.comment_ratingbar);
            removeComment = itemView.findViewById(R.id.comment_remove);

        }
    }

    private void getUserInfo(@NonNull CommentViewHolder holder, int position) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.child(mData.get(position).getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    if (snapshot.hasChild("image")) {
                        String image = snapshot.child("image").getValue(String.class);
                        Glide.with(mContext).load(image).into(holder.userPhoto);


                    }
                    else
                        Glide.with(mContext).load(R.drawable.profile_pic).into(holder.userPhoto);
                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd/MM/yyyy hh:mm aaa",calendar).toString();
        return date;


    }
}
