package com.projects.gabriel.smarttask.domain.factories

import android.app.AlertDialog
import android.content.Context
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.projects.gabriel.smarttask.R
import com.projects.gabriel.smarttask.app.interfacies.CreatorListFromDialogListener

object DialogFactory {
    fun createDialogToCreateNewTaskList(context: Context, confirmCreationListener: CreatorListFromDialogListener): AlertDialog {
        val listName = EditText(context)
        listName.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
        return AlertDialog.Builder(context)
                .setTitle(R.string.menu_item_new_list)
                .setMessage(R.string.type_the_name_of_new_list)
                .setView(listName)
                .setPositiveButton(R.string.create_text, { _, _ ->
                    confirmCreationListener.createList(listName.text.toString())
                })
                .setNegativeButton(R.string.cancel_text, { _, _ ->
//                    do nothinge
                })
                .create()
    }
}