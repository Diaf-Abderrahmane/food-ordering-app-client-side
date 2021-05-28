package com.restop.restopclient;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.zip.Inflater;

public class Notification extends FirebaseMessagingService {

    static class NotificationObg{
        private String Id;
        private String Title;
        private String Text;
        private String ImgName;

        interface NotificationObgI{
            void isLoaded(NotificationObg notificationObg);
        }
        interface NotificationObgImg{
            void isLoaded(Bitmap img);
        }

        public NotificationObg() {
        }

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getText() {
            return Text;
        }

        public void setText(String text) {
            Text = text;
        }

        public String getImgName() {
            return ImgName;
        }

        public void setImgName(String imgName) {
            ImgName = imgName;
        }

        static void getNotificationObg(String id,NotificationObgI notificationObgI){
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Notification").child(id);
            ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()) notificationObgI.isLoaded(task.getResult().getValue(NotificationObg.class));
                }
            });
        }
        public static void getImg(String imgName, NotificationObgImg notificationObgImg){

            String imgPath = "Notification/" + imgName;
            StorageReference SRef = FirebaseStorage.getInstance().getReference().child(imgPath);
            SRef.getBytes(2048 * 2048).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap img = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    notificationObgImg.isLoaded(img);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
        }

        if (remoteMessage.getData() != null) {
            NotificationObg.getNotificationObg(remoteMessage.getData().get("id"), new NotificationObg.NotificationObgI() {
                @Override
                public void isLoaded(NotificationObg notificationObg) {
                    if (notificationObg!=null){
                        NotificationObg.getImg(notificationObg.getImgName(), new NotificationObg.NotificationObgImg() {
                            @Override
                            public void isLoaded(Bitmap img) {
                                    RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
                                    remoteViews.setImageViewBitmap(R.id.NotificationImage,img);
                                    remoteViews.setTextViewText(R.id.NotificationTitle,notificationObg.getTitle());
                                    remoteViews.setTextViewText(R.id.NotificationText,notificationObg.getText());

                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Notification.this, "CHANNEL_ID")
                                            .setSmallIcon(R.drawable.logo)
                                            .setCustomBigContentView(remoteViews)
                                            .setCustomContentView(remoteViews)
                                            .setPriority(NotificationCompat.PRIORITY_MAX);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        int importance = NotificationManager.IMPORTANCE_DEFAULT;
                                        NotificationChannel channel = new NotificationChannel("CHANNEL_ID", "name", importance);
                                        channel.setDescription("description");
                                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                        notificationManager.createNotificationChannel(channel);
                                    }

                                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Notification.this);
                                    android.app.Notification n=builder.build();
                                    n.flags|=android.app.Notification.FLAG_AUTO_CANCEL;
                                    n.defaults|=android.app.Notification.DEFAULT_SOUND;
                                    n.defaults|=android.app.Notification.DEFAULT_VIBRATE;
                                    notificationManager.notify(1, n);

                            }
                        });
                    }
                }
            });
        }
    }
}
