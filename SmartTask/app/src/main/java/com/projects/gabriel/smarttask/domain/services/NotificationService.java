package com.projects.gabriel.smarttask.domain.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.projects.gabriel.smarttask.R;
import com.projects.gabriel.smarttask.app.activities.MainActivity;
import com.projects.gabriel.smarttask.app.broadcastReceiver.NotificationPublisher;
import com.projects.gabriel.smarttask.domain.repositories.NotificationRepository;
import com.projects.gabriel.smarttask.domain.valueObjects.DateTime;
import com.projects.gabriel.smarttask.domain.types.TypesOfNotification;
import com.projects.gabriel.smarttask.domain.valueObjects.CreateNotificationSchedule;

import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class NotificationService {
    private NotificationRepository repository;
    private static final NotificationService ourInstance = new NotificationService();

    public static synchronized NotificationService getInstance() {
        return ourInstance;
    }

    private NotificationService() {
        repository = NotificationRepository.getInstance();
    }

    public Notification scheduleNotification(CreateNotificationSchedule createNotificationSchedule) {

        repository.initialize(createNotificationSchedule.getContext());

        long futureTime = calculateDelay(createNotificationSchedule.getDateTime());

        Notification notification = scheduleNotificationAtSystem(
                createNotificationSchedule.getContext(),
                futureTime,
                createNotificationSchedule.getTitleOfNotification(),
                createNotificationSchedule.getBodyOfNotification(),
                createNotificationSchedule.getTypeOfNotification());


        repository.addNotification(notification.hashCode(), createNotificationSchedule);

        return notification;
    }

    public Notification editNotification(Integer idNotification,
                                         CreateNotificationSchedule createNotificationSchedule) {
        Notification notification = null;
        if (repository.removeNotification(idNotification)){
            notification = scheduleNotification(createNotificationSchedule);
        }
        return notification;
    }

    public CreateNotificationSchedule getNotificationScheduled(Context context, Integer idNotification) {
        repository.initialize(context);

        return repository.getNotificationScheduleVO(idNotification);
    }

    public void cancelNotification(Integer idNotification) {
        repository.removeNotification(idNotification);
    }

    private long calculateDelay(DateTime dateTime) {
        Calendar notificationDate = Calendar.getInstance();
        notificationDate.set(
                dateTime.getYear(),
                dateTime.getMonth(),
                dateTime.getDay(),
                dateTime.getHour(),
                dateTime.getMinute()
        );

        Calendar todayDate = Calendar.getInstance();

        return notificationDate.getTimeInMillis();// - todayDate.getTimeInMillis();
    }

    private Notification scheduleNotificationAtSystem(Context context, long futureTime,
            String title, String body, TypesOfNotification type) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, title)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        switch (type) {
            case COMMON_NOTIFICATION:
                builder.setSmallIcon(R.mipmap.ic_launcher);
                break;
        }

        ComponentName receiver = new ComponentName(context, NotificationPublisher.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Notification notification = builder.build();

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent activity = PendingIntent.getActivity(context, notification.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(activity);

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notification.hashCode());
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notification.hashCode(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureTime, pendingIntent);

        Log.i(TAG, "tempo em milissegundos para soar o alarme "+futureTime);

        return notification;
    }


    public static void showNotification(Context context, Integer id, String title,
                                        String body,TypesOfNotification type)
    {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, title)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setSound(alarmSound)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        switch (type) {
            case COMMON_NOTIFICATION:
                builder.setSmallIcon(R.mipmap.ic_launcher);
                break;
        }

        Notification notification = builder.build();

        if (notification == null){
            Log.e(TAG, "Fail to generate notification");
            return;
        }


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager == null){
            Log.e(TAG, "Fail to get notification manager");
            return;
        }
        notificationManager.notify(id, notification);

    }
}
