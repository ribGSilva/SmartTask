package com.projects.gabriel.smarttask.app.adapters

import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.projects.gabriel.smarttask.R
import com.projects.gabriel.smarttask.app.interfacies.TaskRemoverListener
import com.projects.gabriel.smarttask.domain.entities.Task
import com.projects.gabriel.smarttask.domain.types.PriorityLevel

class TaskRecyclerViewAdapter private constructor(
        private var context: Context,
        var listOfTask: List<Task>,
        private val removerListener: TaskRemoverListener
) : RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskViewHolder>() {

    @Suppress("deprecation")
    override fun onBindViewHolder(viewHolder: TaskViewHolder, position: Int) {

        val task = listOfTask[position]

        viewHolder.title?.text = task.title
        viewHolder.description?.text = task.description
        viewHolder.date?.text = task.finalDate.getFormatedDate()
        viewHolder.time?.text = task.finalDate.getFormatedTime()

        when (task.priority) {
            PriorityLevel.LOW -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    viewHolder.layoutTask?.background =
                            context.getDrawable(R.drawable.low_priority_item_expanded)
                    viewHolder.layoutCore?.background =
                            context.getDrawable(R.drawable.low_priority_item_core)
                } else {
                    viewHolder.layoutTask?.setBackgroundDrawable(
                            context.resources.getDrawable(R.drawable.low_priority_item_expanded)
                    )
                    viewHolder.layoutCore?.setBackgroundDrawable(
                            context.resources.getDrawable(R.drawable.low_priority_item_core)
                    )
                }
            }
            PriorityLevel.MEDIUM_LOW -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    viewHolder.layoutTask?.background =
                            context.getDrawable(R.drawable.low_medium_priority_item_expanded)
                    viewHolder.layoutCore?.background =
                            context.getDrawable(R.drawable.low_medium_priority_item_core)
                } else {
                    viewHolder.layoutTask?.setBackgroundDrawable(
                            context.resources.getDrawable(R.drawable.low_medium_priority_item_expanded)
                    )
                    viewHolder.layoutCore?.setBackgroundDrawable(
                            context.resources.getDrawable(R.drawable.low_medium_priority_item_core)
                    )
                }
            }
            PriorityLevel.MEDIUM -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    viewHolder.layoutTask?.background =
                            context.getDrawable(R.drawable.medium_priority_item_expanded)
                    viewHolder.layoutCore?.background =
                            context.getDrawable(R.drawable.medium_priority_item_core)
                } else {
                    viewHolder.layoutTask?.setBackgroundDrawable(
                            context.resources.getDrawable(R.drawable.medium_priority_item_expanded)
                    )
                    viewHolder.layoutCore?.setBackgroundDrawable(
                            context.resources.getDrawable(R.drawable.medium_priority_item_core)
                    )
                }
            }
            PriorityLevel.MEDIUM_HIGH -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    viewHolder.layoutTask?.background =
                            context.getDrawable(R.drawable.high_medium_priority_item_expanded)
                    viewHolder.layoutCore?.background =
                            context.getDrawable(R.drawable.high_medium_priority_item_core)
                } else {
                    viewHolder.layoutTask?.setBackgroundDrawable(
                            context.resources.getDrawable(R.drawable.high_medium_priority_item_expanded)
                    )
                    viewHolder.layoutCore?.setBackgroundDrawable(
                            context.resources.getDrawable(R.drawable.high_medium_priority_item_core)
                    )
                }
            }
            PriorityLevel.HIGH -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    viewHolder.layoutTask?.background =
                            context.getDrawable(R.drawable.high_priority_item_expanded)
                    viewHolder.layoutCore?.background =
                            context.getDrawable(R.drawable.high_priority_item_core)
                } else {
                    viewHolder.layoutTask?.setBackgroundDrawable(
                            context.resources.getDrawable(R.drawable.high_priority_item_expanded)
                    )
                    viewHolder.layoutCore?.setBackgroundDrawable(
                            context.resources.getDrawable(R.drawable.high_priority_item_core)
                    )
                }
            }
        }

        viewHolder.layoutTask?.setOnClickListener { view ->
            val layout = view.findViewById<RelativeLayout>(R.id.expanded_task_info)
            if (layout.visibility == GONE)
                layout.visibility = VISIBLE
            else
                layout.visibility = GONE
        }

        /*viewHolder.editView?.setOnClickListener { view ->
            //taskManager.changeFragment()
        }*/

        viewHolder.removeView?.tag = task
        viewHolder.removeView?.setOnClickListener { view ->
            val taskToRemove = (view.tag as TaskViewHolder).task!!
            removerListener.removeTaskMethod(taskToRemove)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.task_view_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfTask.size
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var task: Task? = null
        internal var layoutTask: LinearLayout? = null
        internal var title: TextView? = null
        internal var description: TextView? = null
        internal var time: TextView? = null
        internal var date: TextView? = null
        internal var layoutCore: RelativeLayout? = null
        internal var editView: ImageView? = null
        internal var removeView: ImageView? = null

        init {
            layoutTask = itemView.findViewById(R.id.linear_layout_task)
            title = itemView.findViewById(R.id.task_title)
            date = itemView.findViewById(R.id.final_date_task)
            time = itemView.findViewById(R.id.final_time_task)
            description = itemView.findViewById(R.id.task_description)
            layoutCore = itemView.findViewById(R.id.core_layout)
            editView = itemView.findViewById(R.id.edit_task)
            removeView = itemView.findViewById(R.id.delete_task)
        }
    }

    companion object {
        fun newInstance(context: Context, tasksSet: List<Task>, taskRemoverListener: TaskRemoverListener):
                TaskRecyclerViewAdapter {
            return TaskRecyclerViewAdapter(context, tasksSet, taskRemoverListener)
        }
    }
}
