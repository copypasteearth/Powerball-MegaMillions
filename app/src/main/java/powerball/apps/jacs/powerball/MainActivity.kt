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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import powerball.apps.jacs.powerball.AlarmSettingManager.setFridayMegaMillionsAlarm
import powerball.apps.jacs.powerball.AlarmSettingManager.setMondayPowerballAlarm
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
            setMondayPowerballAlarm(this)
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