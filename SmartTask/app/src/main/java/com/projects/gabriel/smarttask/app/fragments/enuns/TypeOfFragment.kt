package com.projects.gabriel.smarttask.app.fragments.enuns

import com.projects.gabriel.smarttask.R

enum class TypeOfFragment (val title: Int) {
    NEW_TASK_FRAGMENT(R.string.fragment_type_new_task) ,
    TASK_LIST_FRAGMENT(R.string.fragment_type_task_list);



    companion object {
        fun getTypeByName(typeName: String): TypeOfFragment? {
            when (typeName){
                NEW_TASK_FRAGMENT.name ->
                        return NEW_TASK_FRAGMENT
                TASK_LIST_FRAGMENT.name ->
                        return TASK_LIST_FRAGMENT
            }
            return null
        }
    }
}