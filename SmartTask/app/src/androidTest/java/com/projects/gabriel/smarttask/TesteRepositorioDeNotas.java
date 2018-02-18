package com.projects.gabriel.smarttask;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.projects.gabriel.smarttask.domain.entities.Task;
import com.projects.gabriel.smarttask.domain.factories.DateTimeFactory;
import com.projects.gabriel.smarttask.domain.factories.TaskFactory;
import com.projects.gabriel.smarttask.domain.repositories.TaskRepository;
import com.projects.gabriel.smarttask.domain.types.PriorityLevel;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;

/**
 * Created by gabriel on 31/12/17.
 */
@RunWith(AndroidJUnit4.class)
public class TesteRepositorioDeNotas {
    Context context = InstrumentationRegistry.getTargetContext();

    @Test
    public void testeNotas() {
        String tableName = "Pessoal";

        TaskRepository taskRepository = TaskRepository.Companion.getInstance(context);

        Task taskVO = TaskFactory.INSTANCE.newInstance("oie", "teste", PriorityLevel.LOW,
                DateTimeFactory.INSTANCE.newInstance("16:00", "31/12/2017"), 15);

        taskRepository.addTaskTable(tableName);

        Task task = taskRepository.addTaskToTable(taskVO, tableName);
        taskRepository.addTaskToTable(taskVO, tableName);
        taskRepository.addTaskToTable(taskVO, tableName);
        taskRepository.addTaskToTable(taskVO, tableName);
        taskRepository.addTaskToTable(taskVO, tableName);

        listarNotas(taskRepository, tableName);

        taskRepository.removeTaskFromTableWithId(4, tableName);

        listarNotas(taskRepository, tableName);

        task.setTitle("asdfasdfasdfadfasdfasf");

        taskRepository.editTaskFromTable(task, tableName);

        listarNotas(taskRepository, tableName);

        taskRepository.removeTaskTable(tableName);
    }

    public void listarNotas(TaskRepository taskRepository, String tableName) {
        Set<Task> tasks = taskRepository.getTasksFromTable(tableName);

        for (Task n : tasks) {
            System.out.println("+++++++++++++++++++++++++++++++" + n.toString());
        }
    }
}
