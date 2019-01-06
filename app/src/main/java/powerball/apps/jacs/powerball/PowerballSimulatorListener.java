/*
 * Author: John Rowan
 * Description:
 * Anyone may use this file or anything contained in this project for their own personal use.
 */

package powerball.apps.jacs.powerball;

import java.util.ArrayList;

public interface PowerballSimulatorListener {
    void onLottoResult(ArrayList<SimulatorData> listData);
    void stopLottoService();
}
