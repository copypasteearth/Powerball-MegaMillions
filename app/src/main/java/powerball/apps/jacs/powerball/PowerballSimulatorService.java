/*
 * Author: John Rowan
 * Description: The service for running powerball simulator
 * Anyone may use this file or anything contained in this project for their own personal use.
 */

package powerball.apps.jacs.powerball;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;

public class PowerballSimulatorService extends Service {
    private final IBinder mBinder = new PowerballSimulatiorServiceBinder();
    private PowerballSimulatorListener listener;
    public static boolean running = false;
    public static boolean update = false;
    public static ArrayList<SimulatorData> data;
    public static long counter;
    public Thread thread;
    public FragmentActivity mainActivity;
    Handler handler = new Handler();
    Runnable serviceRunnable = new Runnable() {
        @Override
        public void run() {
            if(update){
                data = SharedPrefHelper.getSimData(PowerballSimulatorService.this,Constants.POWER_SIM);
                counter = SharedPrefHelper.getLongPowerballCounter(PowerballSimulatorService.this);
                update = false;
            }
            counter++;
            //SharedPrefHelper.setLongPowerballCounter(PowerballSimulatorService.this,counter);
            for(int i = 0;i<data.size();i++){
                data.get(i).plays = counter - data.get(i).ofsetPlays;

            }
            //SharedPrefHelper.setSimData(PowerballSimulatorService.this,data,Constants.POWER_SIM);
           if(listener != null){
               listener.onLottoResult(data);
               Log.d("powerservice","fired listener");
           }
            handler.postDelayed(this, 0);
        }
    };





    public PowerballSimulatorService() {
    }
    public void setPowerballSimulatorListener(PowerballSimulatorListener thelistener){
        listener = thelistener;
        Log.d("powerservice","service listener set");
    }
    public void setMainActivity(FragmentActivity main){
        mainActivity = main;
    }
    @Override
    public IBinder onBind(Intent intent) {
     return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        listener = null;
        return super.onUnbind(intent);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            if(intent.hasExtra("shutdown")){
                if(intent.getStringExtra("shutdown").equals("shutdown")){
                    if(listener != null){
                        listener.stopLottoService();
                    }else{
                        stopSelf();
                    }

                }
            }else{
                Intent notificationIntent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent =
                        PendingIntent.getActivity(this, 0, notificationIntent, 0);
                Intent serviceIntent = new Intent(this,PowerballSimulatorService.class);
                serviceIntent.putExtra("shutdown","shutdown");
                PendingIntent pendingServiceShutdown = PendingIntent.getService(this,20,serviceIntent,0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = "Powerball";
                    String description = "Powerball Notification";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel("10101010101", name, importance);
                    channel.setDescription(description);
                    // Register the channel with the system; you can't change the importance
                    // or other notification behaviors after this
                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }
                Notification notification =
                        new NotificationCompat.Builder(this, "10101010101")
                                .setContentTitle(getText(R.string.powerball))
                                .setContentText(getText(R.string.simrunning))

                                .setSmallIcon(R.drawable.powerstar1)
                                .setContentIntent(pendingIntent)
                                .setTicker(getText(R.string.powerball))
                                .addAction(R.drawable.powerstar1,getText(R.string.stop),pendingServiceShutdown)
                                .build();
                startForeground(Constants.POWER_SIM_FOREGROUND,notification);
                running = true;
                counter = SharedPrefHelper.getLongPowerballCounter(this);
                data = SharedPrefHelper.getSimData(this,Constants.POWER_SIM);
                Log.d("powerservice", "data size: " + data.size());
                handler.postDelayed(serviceRunnable, 0);
                Log.d("powerservice","onstartcommand");
            }

        }

        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;
        //thread.interrupt();
        handler.removeCallbacks(serviceRunnable);
        SharedPrefHelper.setSimData(this,data,Constants.POWER_SIM);
        SharedPrefHelper.setLongPowerballCounter(this,counter);
        Log.d("powerservice","service destroyed");
    }

    public class PowerballSimulatiorServiceBinder extends Binder {
        public PowerballSimulatorService getService() {
            // Return this instance of LocalService so clients can call public methods
            return PowerballSimulatorService.this;
        }
    }
}

