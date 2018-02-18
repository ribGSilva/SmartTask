package com.projects.gabriel.smarttask.domain.factories

import com.projects.gabriel.smarttask.domain.valueObjects.DateTime
import com.projects.gabriel.smarttask.exceptions.DateInvalidException
import com.projects.gabriel.smarttask.exceptions.TimeInvalidException
import java.util.regex.Pattern

object DateTimeFactory {
    private val TIME_PATTERN = "^([01]?[0-9]|2[0-3]):([0-5]?[0-9])$"
    private val DATE_PATTERN = "^(0?[1-9]|[12][0-9]|3[01])[/.-](0?[1-9]|1[012])[/.-]((19|20)\\d\\d$)"

    private var hour: Int = 0
    private var minute: Int = 0
    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0

    fun newInstance(time: String, date: String): DateTime {
        if (!isValidTime(time)) {
            throw TimeInvalidException("Invalid time")
        }
        if (!isValidDate(date)) {
            throw DateInvalidException("Invalid date")
        }

        formatTime(time)
        formatDate(date)

        return DateTime(hour, minute, day, month, year)
    }

    private fun formatTime(time: String) {
        val pattern = Pattern.compile(TIME_PATTERN)
        val matcher = pattern.matcher(time)

        if (matcher.find()) {
            hour = matcher.group(1).toInt()
            minute = matcher.group(2).toInt()
        }
    }

    private fun isValidTime(time: String): Boolean {
        return time.matches(Regex(TIME_PATTERN))
    }

    private fun formatDate(date: String) {
        val pattern = Pattern.compile(DATE_PATTERN)
        val matcher = pattern.matcher(date)

        if (matcher.find()) {
            day = matcher.group(1).toInt()
            month = matcher.group(2).toInt()
            year = matcher.group(3).toInt()
        }
    }

    private fun isValidDate(date: String): Boolean {
        val pattern = Pattern.compile(DATE_PATTERN)
        val matcher = pattern.matcher(date)

        if (matcher.matches()) {
            matcher.reset()

            if (matcher.find()) {
                val day = matcher.group(1)
                val month = matcher.group(2)
                val year = Integer.parseInt(matcher.group(3))

                return if (day == "31" && (month == "4" || month == "6" || month == "9" ||
                        month == "11" || month == "04" || month == "06" ||
                        month == "09")) {
                    false // only 1,3,5,7,8,10,12 has 31 days
                } else if (month == "2" || month == "02") {
                    //leap year
                    if (year % 4 == 0) {
                        !(day == "30" || day == "31")
                    } else {
                        !(day == "29" || day == "30" || day == "31")
                    }
                } else {
                    true
                }
            } else {
                return false
            }
        } else {
            return false
        }
    }
}