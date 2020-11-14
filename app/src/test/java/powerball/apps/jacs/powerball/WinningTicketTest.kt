/*
 * Author: John Rowan
 * Description:
 * Anyone may use this file or anything contained in this project for their own personal use.
 */
package powerball.apps.jacs.powerball

import org.junit.Assert
import org.junit.Test
import java.util.*

class WinningTicketTest {
    @Test
    fun makeClass() {
        val ticket = WinningTicket()
        Assert.assertNotNull(ticket)
    }

    @Test
    fun makeClassAndAddMembers() {
        val date = Date().toString()
        val mult = "none"
        val winningNumber = "1 2 3 4 5 6"
        val ticket = WinningTicket()
        ticket.date = date
        ticket.multiplier = mult
        ticket.winningNumber = winningNumber
        Assert.assertEquals(date, ticket.date)
        Assert.assertEquals(mult, ticket.multiplier)
        Assert.assertEquals(winningNumber, ticket.winningNumber)
    }

    @Test
    fun testCalculateWinMethod() {
        val ticket = WinningTicket()
        ticket.winningNumber = "1 2 3 4 5 6"
        Assert.assertEquals("Jackpot!!!", ticket.calculateWin("1 2 3 4 5 6"))
        Assert.assertEquals("1 Million winner!!", ticket.calculateWin("1 2 3 4 5 7"))
        Assert.assertEquals("50,000 hit!!!", ticket.calculateWin("1 2 3 4 8 6"))
        Assert.assertEquals("$100 hit", ticket.calculateWin("1 2 3 4 9 8"))
        Assert.assertEquals("$100 hit-", ticket.calculateWin("1 2 3 9 8 6"))
        Assert.assertEquals("$7 hit", ticket.calculateWin("1 2 3 12 13 14"))
        Assert.assertEquals("$7 hit-", ticket.calculateWin("1 2 12 13 14 6"))
        Assert.assertEquals("$4 hit", ticket.calculateWin("1 12 13 14 15 6"))
        Assert.assertEquals("$4 hit-", ticket.calculateWin("11 12 13 14 15 6"))
        Assert.assertEquals("Nothing :(", ticket.calculateWin("11 12 13 14 15 16"))
    }

    @Test
    fun testCalculateWinMethodWithLeadingZero() {
        val ticket = WinningTicket()
        ticket.winningNumber = "01 02 03 04 05 06"
        Assert.assertEquals("Jackpot!!!", ticket.calculateWin("1 2 3 4 5 6"))
        Assert.assertEquals("1 Million winner!!", ticket.calculateWin("1 2 3 4 5 7"))
        Assert.assertEquals("50,000 hit!!!", ticket.calculateWin("1 2 3 4 8 6"))
        Assert.assertEquals("$100 hit", ticket.calculateWin("1 2 3 4 9 8"))
        Assert.assertEquals("$100 hit-", ticket.calculateWin("1 2 3 9 8 6"))
        Assert.assertEquals("$7 hit", ticket.calculateWin("1 2 3 12 13 14"))
        Assert.assertEquals("$7 hit-", ticket.calculateWin("1 2 12 13 14 6"))
        Assert.assertEquals("$4 hit", ticket.calculateWin("1 12 13 14 15 6"))
        Assert.assertEquals("$4 hit-", ticket.calculateWin("11 12 13 14 15 6"))
        Assert.assertEquals("Nothing :(", ticket.calculateWin("11 12 13 14 15 16"))
    }

    @Test
    fun testEqualAndHashCodeMethods() {
        val date = Date().toString()
        val mult = "none"
        val winningNumber = "1 2 3 4 5 6"
        val ticket = WinningTicket()
        ticket.date = date
        ticket.multiplier = mult
        ticket.winningNumber = winningNumber
        val ticket2 = WinningTicket()
        ticket2.date = date
        ticket2.multiplier = mult
        ticket2.winningNumber = winningNumber
        Assert.assertTrue(ticket.equals(ticket2))
        Assert.assertTrue(ticket.hashCode() == ticket2.hashCode())
    }
}