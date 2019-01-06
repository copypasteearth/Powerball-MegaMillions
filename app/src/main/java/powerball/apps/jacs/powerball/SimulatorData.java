/*
 * Author: John Rowan
 * Description: Data for each ticket used in simulation
 * Anyone may use this file or anything contained in this project for their own personal use.
 */

package powerball.apps.jacs.powerball;


public class SimulatorData {
    public String number;
    public long plays;
    public long ofsetPlays;
    public long jackpotHits;
    public long jackpotAvg;
    public long jackpotMin;
    public long white5Hits;
    public long white5Avg;
    public long white5Min;
    public long white4PowHits;
    public long white4PowAvg;
    public long white4PowMin;
    public long white4Hits;
    public long white4Avg;
    public long white4Min;
    public long white3PowHits;
    public long white3PowAvg;
    public long white3PowMin;
    public long white3Hits;
    public long white3Avg;
    public long white3Min;
    public long white2PowHits;
    public long white2PowAvg;
    public long white2PowMin;
    public long white1PowHits;
    public long white1PowAvg;
    public long white1PowMin;
    public long nowhitePowHits;
    public long nowhitePowAvg;
    public long nowhitePowMin;
    public long[] counter = new long[9];
    public SimulatorData(){

    }
    public void incrementCounter(){
        for(int i = 0;i < counter.length;i++){
            counter[i]++;
        }
    }
    public void resetCounter(int x){
        counter[x] = 0;
    }
    public long getCounter(int x){
        return counter[x];
    }
    public String getDays(long num,String yearString,String weekString){
        long remainderPlays = num % 104;
        long years = num/104;
        long weeks = remainderPlays/2;
        return yearString + ": " + years + ", " + weekString + ": " + weeks;
    }

}
