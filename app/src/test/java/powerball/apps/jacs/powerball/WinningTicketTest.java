/*
 * Author: John Rowan
 * Description:
 * Anyone may use this file or anything contained in this project for their own personal use.
 */

package powerball.apps.jacs.powerball;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class WinningTicketTest {
    @Test
    public void makeClass(){
        WinningTicket ticket = new WinningTicket();
        assertNotNull(ticket);
    }
    @Test
    public void makeClassAndAddMembers(){
        String date = new Date().toString();
        String mult = "none";
        String winningNumber = "1 2 3 4 5 6";
        WinningTicket ticket = new WinningTicket();
        ticket.date = date;
        ticket.multiplier = mult;
        ticket.winningNumber = winningNumber;
        assertEquals(date,ticket.date);
        assertEquals(mult,ticket.multiplier);
        assertEquals(winningNumber,ticket.winningNumber);
    }
    @Test
    public void testCalculateWinMethod(){
        WinningTicket ticket = new WinningTicket();
        ticket.winningNumber = "1,2,3,4,5,6";
        assertEquals("Jackpot!!!",ticket.calculateWin("1 2 3 4 5 6"));
        assertEquals("1 Million winner!!",ticket.calculateWin("1 2 3 4 5 7"));
        assertEquals("50,000 hit!!!",ticket.calculateWin("1 2 3 4 8 6"));
        assertEquals("$100 hit",ticket.calculateWin("1 2 3 4 9 8"));
        assertEquals("$100 hit-",ticket.calculateWin("1 2 3 9 8 6"));
        assertEquals("$7 hit",ticket.calculateWin("1 2 3 12 13 14"));
        assertEquals("$7 hit-",ticket.calculateWin("1 2 12 13 14 6"));
        assertEquals("$4 hit",ticket.calculateWin("1 12 13 14 15 6"));
        assertEquals("$4 hit-",ticket.calculateWin("11 12 13 14 15 6"));
        assertEquals("Nothing :(",ticket.calculateWin("11 12 13 14 15 16"));
    }
    @Test
    public void testCalculateWinMethodWithLeadingZero(){
        WinningTicket ticket = new WinningTicket();
        ticket.winningNumber = "01,02,03,04,05,06";
        assertEquals("Jackpot!!!",ticket.calculateWin("1 2 3 4 5 6"));
        assertEquals("1 Million winner!!",ticket.calculateWin("1 2 3 4 5 7"));
        assertEquals("50,000 hit!!!",ticket.calculateWin("1 2 3 4 8 6"));
        assertEquals("$100 hit",ticket.calculateWin("1 2 3 4 9 8"));
        assertEquals("$100 hit-",ticket.calculateWin("1 2 3 9 8 6"));
        assertEquals("$7 hit",ticket.calculateWin("1 2 3 12 13 14"));
        assertEquals("$7 hit-",ticket.calculateWin("1 2 12 13 14 6"));
        assertEquals("$4 hit",ticket.calculateWin("1 12 13 14 15 6"));
        assertEquals("$4 hit-",ticket.calculateWin("11 12 13 14 15 6"));
        assertEquals("Nothing :(",ticket.calculateWin("11 12 13 14 15 16"));
    }
    @Test
    public void testEqualAndHashCodeMethods(){
        String date = new Date().toString();
        String mult = "none";
        String winningNumber = "1 2 3 4 5 6";
        WinningTicket ticket = new WinningTicket();
        ticket.date = date;
        ticket.multiplier = mult;
        ticket.winningNumber = winningNumber;
        WinningTicket ticket2 = new WinningTicket();
        ticket2.date = date;
        ticket2.multiplier = mult;
        ticket2.winningNumber = winningNumber;
        assertTrue(ticket.equals(ticket2));
        assertTrue(ticket.hashCode() == ticket2.hashCode());

    }
}
