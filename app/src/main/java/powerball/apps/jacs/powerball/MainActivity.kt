/*
 * Author: John Rowan
 * Description: Main Activity the main entry point to the application
 * Anyone may use this file or anything contained in this project for their own personal use.
 */
package powerball.apps.jacs.powerball

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import powerball.apps.jacs.powerball.AlarmSettingManager.setFridayMegaMillionsAlarm
import powerball.apps.jacs.powerball.AlarmSettingManager.setSaturdayPowerballAlarm
import powerball.apps.jacs.powerball.AlarmSettingManager.setTuesdayMegaMillionsAlarm
import powerball.apps.jacs.powerball.AlarmSettingManager.setWednesdayPowerballAlarm

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var first = false
    private val alarmMgr: AlarmManager? = null
    private val alarmIntent: PendingIntent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        val fragment = PastNumbers.newInstance()
        val args = Bundle()
        fragment.arguments = args

        // Insert the fragment by replacing any existing fragment
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit()
        //checkAndSetAlarms();
        if (SharedPrefHelper.shouldPowerballAlarmsBeSet(this)) {
            setWednesdayPowerballAlarm(this)
            setSaturdayPowerballAlarm(this)
        }
        if (SharedPrefHelper.shouldMegaMillionsAlarmsBeSet(this)) {
            setTuesdayMegaMillionsAlarm(this)
            setFridayMegaMillionsAlarm(this)
        }

    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        if (id == R.id.MyTickets) {
            //MyNumbers fragment = MyNumbers.newInstance();
            val fragment = PickMyTickets.newInstance()
            val args = Bundle()
            fragment.arguments = args

            // Insert the fragment by replacing any existing fragment
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment) //.commitAllowingStateLoss();
                    .commit()
            // Handle the camera action
        } else if (id == R.id.Powerball) {
            val fragment = PastNumbers.newInstance()
            val args = Bundle()
            fragment.arguments = args

            // Insert the fragment by replacing any existing fragment
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit()
        } else if (id == R.id.Simulator) {
            val fragment = SimulatorFragment.newInstance()
            val args = Bundle()
            fragment.arguments = args

            // Insert the fragment by replacing any existing fragment
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit()
        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}