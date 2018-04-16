package com.example.kayletiu.escapade;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jayvee Gabriel on 14/03/2018.
 */

public class NotificationPublisher extends BroadcastReceiver {

    private static final String CHANNEL_ID = "com.PlayWorldActivity";
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("NotificationPublisher", "Received!");
        // Create an explicit intent for an Activity in your app
        Intent pIntent = new Intent(context, StartActivity.class);
        pIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, pIntent, 0);

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.slime)
                .setContentTitle("Escapade")
                .setContentText("Your lives are refilled!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManagerCompat.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }


        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(001, mBuilder.build());
//        final SharedPreferences sharedPreferences = context.getSharedPreferences("MySettings", Context.MODE_PRIVATE);
//        final SharedPreferences.Editor preferenceEditor = context.getSharedPreferences("MySettings", 0).edit();
//        final int[] lives = {0};
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                if (lives[0] < 5) {
//                    lives[0] = sharedPreferences.getInt("lives", 0);
//                    lives[0]++;
//                    preferenceEditor.putInt("lives", lives[0]);
//                    preferenceEditor.apply();
//                }
//                else {
//                    preferenceEditor.putInt("lives", 5);
//                    preferenceEditor.apply();
//                }
//            }
//        };
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(timerTask, 300000, 300000);
        Log.i("NotificationPublisher", "Notified");
    }
}
