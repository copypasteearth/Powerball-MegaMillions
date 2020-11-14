/*
 * Author: John Rowan
 * Description: when the users device boots up or reboots this schedules the alarms again
 * Anyone may use this file or anything contained in this project for their own personal use.
 */
package powerball.apps.jacs.powerball

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import powerball.apps.jacs.powerball.AlarmSettingManager.setFridayMegaMillionsAlarm
import powerball.apps.jacs.powerball.AlarmSettingManager.setSaturdayPowerballAlarm
import powerball.apps.jacs.powerball.AlarmSettingManager.setTuesdayMegaMillionsAlarm
import powerball.apps.jacs.powerball.AlarmSettingManager.setWednesdayPowerballAlarm

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (SharedPrefHelper.shouldPowerballAlarmsBeSet(context)) {
            setWednesdayPowerballAlarm(context)
            setSaturdayPowerballAlarm(context)
        }
        if (SharedPrefHelper.shouldMegaMillionsAlarmsBeSet(context)) {
            setFridayMegaMillionsAlarm(context)
            setTuesdayMegaMillionsAlarm(context)
        }
    }
}