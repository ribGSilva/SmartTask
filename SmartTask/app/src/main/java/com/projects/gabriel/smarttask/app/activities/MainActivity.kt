package com.projects.gabriel.smarttask.app.activities

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.projects.gabriel.smarttask.R
import com.projects.gabriel.smarttask.app.fragments.NewTaskFragment
import com.projects.gabriel.smarttask.app.fragments.TaskListFragment
import com.projects.gabriel.smarttask.app.fragments.enuns.TypeOfFragment
import com.projects.gabriel.smarttask.app.interfacies.CreatorListFromDialogListener
import com.projects.gabriel.smarttask.app.interfacies.SmartTaskManager
import com.projects.gabriel.smarttask.domain.factories.DateTimeFactory
import com.projects.gabriel.smarttask.domain.factories.DialogFactory
import com.projects.gabriel.smarttask.domain.factories.TaskListFactory
import com.projects.gabriel.smarttask.domain.services.NotificationService
import com.projects.gabriel.smarttask.domain.services.TaskService
import com.projects.gabriel.smarttask.domain.valueObjects.CreateNotificationSchedule
import com.projects.gabriel.smarttask.domain.valueObjects.TaskList
import kotlinx.android.synthetic.main.activity_principal.*
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SmartTaskManager {

    private var currentFragment = TypeOfFragment.TASK_LIST_FRAGMENT
    private lateinit var taskService: TaskService
    private lateinit var taskList: ArrayList<TaskList>
    private lateinit var currentTaskList: TaskList
    private lateinit var menuNavigationView: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        setSupportActionBar(toolbar)

        taskService = TaskService.getInstance(baseContext)
        taskList = taskService.getTaskLists()

        currentTaskList = taskList[0]

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()


        nav_view.setNavigationItemSelectedListener(this)
        nav_view.setCheckedItem(0)
        updateNavigationMenu()

        NotificationService.getInstance().scheduleNotification(
                CreateNotificationSchedule.Builder(this).body("22:40").body("agora vai").dateTime(
                        DateTimeFactory.newInstance("13:53", "6/1/2018")
                ).build()
        )

        supportFragmentManager.addOnBackStackChangedListener {
            val lastFragment = supportFragmentManager.fragments
                    .asSequence()
                    .filterNotNull().last()

            currentFragment = TypeOfFragment.valueOf(lastFragment.tag)
        }

        val transaction = supportFragmentManager.beginTransaction()
        val newFragment = TaskListFragment.newInstance(currentTaskList)
        transaction.add(R.id.content_layout, newFragment, TypeOfFragment.TASK_LIST_FRAGMENT.name)
        transaction.addToBackStack(TypeOfFragment.TASK_LIST_FRAGMENT.name)
        transaction.commit()
    }

    override fun changeFragment(newFragmentType: TypeOfFragment, addToBack: Boolean) {
        if (currentFragment == newFragmentType && currentFragment != TypeOfFragment.TASK_LIST_FRAGMENT) return
        val newFragment = getNewFragmentByType(newFragmentType)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_layout, newFragment, newFragmentType.name)
        if (addToBack)
            transaction.addToBackStack(newFragmentType.name)
        transaction.commit()
        changeTitleFromToolbar()
    }

    private fun changeTitleFromToolbar() {
        if(currentFragment == TypeOfFragment.TASK_LIST_FRAGMENT)
            toolbar.title = currentTaskList.title
        else
            toolbar.title = getString(currentFragment.title)
    }

    override fun getTaskService(): TaskService {
        return taskService
    }

    override fun addNewTaskList(newTaskList: TaskList) {
        taskService.createList(newTaskList)
        taskList = taskService.getTaskLists()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (currentFragment == TypeOfFragment.TASK_LIST_FRAGMENT)
                finish()
            else
                super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.principal, menu)
        menuNavigationView = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_task -> {
                changeFragment(TypeOfFragment.NEW_TASK_FRAGMENT, true)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.groupId) {
            0 -> {
                currentTaskList = taskList[0]
                changeFragment(TypeOfFragment.TASK_LIST_FRAGMENT, false)
                nav_view.setCheckedItem(0)

            }
            1 -> {
                currentTaskList = taskList[item.itemId]
                changeFragment(TypeOfFragment.TASK_LIST_FRAGMENT, false)
                nav_view.setCheckedItem(item.itemId)
            }
            2 -> {
                when (item.itemId) {
                    1 -> DialogFactory.createDialogToCreateNewTaskList(this,
                            object : CreatorListFromDialogListener {
                                override fun createList(listName: String) {
                                    if (listName.isBlank())
                                        Toast.makeText(baseContext, getString(R.string.list_name_invalid), Toast.LENGTH_LONG).show()
                                    else {
                                        taskService.createList(
                                                TaskListFactory.createTaskList(listName)
                                        )
                                        taskList = taskService.getTaskLists()
                                        nav_view.menu.clear()
                                        updateNavigationMenu()
                                        Toast.makeText(baseContext, getString(R.string.new_list_created), Toast.LENGTH_LONG).show()
                                    }
                                }

                            }
                    ).show()
//                2 -> changeFragment()
                }
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun updateNavigationMenu() {

        var subMenuNavigationView = nav_view.menu.addSubMenu(getString(R.string.your_tasks_lists))

        taskList.forEachIndexed { index, listName ->
            if (index != 0)
                subMenuNavigationView.add(1, index, index, listName.title)//.icon
        }

        subMenuNavigationView = nav_view.menu.addSubMenu(getString(R.string.menu_options_section))
        subMenuNavigationView.add(2, 1, 1, getString(R.string.menu_item_new_list))//.icon
        subMenuNavigationView.add(2, 2, 2, getString(R.string.menu_item_remove_list))//.icon
    }

    private fun getNewFragmentByType(fragmentType: TypeOfFragment): Fragment {
        if (fragmentType == TypeOfFragment.TASK_LIST_FRAGMENT)
            return TaskListFragment.newInstance(currentTaskList)

        var fragment: Fragment?
        fragment = supportFragmentManager.findFragmentByTag(fragmentType.name)
        if (fragment == null) {
            fragment = when (fragmentType) {
                TypeOfFragment.NEW_TASK_FRAGMENT -> NewTaskFragment.newInstance()
                //this comparation were done because the switch had to have all the enum situations
                TypeOfFragment.TASK_LIST_FRAGMENT -> TaskListFragment.newInstance(currentTaskList)
            }
        }
        return fragment
    }
}
