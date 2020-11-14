/*
 * Author: John Rowan
 * Description: Data for each ticket used in simulation
 * Anyone may use this file or anything contained in this project for their own personal use.
 */
package powerball.apps.jacs.powerball

class SimulatorData {
    var number: String? = null
    var plays: Long = 0
    var ofsetPlays: Long = 0
    var jackpotHits: Long = 0
    var jackpotAvg: Long = 0
    var jackpotMin: Long = 0
    var white5Hits: Long = 0
    var white5Avg: Long = 0
    var white5Min: Long = 0
    var white4PowHits: Long = 0
    var white4PowAvg: Long = 0
    var white4PowMin: Long = 0
    var white4Hits: Long = 0
    var white4Avg: Long = 0
    var white4Min: Long = 0
    var white3PowHits: Long = 0
    var white3PowAvg: Long = 0
    var white3PowMin: Long = 0
    var white3Hits: Long = 0
    var white3Avg: Long = 0
    var white3Min: Long = 0
    var white2PowHits: Long = 0
    var white2PowAvg: Long = 0
    var white2PowMin: Long = 0
    var white1PowHits: Long = 0
    var white1PowAvg: Long = 0
    var white1PowMin: Long = 0
    var nowhitePowHits: Long = 0
    var nowhitePowAvg: Long = 0
    var nowhitePowMin: Long = 0
    var counter = LongArray(9)
    fun incrementCounter() {
        for (i in counter.indices) {
            counter[i]++
        }
    }

    fun resetCounter(x: Int) {
        counter[x] = 0
    }

    fun getCounter(x: Int): Long {
        return counter[x]
    }

    fun getDays(num: Long, yearString: String, weekString: String): String {
        val remainderPlays = num % 104
        val years = num / 104
        val weeks = remainderPlays / 2
        return "$yearString: $years, $weekString: $weeks"
    }
}