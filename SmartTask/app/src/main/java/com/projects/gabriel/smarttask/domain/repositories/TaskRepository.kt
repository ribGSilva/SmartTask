package com.projects.gabriel.smarttask.domain.repositories

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.projects.gabriel.smarttask.domain.entities.Task
import com.projects.gabriel.smarttask.domain.factories.DateTimeFactory
import com.projects.gabriel.smarttask.domain.factories.TaskListFactory
import com.projects.gabriel.smarttask.domain.types.PriorityLevel
import com.projects.gabriel.smarttask.domain.valueObjects.TaskList
import com.projects.gabriel.smarttask.exceptions.NotAllowedToDeleteTableException
import java.util.TreeSet
import kotlin.collections.ArrayList

@SuppressLint("Recycle")
class TaskRepository private constructor(context: Context) :
        SQLiteOpenHelper(context, DB_NAME, null, DATABASE_VERSION) {

    private val db: SQLiteDatabase = writableDatabase

    companion object {

        private var instance: TaskRepository? = null

        var PERSONAL_TASK_LIST: TaskList? = null

        private val TABLE_OF_LIST = "TABLE_OF_LIST"
        private val COLUMN_LIST_NAME = "list_name"
        private val COLUMN_LIST_TABLE_NAME = "list_table_name"
        private val DB_NAME = "SMART_TASK"
        private val DATABASE_VERSION = 1
        private val COLUMN_ID = "id"

        private val COLUMN_TITLE = "titleOfNotification"
        private val COLUMN_BODY = "bodyOfNotification"
        private val COLUMN_PRIORITY = "priority"
        private val COLUMN_DATE = "date"
        private val COLUMN_TIME = "time"
        private val COLUMN_MINUTES_BEFORE_ALARM = "minutes_before_alarm"

        private val CREATE_LIST_OF_TABLES =
                "CREATE TABLE IF NOT EXISTS $TABLE_OF_LIST ( " +
                        "$COLUMN_ID INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT, " +
                        "$COLUMN_LIST_NAME TEXT, " +
                        "$COLUMN_LIST_TABLE_NAME TEXT)"

        @Synchronized
        fun getInstance(context: Context): TaskRepository {
            Log.i(TAG, "Giving new task repository")
            if (instance == null)
                instance = TaskRepository(context)
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.i(TAG, "Creating database")
        db.execSQL(CREATE_LIST_OF_TABLES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    private fun createTaskTable(tableName: String): Boolean {
        db.execSQL(createSQLToCreateNewTableOfList(tableName))
        return taskTableExists(tableName)
    }

    private fun createSQLToCreateNewTableOfList(tableName: String): String {
        return ("CREATE TABLE IF NOT EXISTS $tableName ( " +
                "$COLUMN_ID INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TITLE TEXT, $COLUMN_BODY TEXT, " +
                "$COLUMN_PRIORITY INTEGER, $COLUMN_DATE TEXT, " +
                "$COLUMN_TIME TEXT, $COLUMN_MINUTES_BEFORE_ALARM INTEGER ) ")
    }

    fun addTaskTable(listTask: TaskList): Boolean {
        Log.i(TAG, "Creating new task list")

        if (taskTableExists(listTask.tableName))
            return false

        val values = ContentValues()
        values.put(COLUMN_LIST_NAME, listTask.title)
        values.put(COLUMN_LIST_TABLE_NAME, listTask.tableName)

        db.insert(TABLE_OF_LIST, null, values)

        createTaskTable(listTask.tableName)

        return taskTableExists(listTask.tableName)
    }

    fun getListOfTaskTables(): ArrayList<TaskList> {
        val list = ArrayList<TaskList>()

        val cursor = db.rawQuery("SELECT * FROM $TABLE_OF_LIST", null)

        if (cursor.moveToFirst())
            do {
                list.add(
                        TaskListFactory.createTaskList(
                                cursor.getString(cursor.getColumnIndex(COLUMN_LIST_NAME)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_LIST_TABLE_NAME))
                        )
                )
            } while (cursor.moveToNext())

        Log.i(TAG, "Retrieving lists of tasks")
        return list
    }

    private fun taskTableExists(taskTableName: String): Boolean {

        val cursor = db.rawQuery("SELECT * FROM $TABLE_OF_LIST WHERE $COLUMN_LIST_TABLE_NAME = '$taskTableName'", null)

        return cursor.moveToFirst()
    }

    fun removeTaskTable(taskList: TaskList): TreeSet<Task>? {
        if (taskList == PERSONAL_TASK_LIST)
            throw NotAllowedToDeleteTableException("Trying to delete default list (personal list)")

        if (taskTableExists(taskList.tableName)) {
            val ret = deleteTableByName(taskList.tableName)
            db.execSQL("DELETE FROM $TABLE_OF_LIST WHERE $COLUMN_LIST_TABLE_NAME = '${taskList.tableName}'")
            return ret
        }

        Log.i(TAG, "Removing task list")

        return null
    }

    fun addTaskToTable(task: Task, tableName: String): Task? {
        val values = ContentValues()
        values.put(COLUMN_TITLE, task.title)
        values.put(COLUMN_BODY, task.description)
        values.put(COLUMN_PRIORITY, task.priority.ordinal)
        values.put(COLUMN_DATE, task.finalDate.getFormatedDate())
        values.put(COLUMN_TIME, task.finalDate.getFormatedTime())
        values.put(COLUMN_MINUTES_BEFORE_ALARM, task.timeInMinutesBeforeAlarm)

        db.insert(tableName, null, values)

        Log.i(TAG, "Adding new task")

        return getTaskFromTableWithTitle(task.title, tableName)
    }

    private fun getTaskFromTableWithId(id: Int, tableName: String): Task? {

        val cursor = db.rawQuery("SELECT * FROM $tableName WHERE $COLUMN_ID = $id", null)

        Log.e(TAG, "Getting task by id")

        if (cursor.moveToFirst())
            return Task(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_BODY)),
                    PriorityLevel.getPriorityLevelByOrdinal(cursor.getInt(cursor.getColumnIndex(COLUMN_PRIORITY))),
                    DateTimeFactory.newInstance(
                            cursor.getString(cursor.getColumnIndex(COLUMN_TIME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                    ),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_MINUTES_BEFORE_ALARM))
            )
        return null
    }

    private fun getTaskFromTableWithTitle(title: String, tableName: String): Task? {

        val cursor = db.rawQuery("SELECT * FROM $tableName WHERE $COLUMN_TITLE ='$title'", null)

        Log.e(TAG, "Getting task by titleOfNotification")

        if (cursor.moveToFirst())
            return Task(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_BODY)),
                    PriorityLevel.getPriorityLevelByOrdinal(cursor.getInt(cursor.getColumnIndex(COLUMN_PRIORITY))),
                    DateTimeFactory.newInstance(
                            cursor.getString(cursor.getColumnIndex(COLUMN_TIME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                    ),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_MINUTES_BEFORE_ALARM))
            )
        return null
    }

    fun getTasksFromTable(tableName: String): TreeSet<Task> {

        val cursor = db.rawQuery("SELECT * FROM $tableName", null)

        val idColumn = cursor.getColumnIndex(COLUMN_ID)
        val titleColumn = cursor.getColumnIndex(COLUMN_TITLE)
        val bodyColumn = cursor.getColumnIndex(COLUMN_BODY)
        val priorityColumn = cursor.getColumnIndex(COLUMN_PRIORITY)
        val timeColumn = cursor.getColumnIndex(COLUMN_TIME)
        val dateColumn = cursor.getColumnIndex(COLUMN_DATE)
        val minutesBeforeColumn = cursor.getColumnIndex(COLUMN_MINUTES_BEFORE_ALARM)

        val notes = TreeSet<Task>()

        Log.e(TAG, "Getting tasks from list")

        if (cursor.moveToFirst())
            do {
                notes.add(Task(
                        cursor.getInt(idColumn),
                        cursor.getString(titleColumn),
                        cursor.getString(bodyColumn),
                        PriorityLevel.getPriorityLevelByOrdinal(cursor.getInt(priorityColumn)),
                        DateTimeFactory.newInstance(
                                cursor.getString(timeColumn),
                                cursor.getString(dateColumn)
                        ),
                        cursor.getInt(minutesBeforeColumn)
                )
                )
            } while (cursor.moveToNext())

        return notes
    }

    fun editTaskFromTable(task: Task, tableName: String): Task? {
        db.execSQL("UPDATE $tableName SET " +
                "$COLUMN_TITLE = '${task.title}' , $COLUMN_BODY = '${task.description}' , " +
                "$COLUMN_PRIORITY = ${task.priority.ordinal}, " +
                "$COLUMN_TIME = '${task.finalDate.getFormatedTime()}', " +
                "$COLUMN_DATE = '${task.finalDate.getFormatedDate()}' , " +
                "$COLUMN_MINUTES_BEFORE_ALARM = ${task.timeInMinutesBeforeAlarm} " +
                " WHERE $COLUMN_ID = ${task.id}")
        Log.e(TAG, "Editing task")
        return getTaskFromTableWithId(task.id, tableName)
    }

    fun removeTaskFromTableWithId(id: Int, tableName: String): Task? {
        Log.e(TAG, "Removing task by id")
        val ret = getTaskFromTableWithId(id, tableName)
        if (ret != null)
            db.execSQL("DELETE FROM $tableName WHERE $COLUMN_ID = $id")
        return ret
    }

    private fun deleteTableByName(tableName: String): TreeSet<Task> {
        Log.e(TAG, "Removing task list")
        val ret = getTasksFromTable(tableName)
        db.execSQL("DROP TABLE IF EXISTS $tableName")
        return ret
    }

}
