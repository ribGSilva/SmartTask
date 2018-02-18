package com.projects.gabriel.smarttask.domain.factories

import com.projects.gabriel.smarttask.domain.valueObjects.TaskList
import com.projects.gabriel.smarttask.exceptions.BlankStringException

object TaskListFactory {
    fun createTaskList(title: String, tableName: String): TaskList {
        if (title.isBlank())
            throw BlankStringException("Title of task list is blank")
        return TaskList(title, tableName)
    }

    fun createTaskList(title: String): TaskList {
        if (title.isBlank())
            throw BlankStringException("Title of task list is blank")
        val tableName = title.replace(" ", "_", true)
        return TaskList(title, tableName)
    }
}