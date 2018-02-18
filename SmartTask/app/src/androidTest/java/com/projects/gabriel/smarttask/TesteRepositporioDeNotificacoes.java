package com.projects.gabriel.smarttask;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.projects.gabriel.smarttask.domain.factories.DateTimeFactory;
import com.projects.gabriel.smarttask.domain.repositories.NotificationRepository;
import com.projects.gabriel.smarttask.domain.valueObjects.CreateNotificationSchedule;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by gabriel on 30/12/17.
 */
@RunWith(AndroidJUnit4.class)
public class TesteRepositporioDeNotificacoes {
    Context appContext = InstrumentationRegistry.getTargetContext();
    @Test
    public void teste(){
        NotificationRepository repository = NotificationRepository.getInstance();
        repository.initialize(appContext.getApplicationContext());


        CreateNotificationSchedule a =
        new CreateNotificationSchedule.Builder(appContext.getApplicationContext())
                .body("asdfasdf").body("werqwe").dateTime(
                DateTimeFactory.INSTANCE.newInstance("20:50", "30/12/2017")
        ).build();

        repository.addNotification(7654, a);

        CreateNotificationSchedule b = repository.getNotificationScheduleVO(7654);

        assert a.equals(b);

        repository.removeNotification(7654);

        b = repository.getNotificationScheduleVO(7654);

        assert b == null;

    }
}
