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


    //save data in sharedPrefences

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
    public static void setMyTickets(Context context,List<MyTicket> tickets,String key){
        Gson gson = new Gson();
        Type listOfObjects = new TypeToken<List<MyTicket>>(){}.getType();
        String strObject = gson.toJson(tickets, listOfObjects); // Here list is your List<CUSTOM_CLASS> object
        SharedPreferences  myPrefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putString(key, strObject);
        prefsEditor.commit();
    }
    public static void setSharedOBJECT(Context context, String key, Object value) {

        SharedPreferences sharedPreferences =  context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();

        Gson gson = new Gson();

        String json = gson.toJson(value);

        prefsEditor.putString(key, json);

        prefsEditor.apply();
    }
    public static WinningTicket getSharedOBJECT(Context context, String key) {

        SharedPreferences sharedPreferences
                =context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);


        Gson gson = new Gson();

        String json = sharedPreferences.getString(key, "");
        Log.d("json",json);
        if(json.equals(""))
            return null;

        //Object obj = gson.fromJson(json, Object.class);

        //WinningTicket objData = new Gson().fromJson(obj.toString(), WinningTicket.class);
        WinningTicket objData = gson.fromJson(json, WinningTicket.class);

        return objData;

    }
    public static boolean shouldPowerballAlarmsBeSet(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("PowerballAlarm",true);
    }
    public static boolean shouldMegaMillionsAlarmsBeSet(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("MegaMillionsAlarm",true);
    }
}
