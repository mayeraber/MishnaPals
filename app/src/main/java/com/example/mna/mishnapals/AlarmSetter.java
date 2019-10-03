package com.example.mna.mishnapals;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

/**
 * Created by MNA on 10/21/2017.
 */

public class AlarmSetter extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent touchNotification = new Intent(context, MyMishnayos.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, touchNotification, 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel siyumReminder = new NotificationChannel("REMINDER", "Siyum Reminder", NotificationManager.IMPORTANCE_DEFAULT);
            siyumReminder.setLightColor(Color.CYAN);
            notificationManager.createNotificationChannel(siyumReminder);

            NotificationCompat.Builder n = new NotificationCompat.Builder(context, "REMINDER");
                    n.setContentTitle("MishnaPals")
                    .setContentText("masechta")
                    .setSmallIcon(R.drawable.images)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.images))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true).build();
            notificationManager.notify();
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Notification n = new Notification.Builder(context)
                    .setContentTitle("MishnaPals")
                    .setContentText("masechta")
                    .setSmallIcon(R.drawable.images)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.images))
                    .setColor(Color.GREEN)
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setAutoCancel(true).build();
            notificationManager.notify(0, n);
        }
        else {
            Notification n = new Notification.Builder(context)
                    .setContentTitle("MishnaPals")
                    .setContentText("masechta")
                    .setSmallIcon(R.drawable.images)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.images))
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setAutoCancel(true).build();
            notificationManager.notify(0, n);
        }
    }
}
