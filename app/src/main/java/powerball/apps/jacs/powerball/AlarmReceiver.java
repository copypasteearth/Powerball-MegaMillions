/*
 * Author: John Rowan
 * Description: This class is a Broadcast Reciever for catching alarms.
 * Anyone may use this file or anything contained in this project for their own personal use.
 */

package powerball.apps.jacs.powerball;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * When the alarms that were set fire it will trigger this and start the required service.
 * This checks if the sdk is greater than or equal to oreo and starts the service in foreground
 * as new specifications requires, otherwise it starts service the old way.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int requestCode = intent.getExtras().getInt("requestCode");
        if(requestCode == Constants.WEDNESDAY_ALARM || requestCode == Constants.SATURDAY_ALARM){
            Intent background = new Intent(context, BackgroundService.class);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                ContextCompat.startForegroundService(context,background);
            }else{
                context.startService(background);
            }

            Log.d("timer","reciever triggered");
        }else{
            Intent background = new Intent(context,MegaBackgroundService.class);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                ContextCompat.startForegroundService(context,background);
            }else{
                context.startService(background);
            }
            Log.d("timer", "reciever triggered");
        }

    }

}