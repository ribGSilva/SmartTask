package com.projects.gabriel.smarttask.app.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.projects.gabriel.smarttask.domain.services.NotificationService;
import com.projects.gabriel.smarttask.domain.valueObjects.CreateNotificationSchedule;

public class NotificationPublisher extends BroadcastReceiver {

    public static final String NOTIFICATION_ID = "NotificationID";
    public static final String NOTIFICATION = "Notification";

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle params = intent.getExtras();
        if (params == null)
            return;

        Integer id = params.getInt(NOTIFICATION_ID);

        NotificationService notificationService = NotificationService.getInstance();

        CreateNotificationSchedule saved =
                notificationService.getNotificationScheduled(context, id);

        if (saved == null)
            return;

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {

                CreateNotificationSchedule novo =
                        new CreateNotificationSchedule.Builder(context)
                        .title(saved.getTitleOfNotification()).body(saved.getBodyOfNotification()).dateTime(saved.getDateTime())
                        .typeOfNotification(saved.getTypeOfNotification()).build();

                notificationService.scheduleNotification(novo);

                return;
            }
        }

        notificationService.cancelNotification(id);

        NotificationService.showNotification(
                context,
                id,
                saved.getTitleOfNotification(),
                saved.getBodyOfNotification(),
                saved.getTypeOfNotification()
        );

    }
}