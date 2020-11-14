/*
 * Author: John Rowan
 * Description: Helper class for shared preferences
 * Anyone may use this file or anything contained in this project for their own personal use.
 */
package powerball.apps.jacs.powerball

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

object SharedPrefHelper {
    /**
     *
     * @param context
     * @param key
     * @return an array list of MyTicket class
     * Description: gets an arraylist of MyTicket from shared preferences based
     * on the String key parameter which is either powerball or megamillions
     */
    fun getMyTickets(context: Context, key: String?): ArrayList<MyTicket> {
        val listOfObjects = object : TypeToken<List<MyTicket?>?>() {}.type
        val myPrefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val json = myPrefs.getString(key, "")
        val gson = Gson()
        return if (json != "") gson.fromJson(json, listOfObjects) else ArrayList()
    }

    /**
     *
     * @param context
     * @param tickets
     * @param key
     * Desctiption: stores an arraylist of MyTickets objects in shared preferences based on
     * String key which is either powerball or megamillions
     */
    fun setMyTickets(context: Context, tickets: List<MyTicket?>?, key: String?) {
        val gson = Gson()
        val listOfObjects = object : TypeToken<List<MyTicket?>?>() {}.type
        val strObject = gson.toJson(tickets, listOfObjects) // Here list is your List<CUSTOM_CLASS> object
        val myPrefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val prefsEditor = myPrefs.edit()
        prefsEditor.putString(key, strObject)
        prefsEditor.commit()
    }

    /**
     *
     * @param context
     * @param key
     * @return and arraylist of SimulatorData
     * Description: gets an arraylist of simulator data from shared preferences based on key
     */
    fun getSimData(context: Context, key: String?): ArrayList<SimulatorData> {
        val listOfObjects = object : TypeToken<List<SimulatorData?>?>() {}.type
        val myPrefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val json = myPrefs.getString(key, "")
        val gson = Gson()
        return if (json != "" && json != null && json != "null") {
            Log.d("gson", "the string is not empty : $json")
            gson.fromJson(json, listOfObjects)
        } else {
            Log.d("gson", "the string is empty")
            ArrayList()
        }
    }

    /**
     *
     * @param context
     * @param list
     * @param key
     * Desctiption: stores an arraylist of SimulatorData objects in shared preferences based on a string key
     * from constants power_sim and mega_sim
     */
    fun setSimData(context: Context, list: List<SimulatorData?>?, key: String?) {
        val gson = Gson()
        val listOfObjects = object : TypeToken<List<SimulatorData?>?>() {}.type
        val strObject = gson.toJson(list, listOfObjects) // Here list is your List<CUSTOM_CLASS> object
        val myPrefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val prefsEditor = myPrefs.edit()
        prefsEditor.putString(key, strObject)
        prefsEditor.commit()
    }

    /**
     *
     * @param context
     * @return boolean
     * Description: returns true if setting are set to schedule powerball alarms, false otherwise
     */
    fun shouldPowerballAlarmsBeSet(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("PowerballAlarm", true)
    }

    /**
     *
     * @param context
     * @return boolean
     * Description: returns true if setting are set to schedule megamillions alarms, false otherwise
     */
    fun shouldMegaMillionsAlarmsBeSet(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("MegaMillionsAlarm", true)
    }

    fun setLongPowerballCounter(context: Context, num: Long) {
        val myPrefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val prefsEditor = myPrefs.edit()
        prefsEditor.putLong(Constants.POWER_LONG_COUNTER, num)
        prefsEditor.commit()
    }

    fun getLongPowerballCounter(context: Context): Long {
        val myPrefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return myPrefs.getLong(Constants.POWER_LONG_COUNTER, 0)
    }
}