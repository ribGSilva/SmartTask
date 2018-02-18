package com.projects.gabriel.smarttask.domain.services

import android.content.Context
import com.projects.gabriel.smarttask.R
import com.projects.gabriel.smarttask.domain.entities.Task
import com.projects.gabriel.smarttask.domain.factories.TaskListFactory
import com.projects.gabriel.smarttask.domain.repositories.TaskRepository
import com.projects.gabriel.smarttask.domain.valueObjects.TaskList
import java.util.*

class TaskService private constructor(context: Context) {

    private val repository: TaskRepository = TaskRepository.getInstance(context)

    init {
        TaskRepository.PERSONAL_TASK_LIST = TaskListFactory.createTaskList(context.getString(R.string.personal_task_list))
        repository.addTaskTable(TaskRepository.PERSONAL_TASK_LIST!!)
    }

    fun createList(listList: TaskList) {
        repository.addTaskTable(listList)
    }

    fun addTaskToList(task: Task, taskList: TaskList): Task? {
        return repository.addTaskToTable(task, taskList.tableName)
    }

    fun getTasksFrom(taskList: TaskList): TreeSet<Task> {
        return repository.getTasksFromTable(taskList.tableName)
    }

    fun getTaskLists(): ArrayList<TaskList> {
        return repository.getListOfTaskTables()
    }

    fun editTaskFromList(task: Task, tableName: String): Task? {
        return repository.editTaskFromTable(task, tableName)
    }

    fun deleteTaskFromList(id: Int, taskList: TaskList): Task? {
        return repository.removeTaskFromTableWithId(id, taskList.tableName)
    }

    fun deleteListFromList(tableListName: TaskList): Set<Task>? {
        return repository.removeTaskTable(tableListName)
    }

    companion object {
        private var ourInstance: TaskService? = null

        @Synchronized
        fun getInstance(context: Context): TaskService {
            if (ourInstance == null)
                ourInstance = TaskService(context)
            return ourInstance!!
        }
    }
}
