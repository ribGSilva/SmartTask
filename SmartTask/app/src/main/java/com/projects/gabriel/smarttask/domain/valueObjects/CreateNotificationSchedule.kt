package com.projects.gabriel.smarttask.domain.valueObjects

import android.content.Context
import com.projects.gabriel.smarttask.exceptions.BlankStringException
import com.projects.gabriel.smarttask.domain.types.TypesOfNotification


class CreateNotificationSchedule private constructor(
        val context: Context,
        val titleOfNotification: String,
        val bodyOfNotification: String,
        val dateTime: DateTime,
        val typeOfNotification: TypesOfNotification) {

    class Builder(private val context: Context) {
        private var title: String? = null
        private var body: String? = null
        private var dateTime: DateTime? = null
        private var typeOfNotification = TypesOfNotification.COMMON_NOTIFICATION

        fun title(title: String): Builder {
            if (title.isBlank())
                throw BlankStringException("Blank parameter")
            else
                this.title = title
            return this
        }

        fun body(body: String): Builder {
            if (body.isBlank())
                throw BlankStringException("Blank parameter")
            else
                this.body = body
            return this
        }

        fun dateTime(dateTime: DateTime): Builder{
            this.dateTime = dateTime
            return this
        }

        fun typeOfNotification(typeOfNotification: TypesOfNotification): Builder {
            this.typeOfNotification = typeOfNotification
            return this
        }

        fun build(): CreateNotificationSchedule? {
            if (dateTime == null){
                return null
            }

            val titleOfNotification = if (this.title.isNullOrBlank())
                "Task alert"
            else
                this.title!!

            val bodyOdNotification = if (this.body.isNullOrBlank())
                "You have some task to complete"
            else
                this.body!!

            return CreateNotificationSchedule(
                    context, titleOfNotification,
                    bodyOdNotification, dateTime!!, this.typeOfNotification)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CreateNotificationSchedule

        if (titleOfNotification != other.titleOfNotification) return false
        if (bodyOfNotification != other.bodyOfNotification) return false
        if (dateTime != other.dateTime) return false
        if (typeOfNotification != other.typeOfNotification) return false

        return true
    }

    override fun hashCode(): Int {
        var result = titleOfNotification.hashCode()
        result = 31 * result + bodyOfNotification.hashCode()
        result = 31 * result + dateTime.hashCode()
        result = 31 * result + typeOfNotification.hashCode()
        return result
    }

    override fun toString(): String {
        return "CreateNotificationSchedule(context=$context, titleOfNotification='$titleOfNotification', bodyOfNotification='$bodyOfNotification', dateTime=$dateTime, typeOfNotification=$typeOfNotification)"
    }

}
