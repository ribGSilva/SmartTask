package com.projects.gabriel.smarttask.domain.types

enum class TypesOfNotification{
    COMMON_NOTIFICATION;

    companion object {
        fun getTypeByOrdinary(ordinary : Int): TypesOfNotification {
            return when (ordinary){
                0 -> COMMON_NOTIFICATION
                else -> COMMON_NOTIFICATION
            }
        }
    }
}