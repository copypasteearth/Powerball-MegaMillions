/*
 * Author: John Rowan
 * Description: The service for running powerball simulator
 * Anyone may use this file or anything contained in this project for their own personal use.
 */

package powerball.apps.jacs.powerball;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PowerballSimulatorService extends Service {
    public PowerballSimulatorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
