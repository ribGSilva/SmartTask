package com.projects.gabriel.smarttask.app.types

import com.projects.gabriel.smarttask.R

enum class AlarmTimers(val text: Int, val timeInMinutes: Int) {
    NONE(R.string.timer_alarm_none_alarm, 0),
    FIVE_MINUTES(R.string.timer_alarm_five_minutes, 5),
    TEN_MINUTES(R.string.timer_alarm_ten_minutes, 10),
    FIFTEEN_MINUTES(R.string.timer_alarm_fifteen_minutes, 15),
    THIRTY_MINUTES(R.string.timer_alarm_thirty_minutes, 30),
    FORTY_FIVE_MINUTES(R.string.timer_alarm_forty_five_minutes, 45),
    ONE_HOUR(R.string.timer_alarm_one_hour, 60),
    ONE_AND_A_HALF_HOUR(R.string.timer_alarm_one_and_a_half_hour, 90),
    TWO_HOURS(R.string.timer_alarm_two_hours, 120),
    THREE_HOURS(R.string.timer_alarm_three_hours, 180);
}