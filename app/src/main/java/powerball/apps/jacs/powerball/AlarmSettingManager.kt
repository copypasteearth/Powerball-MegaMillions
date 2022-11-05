/*
 * Author: John Rowan
 * Description: This is a helper class to set and cancel alarms.
 * Anyone may use this file or anything contained in this project for their own personal use.
 */
package powerball.apps.jacs.powerball

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.*

object AlarmSettingManager {

    fun setMondayPowerballAlarm(context: Context) {
        val alarm = Intent("powerball.apps.jacs.powerball.START_ALARM")
        alarm.putExtra("requestCode", Constants.MONDAY_ALARM)
        alarm.setClass(context, AlarmReceiver::class.java)
        val alarmRunning = PendingIntent.getBroadcast(context, Constants.MONDAY_ALARM, alarm, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_MUTABLE) != null
        if (!alarmRunning) {
            val now = Calendar.getInstance()
            val calSet = Calendar.getInstance()
            //wednesday
            calSet[Calendar.DAY_OF_WEEK] = Calendar.TUESDAY
            calSet[Calendar.HOUR_OF_DAY] = 7
            calSet[Calendar.MINUTE] = 15
            calSet[Calendar.SECOND] = 0
            calSet[Calendar.MILLISECOND] = 0
            if (!calSet.after(now)) {
                calSet.add(Calendar.WEEK_OF_YEAR, 1)
                Log.d("timer", "setting later date: " + calSet.time.toString())
            } else {
                Log.d("timer", "date is good: " + calSet.time.toString())
            }
            Log.d("timer", Date(calSet.timeInMillis).toString())
            val pendingIntent = PendingIntent.getBroadcast(context, Constants.MONDAY_ALARM, alarm, PendingIntent.FLAG_MUTABLE)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.timeInMillis, Constants.ONE_WEEK, pendingIntent)
        }
    }
    /**
     *
     * @param context
     * Description: sets an alarm to go off at 11:15pm every tuesday
     * which triggers the broadcast reciever and starts a service
     */
    @JvmStatic
    fun setTuesdayMegaMillionsAlarm(context: Context) {
        val alarm = Intent("powerball.apps.jacs.powerball.START_ALARM")
        alarm.putExtra("requestCode", Constants.TUESDAY_ALARM)
        alarm.setClass(context, AlarmReceiver::class.java)
        val alarmRunning = PendingIntent.getBroadcast(context, Constants.TUESDAY_ALARM, alarm, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_MUTABLE) != null
        if (!alarmRunning) {
            val now = Calendar.getInstance()
            val calSet = Calendar.getInstance()
            //wednesday
            calSet[Calendar.DAY_OF_WEEK] = Calendar.WEDNESDAY
            calSet[Calendar.HOUR_OF_DAY] = 7
            calSet[Calendar.MINUTE] = 15
            calSet[Calendar.SECOND] = 0
            calSet[Calendar.MILLISECOND] = 0
            if (!calSet.after(now)) {
                calSet.add(Calendar.WEEK_OF_YEAR, 1)
                Log.d("timer", "setting later date: " + calSet.time.toString())
            } else {
                Log.d("timer", "date is good: " + calSet.time.toString())
            }
            Log.d("timer", Date(calSet.timeInMillis).toString())
            val pendingIntent = PendingIntent.getBroadcast(context, Constants.TUESDAY_ALARM, alarm, PendingIntent.FLAG_MUTABLE)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.timeInMillis, Constants.ONE_WEEK, pendingIntent)
        }
    }

    /**
     *
     * @param context
     * Description: cancels the tuesday alarm
     */
    fun cancelTuesdayMegaMillionsAlarm(context: Context) {
        val alarm = Intent("powerball.apps.jacs.powerball.START_ALARM")
        alarm.setClass(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, Constants.TUESDAY_ALARM, alarm, PendingIntent.FLAG_MUTABLE)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    /**
     *
     * @param context
     * Description: sets an alarm to go off at 11:15pm every wednesday
     * which triggers the broadcast reciever and starts a service
     */
    @JvmStatic
    fun setWednesdayPowerballAlarm(context: Context) {
        val alarm = Intent("powerball.apps.jacs.powerball.START_ALARM")
        alarm.putExtra("requestCode", Constants.WEDNESDAY_ALARM)
        alarm.setClass(context, AlarmReceiver::class.java)
        val alarmRunning = PendingIntent.getBroadcast(context, Constants.WEDNESDAY_ALARM, alarm, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_MUTABLE) != null
        if (!alarmRunning) {
            val now = Calendar.getInstance()
            val calSet = Calendar.getInstance()
            //wednesday
            calSet[Calendar.DAY_OF_WEEK] = Calendar.THURSDAY
            calSet[Calendar.HOUR_OF_DAY] = 7
            calSet[Calendar.MINUTE] = 15
            calSet[Calendar.SECOND] = 0
            calSet[Calendar.MILLISECOND] = 0
            if (!calSet.after(now)) {
                calSet.add(Calendar.WEEK_OF_YEAR, 1)
                Log.d("timer", "setting later date: " + calSet.time.toString())
            } else {
                Log.d("timer", "date is good: " + calSet.time.toString())
            }
            Log.d("timer", Date(calSet.timeInMillis).toString())
            val pendingIntent = PendingIntent.getBroadcast(context, Constants.WEDNESDAY_ALARM, alarm, PendingIntent.FLAG_MUTABLE)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.timeInMillis, Constants.ONE_WEEK, pendingIntent)
        }
    }

    /**
     *
     * @param context
     * Description: cancels wednesdays powerball alarm
     */
    fun cancelWednesdayPowerballAlarm(context: Context) {
        val alarm = Intent("powerball.apps.jacs.powerball.START_ALARM")
        alarm.setClass(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, Constants.WEDNESDAY_ALARM, alarm, PendingIntent.FLAG_MUTABLE)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    /**
     *
     * @param context
     * Description: sets an alarm to go off at 11:15pm every friday
     * which triggers the broadcast reciever and starts a service
     */
    @JvmStatic
    fun setFridayMegaMillionsAlarm(context: Context) {
        val alarm = Intent("powerball.apps.jacs.powerball.START_ALARM")
        alarm.putExtra("requestCode", Constants.FRIDAY_ALARM)
        alarm.setClass(context, AlarmReceiver::class.java)
        val alarmRunning = PendingIntent.getBroadcast(context, Constants.FRIDAY_ALARM, alarm, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_MUTABLE) != null
        if (!alarmRunning) {
            val now = Calendar.getInstance()
            val calSet1 = Calendar.getInstance()
            //Saturday
            calSet1[Calendar.DAY_OF_WEEK] = Calendar.SATURDAY
            calSet1[Calendar.HOUR_OF_DAY] = 7
            calSet1[Calendar.MINUTE] = 15
            calSet1[Calendar.SECOND] = 0
            calSet1[Calendar.MILLISECOND] = 0
            if (!calSet1.after(now)) {
                calSet1.add(Calendar.WEEK_OF_YEAR, 1)
                Log.d("timer", "setting later date: " + calSet1.time.toString())
            } else {
                Log.d("timer", "date is good: " + calSet1.time.toString())
            }
            Log.d("timer", Date(calSet1.timeInMillis).toString())
            val pendingIntent1 = PendingIntent.getBroadcast(context, Constants.FRIDAY_ALARM, alarm, PendingIntent.FLAG_MUTABLE)
            val alarmManager1 = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP, calSet1.timeInMillis, Constants.ONE_WEEK, pendingIntent1)
        }
    }

    /**
     *
     * @param context
     * Description: cancels the friday alarm
     */
    fun cancelFridayMegaMillionsAlarm(context: Context) {
        val alarm = Intent("powerball.apps.jacs.powerball.START_ALARM")
        alarm.setClass(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, Constants.FRIDAY_ALARM, alarm, PendingIntent.FLAG_MUTABLE)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    /**
     *
     * @param context
     * Description: sets an alarm to go off at 11:15pm every saturday
     * which triggers the broadcast reciever and starts a service
     */
    @JvmStatic
    fun setSaturdayPowerballAlarm(context: Context) {
        val alarm = Intent("powerball.apps.jacs.powerball.START_ALARM")
        alarm.putExtra("requestCode", Constants.SATURDAY_ALARM)
        alarm.setClass(context, AlarmReceiver::class.java)
        val alarmRunning = PendingIntent.getBroadcast(context, Constants.SATURDAY_ALARM, alarm, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_MUTABLE) != null
        if (!alarmRunning) {
            val now = Calendar.getInstance()
            val calSet1 = Calendar.getInstance()
            //Saturday
            calSet1[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
            calSet1[Calendar.HOUR_OF_DAY] = 7
            calSet1[Calendar.MINUTE] = 15
            calSet1[Calendar.SECOND] = 0
            calSet1[Calendar.MILLISECOND] = 0
            if (!calSet1.after(now)) {
                calSet1.add(Calendar.WEEK_OF_YEAR, 1)
                Log.d("timer", "setting later date: " + calSet1.time.toString())
            } else {
                Log.d("timer", "date is good: " + calSet1.time.toString())
            }
            Log.d("timer", Date(calSet1.timeInMillis).toString())
            val pendingIntent1 = PendingIntent.getBroadcast(context, Constants.SATURDAY_ALARM, alarm, PendingIntent.FLAG_MUTABLE)
            val alarmManager1 = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP, calSet1.timeInMillis, Constants.ONE_WEEK, pendingIntent1)
        }
    }

    /**
     *
     * @param context
     * Description: cancels saturdays powerball alarm
     */
    fun cancelSaturdayPowerballAlarm(context: Context) {
        val alarm = Intent("powerball.apps.jacs.powerball.START_ALARM")
        alarm.setClass(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, Constants.SATURDAY_ALARM, alarm, PendingIntent.FLAG_MUTABLE)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}