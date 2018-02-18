package com.projects.gabriel.smarttask.app.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.projects.gabriel.smarttask.R
import com.projects.gabriel.smarttask.app.types.AlarmTimers

class AlarmTimesViewAdapter(context: Context) : ArrayAdapter<AlarmTimers>(context, R.layout.spinner_row) {
    private var mContext : Context = context
    private var alarmList = ArrayList<AlarmTimers>()

    init {
        AlarmTimers.values().toCollection(this.alarmList)
    }

    override fun getItem(position: Int): AlarmTimers {
        return alarmList[position]
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val alarmTimers = getItem(position)

        val textView = LayoutInflater.from(mContext).inflate(R.layout.spinner_row, parent, false) as TextView

        textView.text = mContext.getText(alarmTimers.text)

        return textView
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val alarmTimers = getItem(position)

        val textView = LayoutInflater.from(mContext).inflate(R.layout.spinner_row, parent, false) as TextView

        textView.text = mContext.getText(alarmTimers.text)

        return textView
    }

    override fun getCount(): Int {
        return alarmList.size
    }
}