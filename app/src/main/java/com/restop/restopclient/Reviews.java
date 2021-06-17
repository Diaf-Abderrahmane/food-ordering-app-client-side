package com.restop.restopclient;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;


public class Reviews extends Fragment {

    private ImageView imgCurrentUser, restopLogo;
    private EditText editComment;
    private Button btnAddComment, btnEditComment, btnDeleteComment;
    private RatingBar userRating;
    private CardView commentCard, editDeleteCard;
    private TextView description;
    private ProgressBar addBtnProgressBar,logoProgressBar;
    private float commentRating;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private RecyclerView RvComment;
    private CommentAdapter commentAdapter;
    private ArrayList<Comment> listComments;
    final static String COMMENT_KEY = "Comments",DESCRIPTION_KEY = "resto_description";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);



        View view = inflater.inflate(R.layout.fragment_reviews, container, false);
        listComments = new ArrayList<>();
        addBtnProgressBar = view.findViewById(R.id.progressBar);
        RvComment = view.findViewById(R.id.rv_comment);
        imgCurrentUser = view.findViewById(R.id.current_user_img);
        editComment = view.findViewById(R.id.add_comment_edittext);
        btnAddComment = view.findViewById(R.id.add_comment_button);
        btnEditComment = view.findViewById(R.id.edit_comment_button);
        btnDeleteComment = view.findViewById(R.id.delete_comment_button);
        userRating = view.findViewById(R.id.ratingBar);
        editDeleteCard = view.findViewById(R.id.edit_delete_card);
        commentCard = view.findViewById(R.id.comment_card);
        restopLogo = view.findViewById(R.id.resto_logo);
        logoProgressBar = view.findViewById(R.id.resto_logo_progress_bar);
        description = view.findViewById(R.id.resto_description);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // launch visibility
        restopLogo.setVisibility(View.INVISIBLE);
        logoProgressBar.setVisibility(View.VISIBLE);
        addBtnProgressBar.setVisibility(View.INVISIBLE);
        commentCard.setVisibility(View.VISIBLE);
        editDeleteCard.setVisibility(View.INVISIBLE);


        userRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                commentRating = ratingBar.getRating();
            }
        });

        // if the user already commented
        // show two buttons
        // either delete the comment
        // or edit it
        DatabaseReference commentRef = firebaseDatabase.getReference().child("Comments").child(firebaseUser.getUid());
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                commentCard.setVisibility(View.INVISIBLE);
                editDeleteCard.setVisibility(View.VISIBLE);
                btnEditComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commentCard.setVisibility(View.VISIBLE);
                        editDeleteCard.setVisibility(View.INVISIBLE);
                    }
                });
                btnDeleteComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        commentRef.removeValue();
                        commentAdapter.removeItem(getPosition());
                        commentCard.setVisibility(View.VISIBLE);
                        editDeleteCard.setVisibility(View.INVISIBLE);
                        editComment.setText("");
                        userRating.setRating(0);
                    }
                });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //getting the logo from firebase
        getLogo();

        getDescription();

        getUserPhoto();


        //if user wanted to add a comment
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAddComment.setVisibility(View.INVISIBLE);
                addBtnProgressBar.setVisibility(View.VISIBLE);
                commentCard.setVisibility(View.INVISIBLE);
                editDeleteCard.setVisibility(View.VISIBLE);
                String commentContent = editComment.getText().toString();

                String uname = firebaseUser.getDisplayName();


                Comment comment = new Comment(commentContent, uname, commentRating);
                addComment(comment);
            }


        });
        //initialize recyclerview
        iniRvComment();



        return view;
    }

private void getLogo(){
    DatabaseReference logoRef = firebaseDatabase.getReference().child("About_Us");
    logoRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            restopLogo.setVisibility(View.VISIBLE);
            logoProgressBar.setVisibility(View.INVISIBLE);
            String logo = snapshot.child("LogoUrl").getValue(String.class);
            Glide.with(getActivity()).load(logo).into(restopLogo);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });

}
//----------------------------------------------------------------
// this method gets the restaurant description and puts it in the description TextView
    private void getDescription(){
        DatabaseReference reference = firebaseDatabase.getReference().child("About_Us");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(DESCRIPTION_KEY))
                    description.setText(snapshot.child(DESCRIPTION_KEY).getValue().toString());
                else
                    description.setText("THE DESCRIPTION IS NOT ADDED YET");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
// this method gets the position of the current user comment
private int getPosition(){
    int position=0;
    while(position<listComments.size()){
        if(listComments.get(position).getKey().equals(firebaseUser.getUid()))
            break;
        position++;
    }
    return position;
}

// this method gets the current user photo from firebase
private void getUserPhoto() {
        DatabaseReference reference = firebaseDatabase.getReference().child("Users");
        reference.child(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if(task.getResult().hasChild("image")){
                    String image = task.getResult().child("image").getValue(String.class);
                    Glide.with(getActivity()).load(image).into(imgCurrentUser);

                }
                else
                    Glide.with(getActivity()).load(R.drawable.profile_pic).into(imgCurrentUser);
            }

        });
    }

    // this method gets comments from firebase and puts them in a recyclerview
    private void iniRvComment() {
        RvComment.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                commentAdapter = new CommentAdapter(getActivity(),listComments);
                RvComment.setAdapter(commentAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void addComment(Comment comment) {

        DatabaseReference databaseReference = firebaseDatabase.getReference().child(COMMENT_KEY);

        comment.setKey(firebaseUser.getUid());
        databaseReference.child(firebaseUser.getUid()).setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("comment added successfully");
                btnAddComment.setVisibility(View.VISIBLE);
                addBtnProgressBar.setVisibility(View.INVISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("failed to add comment : "+e.getMessage());
                btnAddComment.setVisibility(View.VISIBLE);
                addBtnProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void showMessage(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
    }
}