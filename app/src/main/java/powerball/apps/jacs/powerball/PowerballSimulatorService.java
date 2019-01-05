/*
 * Author: John Rowan
 * Description: The service for running powerball simulator
 * Anyone may use this file or anything contained in this project for their own personal use.
 */

package powerball.apps.jacs.powerball;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.util.ArrayList;

public class PowerballSimulatorService extends Service {
    private final IBinder mBinder = new PowerballSimulatiorServiceBinder();
    private PowerballSimulatorListener listener;
    public static boolean running = false;
    public static boolean update = false;
    public ArrayList<SimulatorData> data;
    public long counter;
    public Thread thread;
    public FragmentActivity mainActivity;
    Handler handler = new Handler();
    Runnable serviceRunnable = new Runnable() {
        @Override
        public void run() {
            counter++;
            for(int i = 0;i<data.size();i++){
                data.get(i).plays = counter;

            }
           if(listener != null){
               listener.onLottoResult(data);
               Log.d("powerservice","fired listener");
           }
            handler.postDelayed(this, 1);
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
        running = true;
        counter = SharedPrefHelper.getLongPowerballCounter(this);
        data = SharedPrefHelper.getSimData(this,Constants.POWER_SIM);
        Log.d("powerservice", "data size: " + data.size());
        handler.postDelayed(serviceRunnable, 0);

        /*thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(counter >= 0 && !Thread.currentThread().isInterrupted()){
                    counter++;
                    for(int i = 0;i<data.size();i++){
                        data.get(i).plays = counter;

                    }
                   handler.post(serviceRunnable);
                }



            }
        });
        thread.start();*/
        Log.d("powerservice","onstartcommand");
        return super.onStartCommand(intent, flags, startId);
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

