/*
 * Author: John Rowan
 * Description:
 * Anyone may use this file or anything contained in this project for their own personal use.
 */
package powerball.apps.jacs.powerball

import java.util.*

interface PowerballSimulatorListener {
    fun onLottoResult(listData: ArrayList<SimulatorData>)
    fun stopLottoService()
}