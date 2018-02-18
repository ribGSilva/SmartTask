package com.projects.gabriel.smarttask.app.interfacies

import com.projects.gabriel.smarttask.app.fragments.enuns.TypeOfFragment
import com.projects.gabriel.smarttask.domain.services.TaskService
import com.projects.gabriel.smarttask.domain.valueObjects.TaskList

interface SmartTaskManager {
    fun changeFragment(newFragmentType: TypeOfFragment, addToBack: Boolean)

    fun getTaskService(): TaskService

    fun addNewTaskList(newTaskList: TaskList)
}