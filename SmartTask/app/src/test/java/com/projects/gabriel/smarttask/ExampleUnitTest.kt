package com.projects.gabriel.smarttask

import com.projects.gabriel.smarttask.domain.types.PriorityLevel
import com.projects.gabriel.smarttask.domain.types.TypesOfNotification
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TesteEnum {
    @Test
    fun testeEnum(){
        System.out.println("++++++++++++++++++++"+ TypesOfNotification.COMMON_NOTIFICATION.ordinal)
        System.out.println("++++++++++++++++++++"+ PriorityLevel.LOW.ordinal)
        System.out.println("++++++++++++++++++++"+ PriorityLevel.MEDIUM_LOW.ordinal)
        System.out.println("++++++++++++++++++++"+ PriorityLevel.MEDIUM.ordinal)
        System.out.println("++++++++++++++++++++"+ PriorityLevel.MEDIUM_HIGH.ordinal)
        System.out.println("++++++++++++++++++++"+ PriorityLevel.HIGH.ordinal)
    }
}
