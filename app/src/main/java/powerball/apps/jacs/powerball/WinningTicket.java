/*
 * Author: John Rowan
 * Description: Winning Ticket class for storing the winning numbers and calculating results
 * Anyone may use this file or anything contained in this project for their own personal use.
 */

package powerball.apps.jacs.powerball;

import java.io.Serializable;
import java.util.Objects;

public class WinningTicket implements Serializable {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WinningTicket that = (WinningTicket) o;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {

        return Objects.hash(date);
    }

    String date;
    String winningNumber;
    String multiplier;

    public WinningTicket(){

    }
    public String calculateWin(String ticket){
       // if(winningNumber.equals(ticket)){
       //     return "Jackpot!!!";
       // }
        String[] win = winningNumber.split(",");
        String[] yours = ticket.split(" ");
        boolean powerballhit = false;
        if(Integer.parseInt(win[win.length - 1]) == Integer.parseInt(yours[yours.length - 1])){
            powerballhit = true;
        }
        int count = 0;
        for(int i = 0;i < win.length - 1;i++){
            for(int j = 0;j < win.length - 1;j++){
                if(Integer.parseInt(win[i]) == Integer.parseInt(yours[j])){
                    count++;
                }
            }

        }
        if(count == 5 && powerballhit){
            return "Jackpot!!!";
        }
        if(count == 5){
            return "1 Million winner!!";
        }
        if(count == 4 && powerballhit){
            return "50,000 hit!!!";
        }
        if(count == 4){
            return "$100 hit";
        }
        if(count == 3 && powerballhit){
            return "$100 hit-";
        }
        if(count == 3){
            return "$7 hit";
        }
        if(count == 2 && powerballhit){
            return "$7 hit-";
        }
        if(count == 1 && powerballhit){
            return "$4 hit";
        }
        if(powerballhit){
            return "$4 hit-";
        }
        return "Nothing :(";

    }
}
