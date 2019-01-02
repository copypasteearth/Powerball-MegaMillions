/*
 * Author: John Rowan
 * Description: This is a helper class to set and cancel alarms.
 * Anyone may use this file or anything contained in this project for their own personal use.
 */

package powerball.apps.jacs.powerball;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class AlarmSettingManager {
   /**
     *
     * @param context
     * Description: sets an alarm to go off at 11:15pm every tuesday
     *            which triggers the broadcast reciever and starts a service
     */
    public static void setTuesdayMegaMillionsAlarm(Context context) {
        Intent alarm = new Intent("powerball.apps.jacs.powerball.START_ALARM");
        alarm.putExtra("requestCode", Constants.TUESDAY_ALARM);
        alarm.setClass(context,AlarmReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(context, Constants.TUESDAY_ALARM, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if (!alarmRunning) {
            Calendar now = Calendar.getInstance();
            Calendar calSet = Calendar.getInstance();
            //wednesday
            calSet.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            calSet.set(Calendar.HOUR_OF_DAY, 23);
            calSet.set(Calendar.MINUTE, 15);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            if (!calSet.after(now)) {
                calSet.add(Calendar.WEEK_OF_YEAR, 1);
                Log.d("timer", "setting later date: " + calSet.getTime().toString());
            } else {
                Log.d("timer", "date is good: " + calSet.getTime().toString());
            }
            Log.d("timer", new Date(calSet.getTimeInMillis()).toString());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Constants.TUESDAY_ALARM, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), Constants.ONE_WEEK, pendingIntent);

        }
    }
   /**
     *
     * @param context
     * Description: cancels the tuesday alarm
     */
    public static void cancelTuesdayMegaMillionsAlarm(Context context){
        Intent alarm = new Intent("powerball.apps.jacs.powerball.START_ALARM");
        alarm.setClass(context,AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Constants.TUESDAY_ALARM, alarm, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
  /**
     *
     * @param context
     *  Description: sets an alarm to go off at 11:15pm every wednesday
     *           which triggers the broadcast reciever and starts a service
     */
    public static void setWednesdayPowerballAlarm(Context context){
        Intent alarm = new Intent("powerball.apps.jacs.powerball.START_ALARM");
        alarm.putExtra("requestCode", Constants.WEDNESDAY_ALARM);
        alarm.setClass(context,AlarmReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(context, Constants.WEDNESDAY_ALARM, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if(!alarmRunning){
            Calendar now = Calendar.getInstance();
            Calendar calSet = Calendar.getInstance();
            //wednesday
            calSet.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
            calSet.set(Calendar.HOUR_OF_DAY, 23);
            calSet.set(Calendar.MINUTE, 15);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            if(!calSet.after(now)){
                calSet.add(Calendar.WEEK_OF_YEAR,1);
                Log.d("timer","setting later date: " + calSet.getTime().toString());
            }else{
                Log.d("timer","date is good: " + calSet.getTime().toString());
            }
            Log.d("timer",new Date(calSet.getTimeInMillis()).toString());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Constants.WEDNESDAY_ALARM, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(),Constants.ONE_WEEK , pendingIntent);

        }
    }
   /**
     *
     * @param context
     *  Description: cancels wednesdays powerball alarm
     */
    public static void cancelWednesdayPowerballAlarm(Context context){
        Intent alarm = new Intent("powerball.apps.jacs.powerball.START_ALARM");
        alarm.setClass(context,AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Constants.WEDNESDAY_ALARM, alarm, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
   /**
     *
     * @param context
     * Description: sets an alarm to go off at 11:15pm every friday
     *            which triggers the broadcast reciever and starts a service
     */
    public static void setFridayMegaMillionsAlarm(Context context){
        Intent alarm = new Intent("powerball.apps.jacs.powerball.START_ALARM");
        alarm.putExtra("requestCode", Constants.FRIDAY_ALARM);
        alarm.setClass(context,AlarmReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(context, Constants.FRIDAY_ALARM, alarm, PendingIntent.FLAG_NO_CREATE) != null);

        if(!alarmRunning) {
            Calendar now = Calendar.getInstance();
            Calendar calSet1 = Calendar.getInstance();
            //Saturday
            calSet1.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
            calSet1.set(Calendar.HOUR_OF_DAY, 23);
            calSet1.set(Calendar.MINUTE, 15);
            calSet1.set(Calendar.SECOND, 0);
            calSet1.set(Calendar.MILLISECOND, 0);
            if(!calSet1.after(now)){
                calSet1.add(Calendar.WEEK_OF_YEAR,1);
                Log.d("timer","setting later date: " + calSet1.getTime().toString());
            }else{
                Log.d("timer", "date is good: " + calSet1.getTime().toString());
            }
            Log.d("timer",new Date(calSet1.getTimeInMillis()).toString());
            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, Constants.FRIDAY_ALARM, alarm, 0);
            AlarmManager alarmManager1 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP, calSet1.getTimeInMillis(),Constants.ONE_WEEK , pendingIntent1);

        }
    }
   /**
     *
     * @param context
     * Description: cancels the friday alarm
     */
    public static void cancelFridayMegaMillionsAlarm(Context context){
        Intent alarm = new Intent("powerball.apps.jacs.powerball.START_ALARM");
        alarm.setClass(context,AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Constants.FRIDAY_ALARM, alarm, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
   /**
     *
     * @param context
     * Description: sets an alarm to go off at 11:15pm every saturday
     *           which triggers the broadcast reciever and starts a service
     */
    public static void setSaturdayPowerballAlarm(Context context){
        Intent alarm = new Intent("powerball.apps.jacs.powerball.START_ALARM");
        alarm.putExtra("requestCode", Constants.SATURDAY_ALARM);
        alarm.setClass(context,AlarmReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(context, Constants.SATURDAY_ALARM, alarm, PendingIntent.FLAG_NO_CREATE) != null);

        if(!alarmRunning) {
            Calendar now = Calendar.getInstance();
            Calendar calSet1 = Calendar.getInstance();
            //Saturday
            calSet1.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            calSet1.set(Calendar.HOUR_OF_DAY, 23);
            calSet1.set(Calendar.MINUTE, 15);
            calSet1.set(Calendar.SECOND, 0);
            calSet1.set(Calendar.MILLISECOND, 0);
            if(!calSet1.after(now)){
                calSet1.add(Calendar.WEEK_OF_YEAR,1);
                Log.d("timer","setting later date: " + calSet1.getTime().toString());
            }else{
                Log.d("timer", "date is good: " + calSet1.getTime().toString());
            }
            Log.d("timer",new Date(calSet1.getTimeInMillis()).toString());
            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, Constants.SATURDAY_ALARM, alarm, 0);
            AlarmManager alarmManager1 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP, calSet1.getTimeInMillis(),Constants.ONE_WEEK , pendingIntent1);

        }
    }
    /**
     *
     * @param context
     *  Description: cancels saturdays powerball alarm
     */
    public static void cancelSaturdayPowerballAlarm(Context context){
        Intent alarm = new Intent("powerball.apps.jacs.powerball.START_ALARM");
        alarm.setClass(context,AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Constants.SATURDAY_ALARM, alarm, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
