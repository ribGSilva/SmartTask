package com.projects.gabriel.smarttask.domain.types

import com.projects.gabriel.smarttask.R

enum class PriorityLevel (val text: Int) : Comparable<PriorityLevel>{
    LOW(R.string.priority_low),
    MEDIUM_LOW(R.string.priority_medium_low),
    MEDIUM(R.string.priority_medium),
    MEDIUM_HIGH(R.string.priority_medium_high),
    HIGH(R.string.priority_high);

    companion object {
        fun getPriorityLevelByOrdinal(ordinal: Int): PriorityLevel {
            return when (ordinal) {
                0 -> LOW
                1 -> MEDIUM_LOW
                2 -> MEDIUM
                3 -> MEDIUM_HIGH
                else -> HIGH
            }
        }
    }
}