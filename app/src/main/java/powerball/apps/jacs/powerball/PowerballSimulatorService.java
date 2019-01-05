/*
 * Author: John Rowan
 * Description: The service for running powerball simulator
 * Anyone may use this file or anything contained in this project for their own personal use.
 */

package powerball.apps.jacs.powerball;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;

public class PowerballSimulatorService extends Service {
    private final IBinder mBinder = new PowerballSimulatiorServiceBinder();
    private PowerballSimulatorListener listener;
    public static boolean running = false;
    public static boolean update = false;
    public ArrayList<SimulatorData> data;
    public PowerballSimulatorService() {
    }
    public void setPowerballSimulatorListener(PowerballSimulatorListener thelistener){
        listener = thelistener;
    }
    @Override
    public IBinder onBind(Intent intent) {
     return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        running = true;
        return super.onStartCommand(intent, flags, startId);
    }
    public class PowerballSimulatiorServiceBinder extends Binder {
        public PowerballSimulatorService getService() {
            // Return this instance of LocalService so clients can call public methods
            return PowerballSimulatorService.this;
        }
    }
}

