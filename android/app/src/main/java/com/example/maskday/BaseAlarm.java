package com.example.maskday;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.maskday.Activity.MainActivity;

import static com.example.maskday.Activity.MainActivity.CHANNEL_ID;

public class BaseAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Alarm", "알람입니다.");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notiIntent = new Intent(context.getApplicationContext(), MainActivity.class);
        notiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, notiIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.applogo)
                .setContentTitle("올 때 마스크")
                .setContentText("잊지말고 마스크를 구매해주세요 :)")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder.setSmallIcon(R.drawable.applogo);
            CharSequence charSequence = "Notification Channel";
            String description = "It's for over Oreo";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, charSequence, importance);
            channel.setDescription(description);

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        } else builder.setSmallIcon(R.drawable.applogo);

        assert notificationManager != null;
        notificationManager.notify(1234, builder.build());
    }
}
