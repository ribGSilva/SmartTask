package com.projects.gabriel.smarttask.domain.factories

import com.projects.gabriel.smarttask.domain.entities.Task
import com.projects.gabriel.smarttask.domain.valueObjects.DateTime
import com.projects.gabriel.smarttask.domain.types.PriorityLevel
import com.projects.gabriel.smarttask.exceptions.BlankStringException
import com.projects.gabriel.smarttask.exceptions.FinalDateException
import com.projects.gabriel.smarttask.exceptions.TimeInvalidException
import java.util.*

object TaskFactory {
    private fun isFinalDateValid(finalDate: DateTime): Boolean{
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)
        val day = currentTime.get(Calendar.DAY_OF_MONTH)
        val month = currentTime.get(Calendar.MONTH) + 1
        val year = currentTime.get(Calendar.YEAR)

        val todayDate = DateTimeFactory.newInstance(
                "$hour:$minute",
                "$day/$month/$year"
        )

        if (todayDate < finalDate)
            return true
        return false
    }

    fun newInstance(title: String, body: String, priorityLevel: PriorityLevel,
                    finalDate: DateTime, timeInMinutesBeforeAlarm: Int): Task {
        if (title.isBlank())
            throw BlankStringException("Titulo vazio")
        if (body.isBlank())
            throw BlankStringException("Corpo vazio")
        if (!isFinalDateValid(finalDate))
            throw FinalDateException("Data final inválida")
        if (timeInMinutesBeforeAlarm < 0)
            throw TimeInvalidException("Tempo de alarme inválido")

        return Task(-1, title, body, priorityLevel, finalDate, timeInMinutesBeforeAlarm)
    }
}
