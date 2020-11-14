/*
 * Author: John Rowan
 * Description: This class is a Broadcast Reciever for catching alarms.
 * Anyone may use this file or anything contained in this project for their own personal use.
 */
package powerball.apps.jacs.powerball

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.Log

/**
 * When the alarms that were set fire it will trigger this and start the required service.
 * This checks if the sdk is greater than or equal to oreo and starts the service in foreground
 * as new specifications requires, otherwise it starts service the old way.
 */
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val requestCode = intent.extras.getInt("requestCode")
        if (requestCode == Constants.WEDNESDAY_ALARM || requestCode == Constants.SATURDAY_ALARM) {
            val background = Intent(context, BackgroundService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(context, background)
            } else {
                context.startService(background)
            }
            Log.d("timer", "reciever triggered")
        } else {
            val background = Intent(context, MegaBackgroundService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(context, background)
            } else {
                context.startService(background)
            }
            Log.d("timer", "reciever triggered")
        }
    }
}