package com.projects.gabriel.smarttask.app.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.projects.gabriel.smarttask.R
import com.projects.gabriel.smarttask.app.adapters.TaskRecyclerViewAdapter
import com.projects.gabriel.smarttask.app.interfacies.SmartTaskManager
import com.projects.gabriel.smarttask.app.interfacies.TaskRemoverListener
import com.projects.gabriel.smarttask.domain.entities.Task
import com.projects.gabriel.smarttask.domain.services.TaskService
import com.projects.gabriel.smarttask.domain.valueObjects.TaskList
import java.util.*

class TaskListFragment : Fragment() {

    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var tasksAdapter: TaskRecyclerViewAdapter
    private lateinit var smartTaskManager: SmartTaskManager
    private lateinit var taskService: TaskService
    private var currentTaskList: TaskList? = null
    private var tasksSet: TreeSet<Task>? = null

    companion object {
        fun newInstance(taskList: TaskList): TaskListFragment {
            val taskFragment = TaskListFragment()
            taskFragment.currentTaskList = taskList
            return taskFragment
        }
    }

    override fun onAttach(activity: Context?) {
        super.onAttach(activity)

        smartTaskManager = activity as SmartTaskManager
        taskService = smartTaskManager.getTaskService()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_task_list, container, false)

        tasksRecyclerView = view.findViewById(R.id.tasks_recycler_view)
        tasksRecyclerView.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        tasksSet = taskService.getTasksFrom(currentTaskList!!)

        tasksRecyclerView.layoutManager = layoutManager
        tasksAdapter = TaskRecyclerViewAdapter.newInstance(context, tasksSet!!.toList(), object : TaskRemoverListener  {
            override fun removeTaskMethod(taskToRemove: Task) {
                taskService.deleteTaskFromList(taskToRemove.id, currentTaskList!!)
            }
        })
        tasksRecyclerView.adapter = tasksAdapter

        return view
    }

    override fun onResume() {
        super.onResume()

        updateRecyclerViewItems()
    }

    private fun updateRecyclerViewItems(){
        tasksSet = taskService.getTasksFrom(currentTaskList!!)
        tasksAdapter.listOfTask = tasksSet!!.toList()
        tasksAdapter.notifyDataSetChanged()
    }
}
