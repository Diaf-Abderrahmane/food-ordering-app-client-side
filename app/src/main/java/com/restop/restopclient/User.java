package com.restop.restopclient;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class User {
    private String UserName;
    private String Email;
    private String Image;
    private int Points;

    public User() {
    }

    public User(String userName, String email, int points) {
        UserName = userName;
        Email = email;
        Points = points;
    }


    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        Points = points;
    }


    static void AddUser(User user, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                                                 @Override
                                                                                                 public void onComplete(@NonNull Task<AuthResult> task) {
                                                                                                     if (task.isSuccessful()) {
                                                                                                         String UId = task.getResult().getUser().getUid().toString();
                                                                                                         DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(UId);
                                                                                                         ref.setValue(user);
                                                                                                         User.SubscribeNotification(ref);
                                                                                                     }

                                                                                                 }
                                                                                             }
        );
    }

    public void EditUser(User user, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser User = auth.getCurrentUser();
        User.updateEmail(user.getEmail());
        User.updatePassword(password);
    }

    static void SubscribeNotification(DatabaseReference ref) {
        ref.child("NotificationActivation").setValue(1);
        FirebaseMessaging.getInstance().subscribeToTopic("Notification");
    }

    static void unSubscribeNotification(DatabaseReference ref) {
        ref.child("NotificationActivation").setValue(0);
        FirebaseMessaging.getInstance().unsubscribeFromTopic("Notification");
    }

}
