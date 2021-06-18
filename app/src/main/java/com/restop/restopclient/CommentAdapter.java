package com.restop.restopclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.ContextThemeWrapper;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mData.get(position).getKey());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.name.setText(snapshot.child("Username").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.content.setText(mData.get(position).getContent());
        holder.date.setText(timestampToString((long) mData.get(position).getTimestamp()));
        holder.commentRatingShow.setRating(mData.get(position).getRating());
        getUserInfo(holder,position);
        if(!mData.get(position).getReply().isEmpty() ){
            holder.showCommentReply.setVisibility(View.VISIBLE);
            holder.showCommentReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext,R.style.Theme_AppCompat_Light_Dialog_Alert));
                    final View replyLayout = LayoutInflater.from(mContext).inflate(R.layout.show_reply,null);
                    builder.setView(replyLayout);
                    builder.setTitle("Admin reply");
                    TextView commentReply = replyLayout.findViewById(R.id.comment_reply_textview);
                    commentReply.setText(mData.get(position).getReply());
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }



    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        ImageView userPhoto, showCommentReply;
        TextView name,content,date;
        RatingBar commentRatingShow;
        public CommentViewHolder(View itemView){
            super(itemView);
            userPhoto = itemView.findViewById(R.id.comment_img);
            name = itemView.findViewById(R.id.comment_username);
            content = itemView.findViewById(R.id.comment_content);
            date = itemView.findViewById(R.id.comment_date);
            commentRatingShow = itemView.findViewById(R.id.comment_ratingbar);
            showCommentReply = itemView.findViewById(R.id.show_comment_reply);
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
        String date = DateFormat.format("dd/MM/yyyy",calendar).toString();
        return date;


    }
    public void removeItem(int position){
        mData.remove(position);
        notifyDataSetChanged();
        notifyItemRangeChanged(position, mData.size());
    }
}
