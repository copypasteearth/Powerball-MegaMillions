/*
 * Author: John Rowan
 * Description: Winning Ticket class for storing the winning numbers and calculating results
 * Anyone may use this file or anything contained in this project for their own personal use.
 */
package powerball.apps.jacs.powerball

import java.io.Serializable
import java.util.*

class WinningTicket : Serializable {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as WinningTicket
        return date == that.date
    }

    override fun hashCode(): Int {
        return Objects.hash(date)
    }

    @JvmField
    var date: String? = null
    @JvmField
    var winningNumber: String? = null
    @JvmField
    var multiplier: String? = null
    fun calculateWin(ticket: String): String {
        // if(winningNumber.equals(ticket)){
        //     return "Jackpot!!!";
        // }
        val win = winningNumber!!.split(" ").toTypedArray()
        val yours = ticket.split(" ").toTypedArray()
        var powerballhit = false
        if (win[win.size - 1].toInt() == yours[yours.size - 1].toInt()) {
            powerballhit = true
        }
        var count = 0
        for (i in 0 until win.size - 1) {
            for (j in 0 until win.size - 1) {
                if (win[i].toInt() == yours[j].toInt()) {
                    count++
                }
            }
        }
        if (count == 5 && powerballhit) {
            return "Jackpot!!!"
        }
        if (count == 5) {
            return "1 Million winner!!"
        }
        if (count == 4 && powerballhit) {
            return "50,000 hit!!!"
        }
        if (count == 4) {
            return "$100 hit"
        }
        if (count == 3 && powerballhit) {
            return "$100 hit-"
        }
        if (count == 3) {
            return "$7 hit"
        }
        if (count == 2 && powerballhit) {
            return "$7 hit-"
        }
        if (count == 1 && powerballhit) {
            return "$4 hit"
        }
        return if (powerballhit) {
            "$4 hit-"
        } else "Nothing :("
    }
}