package com.example.mna.mishnapals;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

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
        Notification n = new Notification.Builder(context).setContentTitle("MishnaPals").setContentText("masechta").setSmallIcon(R.mipmap.ic_launcher_round).setContentIntent(pendingIntent).build();
        notificationManager.notify(0, n);

    }
}
