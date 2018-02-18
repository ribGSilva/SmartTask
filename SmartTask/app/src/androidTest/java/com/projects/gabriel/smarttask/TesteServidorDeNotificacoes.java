package com.projects.gabriel.smarttask;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.projects.gabriel.smarttask.domain.factories.DateTimeFactory;
import com.projects.gabriel.smarttask.domain.services.NotificationService;
import com.projects.gabriel.smarttask.domain.valueObjects.CreateNotificationSchedule;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by gabriel on 30/12/17.
 */

@RunWith(AndroidJUnit4.class)
public class TesteServidorDeNotificacoes {
    Context appContext = InstrumentationRegistry.getTargetContext();

    @Test
    public void teste(){
        NotificationService service = NotificationService.getInstance();

        CreateNotificationSchedule a =
                new CreateNotificationSchedule.Builder(appContext.getApplicationContext())
                        .body("asdfasdf").body("werqwe").dateTime(
                        DateTimeFactory.INSTANCE.newInstance("15:15", "31/12/2017")
                ).build();

        int id = service.scheduleNotification(a).hashCode();

        CreateNotificationSchedule b = service.getNotificationScheduled(appContext, id);

        assert a.equals(b);

        a = new CreateNotificationSchedule.Builder(appContext.getApplicationContext())
                        .body("asdfasdf").body("werqwe").dateTime(
                        DateTimeFactory.INSTANCE.newInstance("15:15", "31/12/2017")
                ).build();

        id = service.editNotification(id, a).hashCode();

        b = service.getNotificationScheduled(appContext, id);

        assert a.equals(b);

        service.cancelNotification(id);

        b = service.getNotificationScheduled(appContext, id);

        assert b == null;
    }
}
