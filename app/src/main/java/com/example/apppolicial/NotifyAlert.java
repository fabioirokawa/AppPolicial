package com.example.apppolicial;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotifyAlert extends Application {
	public static final String CHANNEL_1_ID = "channel1";
	public static final String CHANNEL_2_ID = "channel2";

    private NotificationManagerCompat notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels(){
		NotificationChannel channel1 = null;
		NotificationChannel channel2 = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "channel1",
                    NotificationManager.IMPORTANCE_HIGH
            );
			channel2 = new NotificationChannel(
					CHANNEL_2_ID,
					"channel1",
					NotificationManager.IMPORTANCE_DEFAULT
			);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			channel1.setDescription("this is Channel 1");
			channel2.setDescription("this is Channel 2");
        }
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			manager.createNotificationChannel(channel1);
			manager.createNotificationChannel(channel2);
        }
    }

    public void alertNotification(Context context,String title,String content){
        Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Uri path =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ context.getApplicationContext().getPackageName() + "/" + R.raw.sound);
        notificationManager = NotificationManagerCompat.from(context);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle(title)
                .setContentText(content)
                .setSound(path)
                .setVibrate(new long[] {0, 1000, 500, 1000, 500, 1000})
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .build();

        notificationManager.notify(1, notification);
    }

    public void progressNotification(Context context,String title,String content){
		notificationManager = NotificationManagerCompat.from(context);

    	Notification notification = new NotificationCompat.Builder(context,CHANNEL_1_ID)
				.setSmallIcon(R.drawable.ic_launcher_foreground)
				.setContentTitle(title)
				.setContentText(content)
				.setProgress(0,0,true)
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.setCategory(NotificationCompat.CATEGORY_PROGRESS)
				.build();
		notificationManager.notify(2,notification);
    }

	public void errorNotification(Context context,String title, String content){
		notificationManager = NotificationManagerCompat.from(context);

		Notification notification = new NotificationCompat.Builder(context,CHANNEL_1_ID)
				.setSmallIcon(R.drawable.ic_launcher_foreground)
				.setContentTitle("ERRO")
				.setContentTitle(title)
				.setContentText(content)
				.setPriority(NotificationCompat.PRIORITY_HIGH)
				.setCategory(NotificationCompat.CATEGORY_ERROR)
				.build();
		notificationManager.notify(3,notification);
	}

    public void cancelNotification(Context context,int id){

    	notificationManager = NotificationManagerCompat.from(context);
    	notificationManager.cancel(id);
	}

}
