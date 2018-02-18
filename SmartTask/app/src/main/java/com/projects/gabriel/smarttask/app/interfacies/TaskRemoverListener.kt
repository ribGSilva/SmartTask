package com.projects.gabriel.smarttask.app.interfacies

import com.projects.gabriel.smarttask.domain.entities.Task

interface TaskRemoverListener {
    fun removeTaskMethod(taskToRemove: Task)
}