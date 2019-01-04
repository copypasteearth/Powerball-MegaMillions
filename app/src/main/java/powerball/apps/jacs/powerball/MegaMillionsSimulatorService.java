/*
 * Author: John Rowan
 * Description: service for simulating megamillions
 * Anyone may use this file or anything contained in this project for their own personal use.
 */

package powerball.apps.jacs.powerball;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MegaMillionsSimulatorService extends Service {
    public MegaMillionsSimulatorService() {
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
