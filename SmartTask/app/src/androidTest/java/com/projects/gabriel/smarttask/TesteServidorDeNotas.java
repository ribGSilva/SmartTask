package com.projects.gabriel.smarttask;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.projects.gabriel.smarttask.domain.entities.Task;
import com.projects.gabriel.smarttask.domain.factories.DateTimeFactory;
import com.projects.gabriel.smarttask.domain.factories.TaskFactory;
import com.projects.gabriel.smarttask.domain.services.TaskService;
import com.projects.gabriel.smarttask.domain.types.PriorityLevel;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;

/**
 * Created by gabriel on 31/12/17.
 */
@RunWith(AndroidJUnit4.class)
public class TesteServidorDeNotas {
    Context context = InstrumentationRegistry.getTargetContext();
    @Test
    public void testeServicoDeNotas(){
        String tableName = "Pessoal";

        TaskService servicoDeNotas = TaskService.Companion.getInstance(context);

        Task taskVO = TaskFactory.INSTANCE.newInstance("oie", "teste", PriorityLevel.LOW,
                DateTimeFactory.INSTANCE.newInstance("16:00", "31/12/2017"), 15);

        servicoDeNotas.createList(tableName);

        Task task = servicoDeNotas.addTaskToList(tableName, taskVO);
        servicoDeNotas.addTaskToList(tableName, taskVO);
        servicoDeNotas.addTaskToList(tableName, taskVO);
        servicoDeNotas.addTaskToList(tableName, taskVO);
        servicoDeNotas.addTaskToList(tableName, taskVO);

        Set<Task> tasks = servicoDeNotas.getTasksFrom(tableName);

        for (Task n : tasks){
            System.out.println("+++++++++++++++++++++++++++++++"+n.toString());
        }

        servicoDeNotas.deleteTaskFromList(2, tableName);
        servicoDeNotas.deleteTaskFromList(3, tableName);
        servicoDeNotas.deleteTaskFromList(4, tableName);

        task.setTitle("asdfasdfasdfadfasdfasf");

        servicoDeNotas.editTaskFromList(task, tableName);

        tasks = servicoDeNotas.getTasksFrom(tableName);

        for (Task n : tasks){
            System.out.println("-------------------------------"+n.toString());
        }

        servicoDeNotas.deleteListFromList(tableName);
    }
}
