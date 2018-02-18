package com.projects.gabriel.smarttask.domain.entities

import com.projects.gabriel.smarttask.domain.valueObjects.DateTime
import com.projects.gabriel.smarttask.domain.types.PriorityLevel

class Task(
        var id: Int,
        var title: String,
        var description: String,
        var priority: PriorityLevel,
        var finalDate: DateTime,
        var timeInMinutesBeforeAlarm: Int) : Comparable<Task> {

    override fun compareTo(other: Task): Int {
        var compareValue = finalDate.compareTo(other.finalDate)
        if (compareValue != 0)
            return compareValue
        compareValue = priority.ordinal.compareTo(other.priority.ordinal)
        if (compareValue != 0)
            return compareValue
        return title.compareTo(other.title)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        if (id != other.id) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (priority != other.priority) return false
        if (finalDate != other.finalDate) return false
        if (timeInMinutesBeforeAlarm != other.timeInMinutesBeforeAlarm) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + priority.hashCode()
        result = 31 * result + finalDate.hashCode()
        result = 31 * result + timeInMinutesBeforeAlarm
        return result
    }

    override fun toString(): String {
        return "Task(id=$id, titleOfNotification='$title', description='$description', priority=$priority, finalDate=$finalDate, timeInMinutesBeforeAlarm=$timeInMinutesBeforeAlarm)"
    }
}