package com.projects.gabriel.smarttask.app.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.projects.gabriel.smarttask.R
import com.projects.gabriel.smarttask.app.adapters.AlarmTimesViewAdapter
import com.projects.gabriel.smarttask.app.adapters.PriorityViewAdapter
import com.projects.gabriel.smarttask.app.adapters.TaskListAdapter
import com.projects.gabriel.smarttask.app.interfacies.SmartTaskManager
import com.projects.gabriel.smarttask.domain.factories.DateTimeFactory
import com.projects.gabriel.smarttask.domain.factories.TaskFactory
import com.projects.gabriel.smarttask.domain.services.TaskService
import com.projects.gabriel.smarttask.exceptions.BlankStringException
import com.projects.gabriel.smarttask.exceptions.FinalDateException
import com.projects.gabriel.smarttask.exceptions.TimeInvalidException
import java.text.SimpleDateFormat
import java.util.*

class NewTaskFragment : Fragment() {

    private lateinit var taskManager: SmartTaskManager
    private lateinit var taskService: TaskService

    companion object {
        fun newInstance(): NewTaskFragment {
            return NewTaskFragment()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        taskManager = context as SmartTaskManager
        taskService = taskManager.getTaskService()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_new_task, container, false)

        val taskTitleTextView = view.findViewById<EditText>(R.id.new_task_title)
        val taskDescriptionTextView = view.findViewById<EditText>(R.id.new_task_description)

        val taskTimeTextView = view.findViewById<TextView>(R.id.new_task_time)
        val taskDateTextView = view.findViewById<TextView>(R.id.new_task_date)

        val prioritySpinner = view.findViewById<Spinner>(R.id.new_task_priority)
        val taskListSpinner = view.findViewById<Spinner>(R.id.list_of_new_task)
        val alarmTaskSpinner = view.findViewById<Spinner>(R.id.new_task_alarm)

        val cancelCreationOfNewTaskButton = view.findViewById<Button>(R.id.cancel_creation_of_new_task)
        val createNewTaskButton = view.findViewById<Button>(R.id.create_new_task)

        val currentDateTimeToBindView = Date()
        taskDateTextView.text = SimpleDateFormat.getDateInstance().format(currentDateTimeToBindView)
        taskTimeTextView.text = SimpleDateFormat.getTimeInstance().format(currentDateTimeToBindView)

        taskTimeTextView.setOnClickListener {
            val currentTime = Calendar.getInstance()
            val hour = currentTime.get(Calendar.HOUR_OF_DAY)
            val minute = currentTime.get(Calendar.MINUTE)
            val timePicker: TimePickerDialog
            timePicker = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                taskTimeTextView.text = "$selectedHour:$selectedMinute"
            }
                    , hour, minute, true)
            timePicker.setTitle(getString(R.string.select_time))
            timePicker.show()
        }

        taskDateTextView.setOnClickListener {
            val currentTime = Calendar.getInstance()
            val day = currentTime.get(Calendar.DAY_OF_MONTH)
            val month = currentTime.get(Calendar.MONTH)
            val year = currentTime.get(Calendar.YEAR)
            val datePicker: DatePickerDialog
            datePicker = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->

                taskDateTextView.text = "$selectedDay/$selectedMonth/$selectedYear"

            }, year, month, day)
            datePicker.setTitle(getString(R.string.select_date))
            datePicker.show()
        }

        val priorityAdapter = PriorityViewAdapter(activity.baseContext)
        prioritySpinner.adapter = priorityAdapter

        val listOfTaskAdapter = TaskListAdapter(
                activity.baseContext, taskManager.getTaskService().getTaskLists()
        )
        taskListSpinner.adapter = listOfTaskAdapter

        val alarmAdapter = AlarmTimesViewAdapter(activity.baseContext)
        alarmTaskSpinner.adapter = alarmAdapter

        cancelCreationOfNewTaskButton.setOnClickListener { activity.onBackPressed() }
        createNewTaskButton.setOnClickListener {
            try {
                val finalDate = DateTimeFactory.newInstance(
                        taskTimeTextView.text.toString(),
                        taskDateTextView.text.toString()
                )

                val newTask = TaskFactory.newInstance(
                        taskTitleTextView.text.toString(),
                        taskDescriptionTextView.text.toString(),
                        priorityAdapter.getItem(prioritySpinner.selectedItemPosition),
                        finalDate,
                        alarmAdapter.getItem(alarmTaskSpinner.selectedItemPosition)
                                .timeInMinutes * 60 * 1000
                )

                val taskReturned = taskService.addTaskToList(
                        newTask,
                        listOfTaskAdapter.getItem(taskListSpinner.selectedItemPosition)
                )

                if (taskReturned != null)
                    Toast.makeText(context, R.string.alert_new_task_created, Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(context, R.string.alert_new_task_problem_creation, Toast.LENGTH_SHORT).show()

                activity.onBackPressed()

            } catch (e: BlankStringException) {
                e.printStackTrace()
                Toast.makeText(
                        activity,
                        getString(R.string.all_fields_must_be_completed),
                        Toast.LENGTH_LONG
                ).show()
            } catch (e: TimeInvalidException) {
                e.printStackTrace()
                Toast.makeText(
                        activity,
                        getString(R.string.invalid_alarm_time),
                        Toast.LENGTH_LONG
                ).show()
            } catch (e: FinalDateException) {
                e.printStackTrace()
                Toast.makeText(
                        activity,
                        getString(R.string.due_time_invalid),
                        Toast.LENGTH_LONG
                ).show()
            }
        }
        return view
    }
}
