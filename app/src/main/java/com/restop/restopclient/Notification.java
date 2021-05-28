package com.restop.restopclient;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.Toast;

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
        NotificationObg notificationObg;
        //remoteMessage.getFrom()

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            remoteMessage.getData();

            /*if (true) {
                scheduleJob();
            } else {
                handleNow();
            }*/

        }

        if (remoteMessage.getNotification() != null && false) {
            NotificationObg.getNotificationObg(remoteMessage.getNotification().getTitle(), new NotificationObg.NotificationObgI() {
                @Override
                public void isLoaded(NotificationObg notificationObg) {
                    if (notificationObg!=null){
                        NotificationObg.getImg(notificationObg.getImgName(), new NotificationObg.NotificationObgImg() {
                            @Override
                            public void isLoaded(Bitmap img) {
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(Notification.this, "CHANNEL_ID")
                                        .setContentTitle(notificationObg.getTitle())
                                        .setContentText(notificationObg.getText())
                                        .setLargeIcon(img)
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                                    NotificationChannel channel = new NotificationChannel("CHANNEL_ID", "name", importance);
                                    channel.setDescription("description");
                                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                    notificationManager.createNotificationChannel(channel);
                                }

                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Notification.this);
                                notificationManager.notify(1, builder.build());
                            }
                        });
                    }
                }
            });
        }
    }
}
