package com.example.apppolicial;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotifyAlert extends Application {
    public static final String CHANNEL_1_ID = "channel1";
    private NotificationManagerCompat notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels(){
		NotificationChannel channel1 = new NotificationChannel(
				CHANNEL_1_ID,
				"channel1",
				NotificationManager.IMPORTANCE_HIGH
		);
		channel1.setDescription("this is Channel 1");

		NotificationManager manager = getSystemService(NotificationManager.class);
		manager.createNotificationChannel(channel1);
	}

    public void sendOnChannel1(Context context){
        Uri path =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ context.getApplicationContext().getPackageName() + "/" + R.raw.sound);
        notificationManager = NotificationManagerCompat.from(context);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle("Alerta")
                .setContentText("Suspeito detectado!")
                .setSound(path)
                .setVibrate(new long[] {0, 1000, 500, 1000, 500, 1000})
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .build();

        notificationManager.notify(1, notification);
    }
}
