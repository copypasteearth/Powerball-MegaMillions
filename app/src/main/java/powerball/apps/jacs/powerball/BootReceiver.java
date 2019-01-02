package powerball.apps.jacs.powerball;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class BootReceiver  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(SharedPrefHelper.shouldPowerballAlarmsBeSet(context)){
            AlarmSettingManager.setWednesdayPowerballAlarm(context);
            AlarmSettingManager.setSaturdayPowerballAlarm(context);
        }
        if(SharedPrefHelper.shouldMegaMillionsAlarmsBeSet(context)){
            AlarmSettingManager.setFridayMegaMillionsAlarm(context);
            AlarmSettingManager.setTuesdayMegaMillionsAlarm(context);
        }

    }

}
