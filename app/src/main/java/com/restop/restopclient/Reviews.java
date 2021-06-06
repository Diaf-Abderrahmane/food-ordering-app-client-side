package com.restop.restopclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class  Reviews extends AppCompatActivity {
    private ImageView imgCurrentUser,restopPhoto;
    private EditText editComment;
    private Button btnAddComment;
    private RatingBar userRating;

    private ProgressBar progressBar;
    private float commentRating;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private RecyclerView RvComment;
    private CommentAdapter commentAdapter;
    private ArrayList<Comment> listComments;
    static String COMMENT_KEY = "Comments";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        listComments = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar);
        RvComment = findViewById(R.id.rv_comment);
        imgCurrentUser =  findViewById(R.id.current_user_img);
        editComment = findViewById(R.id.edit_comment);
        btnAddComment = findViewById(R.id.add_comment_button);
        userRating = findViewById(R.id.ratingBar);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        progressBar.setVisibility(View.INVISIBLE);
        userRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                commentRating = ratingBar.getRating();
            }
        });


        ////////////////////////////////////////////////////////
        if(firebaseUser != null && firebaseUser.getPhotoUrl() != null)
           Glide.with(this).load(firebaseUser.getPhotoUrl()).into(imgCurrentUser);
        else
            Glide.with(this).load(R.drawable.profile_pic).into(imgCurrentUser);

        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnAddComment.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                String commentContent = editComment.getText().toString();
                String uid =firebaseUser.getUid().toString();
                String uname =firebaseUser.getDisplayName();
                String uimg = firebaseUser.getPhotoUrl().toString();
                Comment comment = new Comment(commentContent,uid,uimg,uname,commentRating);

                addComment(comment);
            }
        });
        //ini recyclerview

        iniRvComment();
        restopPhoto = findViewById(R.id.resto_img);
        Glide.with(this).load(R.drawable.restop).into(restopPhoto);


    }


//----------------------------------------------------------------

/*    private void gotoActivity(Class<?> cls){
        navBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reviews.this,cls);
                startActivity(intent);
                finish();
            }
        });

    }
*/

    private void iniRvComment() {
        RvComment.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child(COMMENT_KEY);
        listComments = new ArrayList<>();

        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listComments.clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    Comment comment = snap.getValue(Comment.class);
                    listComments.add(comment);

                }
                Collections.reverse(listComments);
                commentAdapter = new CommentAdapter(getApplicationContext(),listComments);
                RvComment.setAdapter(commentAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void addComment(Comment comment) {

        DatabaseReference databaseReference = firebaseDatabase.getReference().child(COMMENT_KEY).push();

        comment.setKey(databaseReference.getKey());
        databaseReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("comment added successfully");
                btnAddComment.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                editComment.setText("");
                userRating.setRating(1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("failed to add comment : "+e.getMessage());
                btnAddComment.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}