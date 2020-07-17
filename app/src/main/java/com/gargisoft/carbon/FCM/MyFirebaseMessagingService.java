package com.gargisoft.carbon.FCM;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.gargisoft.carbon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kongzue.dialog.v3.WaitDialog;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.gargisoft.carbon.App.Carbon.CHANNEL_1_ID;
import static com.gargisoft.carbon.App.Carbon.CHANNEL_2_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
       createNotificationChannels();
       showNotification(remoteMessage.getData());
        Log.d("notificaiton", "onMessageReceived: "+remoteMessage.getData().values());
    }
  @Override
    public void onNewToken(String mToken) {
        super.onNewToken(mToken);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            String tokenID = FirebaseInstanceId.getInstance().getToken();
            Map<String,Object> map=new HashMap<>();
            map.put("tokenID",mToken);
            db.collection("user")
                    .document(FirebaseAuth.getInstance().getUid())
                    .set(map, SetOptions.merge());

            Log.d("tokenId", "onNewToken: "+mToken);
            Log.e("TOKEN",mToken);
        }

    }
    private void showNotification(Map<String , String > data){
        Intent notificationIntent = null;


        String title = "", body ="";

        notificationIntent = new Intent(MyFirebaseMessagingService.this, WaitDialog.class);
        NotificationManager manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channel = "com.gargi";

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel1
                    = new NotificationChannel(channel,"bildirim",
                    NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("Gargii");
            channel1 .enableLights(true);
            channel1.setLightColor(Color.BLUE);
            //   channel1.setVibrationPattern(new long[]{0,1000,500,1000});


            manager.createNotificationChannel(channel1);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,channel);

            builder.setSmallIcon(R.drawable.notification);

            builder.setWhen(System.currentTimeMillis());
            builder.setContentTitle( data.get("senderName"));
            builder.setContentText(data.get("msg"));

            //  builder.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.sound));
            builder.build().flags |= Notification.FLAG_AUTO_CANCEL;
            PendingIntent pendingClearScreenIntent = PendingIntent.getActivity(getApplicationContext(), (int)(Math.random() * 100), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingClearScreenIntent);

            builder.setContentInfo("info");
            builder.setAutoCancel(true);
            //   manager.cancelAll();

            manager.notify(new Random().nextInt(),builder.build());

        }
        else{
            // notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,channel);
            builder.setWhen(System.currentTimeMillis());
            builder.setSmallIcon(R.drawable.notification);
            builder.setContentTitle(data.get("senderName"));
            builder.setContentText(data.get("msg"));
            builder.setDefaults(Notification.DEFAULT_VIBRATE);

            //builder.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.sound));
            PendingIntent pendingClearScreenIntent = PendingIntent.getActivity(getApplicationContext(), (int)(Math.random() * 100), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingClearScreenIntent);

            builder.build().flags |= Notification.FLAG_AUTO_CANCEL;
            // manager.cancelAll();
            builder.setAutoCancel(true);


            manager.notify(new Random().nextInt(),builder.build());

        }
    }
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription("This is Channel 2");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }

}
