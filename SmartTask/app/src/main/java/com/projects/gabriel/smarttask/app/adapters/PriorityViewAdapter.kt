package com.projects.gabriel.smarttask.app.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.projects.gabriel.smarttask.R
import com.projects.gabriel.smarttask.domain.types.PriorityLevel

class PriorityViewAdapter(context: Context) : ArrayAdapter<PriorityLevel>(context, R.layout.spinner_row) {
    private var mContext : Context = context
    private var priorityList = ArrayList<PriorityLevel>()

    init {
        PriorityLevel.values().toCollection(this.priorityList)
    }

    override fun getItem(position: Int): PriorityLevel {
        return priorityList[position]
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val priorityLevel = getItem(position)

        val textView = LayoutInflater.from(mContext).inflate(R.layout.spinner_row, parent, false) as TextView

        textView.text = mContext.getText(priorityLevel.text)

        return textView
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val priorityLevel = getItem(position)

        val textView = LayoutInflater.from(mContext).inflate(R.layout.spinner_row, parent, false) as TextView

        textView.text = mContext.getText(priorityLevel.text)

        return textView
    }

    override fun getCount(): Int {
        return priorityList.size
    }
}