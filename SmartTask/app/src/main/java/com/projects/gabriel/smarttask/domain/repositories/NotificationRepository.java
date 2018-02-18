package com.projects.gabriel.smarttask.domain.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.projects.gabriel.smarttask.domain.factories.DateTimeFactory;
import com.projects.gabriel.smarttask.domain.types.TypesOfNotification;
import com.projects.gabriel.smarttask.domain.valueObjects.CreateNotificationSchedule;


public class NotificationRepository {
    private static final String NOTIFICATION_LIST = "Notification";
    private static final String NOTIFICATION_TITLE = "NotificationTitle";
    private static final String NOTIFICATION_BODY = "NotificationBody";
    private static final String NOTIFICATION_DATE = "NotificationDate";
    private static final String NOTIFICATION_TIME = "NotificationTime";
    private static final String NOTIFICATION_TYPE = "NotificationType";
    private SharedPreferences repository;
    private Context context;

    public static synchronized NotificationRepository getInstance() {
        return new NotificationRepository();
    }

    private NotificationRepository() {
    }

    public void initialize(Context context) {
        this.context = context;
        repository = context.getSharedPreferences(NOTIFICATION_LIST, Context.MODE_PRIVATE);
    }

    public void addNotification(Integer idNotification,
                                CreateNotificationSchedule createNotificationSchedule) {

        String key;
        SharedPreferences.Editor editor = repository.edit();

        editor.putBoolean(idNotification.toString(), true);

        key = NOTIFICATION_TITLE + idNotification.toString();
        editor.putString(key, createNotificationSchedule.getTitleOfNotification());

        key = NOTIFICATION_BODY + idNotification.toString();
        editor.putString(key, createNotificationSchedule.getBodyOfNotification());

        key = NOTIFICATION_TIME + idNotification.toString();
        editor.putString(key,
                createNotificationSchedule.getDateTime().getHour() +
                        ":" + createNotificationSchedule.getDateTime().getMinute());

        key = NOTIFICATION_DATE + idNotification.toString();
        editor.putString(key,
                createNotificationSchedule.getDateTime().getDay() +
                        "/" + createNotificationSchedule.getDateTime().getMonth() +
                        "/" + createNotificationSchedule.getDateTime().getYear());

        key = NOTIFICATION_TYPE + idNotification.toString();
        editor.putInt(key, createNotificationSchedule.getTypeOfNotification().ordinal());

        editor.apply();
    }

    private boolean haveThisNotification(Integer idNotification) {
        return repository.contains(idNotification.toString());
    }

    public CreateNotificationSchedule getNotificationScheduleVO(Integer idNotificaton){
        if (!haveThisNotification(idNotificaton)){
            return null;
        }

        String key;
        CreateNotificationSchedule.Builder builder = new CreateNotificationSchedule.Builder(context);

        key = NOTIFICATION_TITLE + idNotificaton.toString();
        builder.title(repository.getString(key, ""));

        key = NOTIFICATION_BODY + idNotificaton.toString();
        builder.body(repository.getString(key, ""));

        builder.dateTime(
                DateTimeFactory.INSTANCE.newInstance(
                        repository.getString(NOTIFICATION_TIME + idNotificaton.toString(), ""),
                        repository.getString(NOTIFICATION_DATE + idNotificaton.toString(), "")
                )
        );

        builder.typeOfNotification(
                TypesOfNotification.Companion.getTypeByOrdinary(
                        repository.getInt(NOTIFICATION_TYPE + idNotificaton.toString(), 0)
                )
        );

        return builder.build();
    }

    public boolean removeNotification(Integer idNotification) {

        String key;
        SharedPreferences.Editor editor = repository.edit();

        editor.putBoolean(idNotification.toString(), true);

        key = NOTIFICATION_TITLE + idNotification.toString();
        editor.remove(key);

        key = NOTIFICATION_BODY + idNotification.toString();
        editor.remove(key);

        key = NOTIFICATION_TIME + idNotification.toString();
        editor.remove(key);

        key = NOTIFICATION_DATE + idNotification.toString();
        editor.remove(key);

        key = NOTIFICATION_TYPE + idNotification.toString();
        editor.remove(key);

        editor.remove(idNotification.toString());

        editor.apply();
        return true;
    }
}
