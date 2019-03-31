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
import java.util.Collections;
import java.util.Random;

public class PowerballSimulatorService extends Service {
    private final IBinder mBinder = new PowerballSimulatorServiceBinder();
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
            Random random = new Random();
            ArrayList<Integer> nums = new ArrayList<>();
            while(nums.size() < 6){
                Integer num = random.nextInt(69) + 1;
                if(!nums.contains(num)){
                    nums.add(num);
                }
            }
            Integer power = random.nextInt(26) + 1;
            Collections.sort(nums);
            String number = nums.get(0) + "," + nums.get(1) + "," + nums.get(2) + "," + nums.get(3) + "," + nums.get(4) + "," + nums.get(5) + "," + power;

            //Log.d("powerballnumber","number : " + number);
            WinningTicket ticket = new WinningTicket();
            ticket.winningNumber = number;
            counter++;
            //SharedPrefHelper.setLongPowerballCounter(PowerballSimulatorService.this,counter);
            for(int i = 0;i<data.size();i++){
                data.get(i).plays = counter - data.get(i).ofsetPlays;
                data.get(i).incrementCounter();
                String winning = ticket.calculateWin(data.get(i).number);
                if(winning.equals("Nothing :(")){
                    continue;
                }
                if(winning.equals("Jackpot!!!")){
                    data.get(i).jackpotHits++;
                    data.get(i).jackpotAvg = data.get(i).plays / data.get(i).jackpotHits;
                    if(data.get(i).jackpotMin <= 0){
                        data.get(i).jackpotMin = data.get(i).getCounter(0);
                        data.get(i).resetCounter(0);
                    }else{
                        if(data.get(i).getCounter(0) < data.get(i).jackpotMin){
                            data.get(i).jackpotMin = data.get(i).getCounter(0);
                            data.get(i).resetCounter(0);
                        }else{
                            data.get(i).resetCounter(0);
                        }
                    }
                }else if(winning.equals("1 Million winner!!")){
                    data.get(i).white5Hits++;
                    data.get(i).white5Avg = data.get(i).plays / data.get(i).white5Hits;
                    if(data.get(i).white5Min <= 0){
                        data.get(i).white5Min = data.get(i).getCounter(1);
                        data.get(i).resetCounter(1);
                    }else{
                        if(data.get(i).getCounter(1) < data.get(i).white5Min){
                            data.get(i).white5Min = data.get(i).getCounter(1);
                            data.get(i).resetCounter(1);
                        }else{
                            data.get(i).resetCounter(1);
                        }
                    }
                }else if(winning.equals("50,000 hit!!!")){
                    data.get(i).white4PowHits++;
                    data.get(i).white4PowAvg = data.get(i).plays / data.get(i).white4PowHits;
                    if(data.get(i).white4PowMin <= 0){
                        data.get(i).white4PowMin = data.get(i).getCounter(2);
                        data.get(i).resetCounter(2);
                    }else{
                        if(data.get(i).getCounter(2) < data.get(i).white4PowMin){
                            data.get(i).white4PowMin = data.get(i).getCounter(2);
                            data.get(i).resetCounter(2);
                        }else{
                            data.get(i).resetCounter(2);
                        }
                    }
                }else if(winning.equals("$100 hit")){
                    data.get(i).white4Hits++;
                    data.get(i).white4Avg = data.get(i).plays / data.get(i).white4Hits;
                    if(data.get(i).white4Min <= 0){
                        data.get(i).white4Min = data.get(i).getCounter(3);
                        data.get(i).resetCounter(3);
                    }else{
                        if(data.get(i).getCounter(3) < data.get(i).white4Min){
                            data.get(i).white4Min = data.get(i).getCounter(3);
                            data.get(i).resetCounter(3);
                        }else{
                            data.get(i).resetCounter(3);
                        }
                    }
                }else if(winning.equals("$100 hit-")){
                    data.get(i).white3PowHits++;
                    data.get(i).white3PowAvg = data.get(i).plays / data.get(i).white3PowHits;
                    if(data.get(i).white3PowMin <= 0){
                        data.get(i).white3PowMin = data.get(i).getCounter(4);
                        data.get(i).resetCounter(4);
                    }else{
                        if(data.get(i).getCounter(4) < data.get(i).white3PowMin){
                            data.get(i).white3PowMin = data.get(i).getCounter(4);
                            data.get(i).resetCounter(4);
                        }else{
                            data.get(i).resetCounter(4);
                        }
                    }
                }else if(winning.equals("$7 hit")){
                    data.get(i).white3Hits++;
                    data.get(i).white3Avg = data.get(i).plays / data.get(i).white3Hits;
                    if(data.get(i).white3Min <= 0){
                        data.get(i).white3Min = data.get(i).getCounter(5);
                        data.get(i).resetCounter(5);
                    }else{
                        if(data.get(i).getCounter(5) < data.get(i).white3Min){
                            data.get(i).white3Min = data.get(i).getCounter(5);
                            data.get(i).resetCounter(5);
                        }else{
                            data.get(i).resetCounter(5);
                        }
                    }
                }else if(winning.equals("$7 hit-")){
                    data.get(i).white2PowHits++;
                    data.get(i).white2PowAvg = data.get(i).plays / data.get(i).white2PowHits;
                    if(data.get(i).white2PowMin <= 0){
                        data.get(i).white2PowMin = data.get(i).getCounter(6);
                        data.get(i).resetCounter(6);
                    }else{
                        if(data.get(i).getCounter(6) < data.get(i).white2PowMin){
                            data.get(i).white2PowMin = data.get(i).getCounter(6);
                            data.get(i).resetCounter(6);
                        }else{
                            data.get(i).resetCounter(6);
                        }
                    }
                }else if(winning.equals("$4 hit")){
                    data.get(i).white1PowHits++;
                    data.get(i).white1PowAvg = data.get(i).plays / data.get(i).white1PowHits;
                    if(data.get(i).white1PowMin <= 0){
                        data.get(i).white1PowMin = data.get(i).getCounter(7);
                        data.get(i).resetCounter(7);
                    }else{
                        if(data.get(i).getCounter(7) < data.get(i).white1PowMin){
                            data.get(i).white1PowMin = data.get(i).getCounter(7);
                            data.get(i).resetCounter(7);
                        }else{
                            data.get(i).resetCounter(7);
                        }
                    }
                }else if(winning.equals("$4 hit-")){
                    data.get(i).nowhitePowHits++;
                    data.get(i).nowhitePowAvg = data.get(i).plays / data.get(i).nowhitePowHits;
                    if(data.get(i).nowhitePowMin <= 0){
                        data.get(i).nowhitePowMin = data.get(i).getCounter(8);
                        data.get(i).resetCounter(8);
                    }else{
                        if(data.get(i).getCounter(8) < data.get(i).nowhitePowMin){
                            data.get(i).nowhitePowMin = data.get(i).getCounter(8);
                            data.get(i).resetCounter(8);
                        }else{
                            data.get(i).resetCounter(8);
                        }
                    }
                }

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
               setUpForeground();
            }

        }else{
            setUpForeground();
        }

        return START_STICKY;

    }
    public void setUpForeground(){
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

    public class PowerballSimulatorServiceBinder extends Binder {
        public PowerballSimulatorService getService() {
            // Return this instance of LocalService so clients can call public methods
            return PowerballSimulatorService.this;
        }
    }
}

