/*
 * Author: John Rowan
 * Description: Helper class for shared preferences
 * Anyone may use this file or anything contained in this project for their own personal use.
 */

package powerball.apps.jacs.powerball;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPrefHelper {
    /**
     *
     * @param context
     * @param key
     * @return an array list of MyTicket class
     * Description: gets an arraylist of MyTicket from shared preferences based
     * on the String key parameter which is either powerball or megamillions
     */
    public static ArrayList<MyTicket> getMyTickets(Context context,String key){
        Type listOfObjects = new TypeToken<List<MyTicket>>(){}.getType();
        SharedPreferences myPrefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        String json = myPrefs.getString(key, "");
        Gson gson = new Gson();
        if(!json.equals(""))
            return gson.fromJson(json, listOfObjects);
        else
            return new ArrayList<MyTicket>();
    }

    /**
     *
     * @param context
     * @param tickets
     * @param key
     * Desctiption: stores an arraylist of MyTickets objects in shared preferences based on
     * String key which is either powerball or megamillions
     */
    public static void setMyTickets(Context context,List<MyTicket> tickets,String key){
        Gson gson = new Gson();
        Type listOfObjects = new TypeToken<List<MyTicket>>(){}.getType();
        String strObject = gson.toJson(tickets, listOfObjects); // Here list is your List<CUSTOM_CLASS> object
        SharedPreferences  myPrefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putString(key, strObject);
        prefsEditor.commit();
    }

    /**
     *
     * @param context
     * @param key
     * @return and arraylist of SimulatorData
     * Description: gets an arraylist of simulator data from shared preferences based on key
     */
    public static ArrayList<SimulatorData> getSimData(Context context,String key){
        Type listOfObjects = new TypeToken<List<SimulatorData>>(){}.getType();
        SharedPreferences myPrefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        String json = myPrefs.getString(key, "");
        Gson gson = new Gson();
        if(!json.equals("") && json != null && !json.equals("null")){
            Log.d("gson", "the string is not empty : " + json);
            return gson.fromJson(json, listOfObjects);
        }

        else{
            Log.d("gson", "the string is empty");
            return new ArrayList<SimulatorData>();
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
    public static void setSimData(Context context, List<SimulatorData> list,String key){
        Gson gson = new Gson();
        Type listOfObjects = new TypeToken<List<SimulatorData>>(){}.getType();
        String strObject = gson.toJson(list, listOfObjects); // Here list is your List<CUSTOM_CLASS> object
        SharedPreferences  myPrefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putString(key, strObject);
        prefsEditor.commit();
    }

    /**
     *
     * @param context
     * @return boolean
     * Description: returns true if setting are set to schedule powerball alarms, false otherwise
     */
    public static boolean shouldPowerballAlarmsBeSet(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("PowerballAlarm",true);
    }

    /**
     *
     * @param context
     * @return boolean
     * Description: returns true if setting are set to schedule megamillions alarms, false otherwise
     */
    public static boolean shouldMegaMillionsAlarmsBeSet(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("MegaMillionsAlarm",true);
    }
    public static void setLongPowerballCounter(Context context,long num){
        SharedPreferences  myPrefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putLong(Constants.POWER_LONG_COUNTER, num);
        prefsEditor.commit();
    }
    public static long getLongPowerballCounter(Context context){
        SharedPreferences myPrefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        long num = myPrefs.getLong(Constants.POWER_LONG_COUNTER, 0);
        return num;
    }
}
