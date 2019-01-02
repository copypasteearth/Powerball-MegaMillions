package powerball.apps.jacs.powerball;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.content.ContextCompat;
import android.util.Log;

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