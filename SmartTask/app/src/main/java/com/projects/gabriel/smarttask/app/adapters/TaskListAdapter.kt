package com.projects.gabriel.smarttask.app.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.projects.gabriel.smarttask.R
import com.projects.gabriel.smarttask.domain.valueObjects.TaskList


class TaskListAdapter(context: Context,
                      private var listOfTaskList: ArrayList<TaskList>): ArrayAdapter<TaskList>(context, R.layout.spinner_row) {
    private var mContext : Context = context

    override fun getItem(position: Int): TaskList {
        return listOfTaskList[position]
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val taskList = getItem(position)

        val textView = LayoutInflater.from(mContext).inflate(R.layout.spinner_row, parent, false) as TextView

        textView.text = taskList.title

        return textView
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val taskList = getItem(position)

        val textView = LayoutInflater.from(mContext).inflate(R.layout.spinner_row, parent, false) as TextView

        textView.text = taskList.title

        return textView
    }

    override fun getCount(): Int {
        return listOfTaskList.size
    }
}