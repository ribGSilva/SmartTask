package com.projects.gabriel.smarttask.domain.valueObjects

/**
 * Created by gabriel on 30/12/17.
 */
class DateTime : Comparable<DateTime> {
    val hour: Int
    val minute: Int
    val day: Int
    val month: Int
    val year: Int

    constructor(hour: Int, minute: Int, day: Int, month: Int, year: Int) {
        this.hour = hour
        this.minute = minute
        this.day = day
        this.month = month
        this.year = year
    }

    fun getFormatedTime(): String {
        if (minute > 9)
            return "$hour:$minute"
        else
            return "$hour:0$minute"
    }

    fun getFormatedDate(): String {
        return "$day/$month/$year"
    }

    override fun compareTo(other: DateTime): Int {
        var compareValue = year.compareTo(other.year)
        if (compareValue != 0)
            return compareValue
        compareValue = month.compareTo(other.month)
        if (compareValue != 0)
            return compareValue
        compareValue = day.compareTo(other.day)
        if (compareValue != 0)
            return compareValue
        compareValue = hour.compareTo(other.hour)
        if (compareValue != 0)
            return compareValue
        return minute.compareTo(other.minute)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DateTime

        if (hour != other.hour) return false
        if (minute != other.minute) return false
        if (day != other.day) return false
        if (month != other.month) return false
        if (year != other.year) return false

        return true
    }

    override fun hashCode(): Int {
        var result = hour
        result = 31 * result + minute
        result = 31 * result + day
        result = 31 * result + month
        result = 31 * result + year
        return result
    }

    override fun toString(): String {
        return "DateTime(hour=$hour, minute=$minute, day=$day, month=$month, year=$year)"
    }


}
