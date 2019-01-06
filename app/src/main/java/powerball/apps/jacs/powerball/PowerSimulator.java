/*
 * Author: John Rowan
 * Description: Fragment to view simulator data used in viewpager
 * Anyone may use this file or anything contained in this project for their own personal use.
 */

package powerball.apps.jacs.powerball;


import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PowerSimulator#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PowerSimulator extends Fragment{
public Button start,reset;
public LinearLayout linear;
public Context mContext;
public PowerballSimulatorListener listener;
public ArrayList<SimulatorData> data = new ArrayList<>();
public PowerballSimulatorService mPlayService;
public ArrayList<View> viewList = new ArrayList<>();
public boolean binding = false;
    private ServiceConnection mPlayServiceConnection;

    public PowerSimulator() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment PowerSimulator.
     */

    public static PowerSimulator newInstance() {
        PowerSimulator fragment = new PowerSimulator();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mPlayService != null)
        mPlayService.setPowerballSimulatorListener(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mPlayService != null)
        mPlayService.setPowerballSimulatorListener(listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       if(binding)
        mContext.unbindService(mPlayServiceConnection);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_power_simulator, container, false);
        start = (Button)view.findViewById(R.id.start);
        if(PowerballSimulatorService.running)
            start.setText(R.string.stop);
        reset = (Button)view.findViewById(R.id.reset);
        linear = (LinearLayout)view.findViewById(R.id.powersimlinear);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<MyTicket> tickets = SharedPrefHelper.getMyTickets(mContext,Constants.POWER_TICKETS);
        if(PowerballSimulatorService.running){
            data = PowerballSimulatorService.data;
        }else{
            data = SharedPrefHelper.getSimData(mContext,Constants.POWER_SIM);
        }

       Log.d("powerservice","fragment size: " + data);
        long powerSimCounter = SharedPrefHelper.getLongPowerballCounter(mContext);
        if(tickets.size() > 0){
            boolean update = false;
            for(int x = 0;x < data.size();x++){
                boolean inside = false;
                for(int y = 0;y < tickets.size();y++){
                    if(data.get(x).number.equals(tickets.get(y).ticket)){
                        inside = true;

                    }
                }
                if(!inside){
                    data.remove(x);
                    update = true;
                }
            }
            //checking if each ticket has a corresponding simulatorData and if not making one
            for(int i = 0;i < tickets.size();i++){
                boolean contained = false;
                for(int j = 0;j < data.size();j++){
                    if(tickets.get(i).ticket.equals(data.get(j).number)){
                        contained = true;

                    }
                }
                if(!contained){
                    SimulatorData simDat = new SimulatorData();
                    simDat.number = tickets.get(i).ticket;
                    if(PowerballSimulatorService.running){
                        simDat.ofsetPlays = PowerballSimulatorService.counter;
                    }else{
                        simDat.ofsetPlays = powerSimCounter;
                    }

                    data.add(simDat);
                    update = true;
                }

            }

            if(update){
                SharedPrefHelper.setSimData(mContext,data,Constants.POWER_SIM);
                if(PowerballSimulatorService.running){
                    SharedPrefHelper.setLongPowerballCounter(mContext,PowerballSimulatorService.counter);
                    PowerballSimulatorService.update = true;
                }

            }
            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(getString(R.string.messageDialog));
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            resetButtonWork();
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog1 = builder.create();
                    dialog1.setTitle(R.string.resetyes);
                    dialog1.show();
                }
            });
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for(int q = 0;q < data.size();q++){
                SimulatorData singleData = data.get(q);
                View view = inflater.inflate(R.layout.power_sim, null);
                View include = view.findViewById(R.id.includesim);
                TextView ball1 = (TextView) view.findViewById(R.id.ball1);
                TextView ball2 = (TextView) view.findViewById(R.id.ball2);
                TextView ball3 = (TextView) view.findViewById(R.id.ball3);
                TextView ball4 = (TextView) view.findViewById(R.id.ball4);
                TextView ball5 = (TextView) view.findViewById(R.id.ball5);
                TextView ball6 = (TextView) view.findViewById(R.id.ball6);
                TextView win = (TextView)view.findViewById(R.id.win);
                win.setText("");
                TextView[] images = {ball1,ball2,ball3,ball4,ball5,ball6};
                String num = singleData.number;
                String[] split = num.split(" ");
                for(int j = 0;j < split.length;j++){
                    images[j].setText(split[j]);
                }
               setViews(singleData,view);
                viewList.add(view);
                linear.addView(view);
            }
           listener = new PowerballSimulatorListener() {
               @Override
               public void onLottoResult(ArrayList<SimulatorData> listData) {
                   Log.d("powerservice","onlottoresult" + listData.size());
                   for(int i = 0;i < listData.size();i++){
                       SimulatorData singleData = listData.get(i);
                       View view = viewList.get(i);
                      setViews(singleData,view);
                   }
               }

               @Override
               public void stopLottoService() {
                   Intent intent = new Intent(mContext,PowerballSimulatorService.class);
                   mContext.unbindService(mPlayServiceConnection);
                   binding = false;
                   mContext.stopService(intent);
                   start.setText(R.string.start);
               }
           };
            mPlayServiceConnection = new ServiceConnection() {

                @Override
                public void onServiceConnected(ComponentName className, IBinder service) {
                    PowerballSimulatorService.PowerballSimulatorServiceBinder binder = (PowerballSimulatorService.PowerballSimulatorServiceBinder) service;
                    mPlayService = binder.getService();
                    mPlayService.setPowerballSimulatorListener(listener);
                   // mPlayService.setMainActivity(getActivity());
                    Log.d("powerservice","service binded");
                }

                @Override
                public void onServiceDisconnected(ComponentName className) {
                    Log.d("powerservice","service unbinded");
                }

            };
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(PowerballSimulatorService.running){
                        Intent intent = new Intent(mContext,PowerballSimulatorService.class);
                        mContext.unbindService(mPlayServiceConnection);
                        binding = false;
                        mContext.stopService(intent);
                        start.setText(R.string.start);
                    }else{
                        Intent intent = new Intent(mContext,PowerballSimulatorService.class);
                        if(Build.VERSION.SDK_INT >25){
                            mContext.startForegroundService(intent);
                        }else{
                            mContext.startService(intent);
                        }


                        mContext.bindService(intent,mPlayServiceConnection,Context.BIND_AUTO_CREATE);
                        binding = true;
                        start.setText(R.string.stop);
                    }
                }
            });
            if(PowerballSimulatorService.running){
                Intent intent = new Intent(mContext,PowerballSimulatorService.class);
                mContext.bindService(intent,mPlayServiceConnection,Context.BIND_AUTO_CREATE);
                binding = true;
            }
        }else{
            start.setEnabled(false);
            reset.setEnabled(false);
        }
    }
    public void setViews(SimulatorData singleData,View view){
        //set the views details from the simulator data
        String years = getString(R.string.years);
        String weeks = getString(R.string.weeks);
        String average = getString(R.string.average);
        String hits = getString(R.string.hits);
        String minimum = getString(R.string.minimum);
        TextView totalPlays = (TextView)view.findViewById(R.id.totalplays);
        totalPlays.setText(getString(R.string.totalplays) + ": " + singleData.plays + " -- " + singleData.getDays(singleData.plays,years,weeks));
        TextView jackPotHit = (TextView)view.findViewById(R.id.jackpothit);
        jackPotHit.setText(hits + ": " + singleData.jackpotHits);
        TextView jackPotAvg = (TextView)view.findViewById(R.id.jackpotavg);
        jackPotAvg.setText(average + ": " + singleData.jackpotAvg + " -- " + singleData.getDays(singleData.jackpotAvg,years,weeks));
        TextView jackPotMin = (TextView)view.findViewById(R.id.jackpotmin);
        jackPotMin.setText(minimum + ": " + singleData.jackpotMin + " -- " + singleData.getDays(singleData.jackpotMin,years,weeks));
        TextView ball5Hit = (TextView)view.findViewById(R.id.ball5hit);
        ball5Hit.setText(hits + ": " + singleData.white5Hits);
        TextView ball5Avg = (TextView)view.findViewById(R.id.ball5avg);
        ball5Avg.setText(average + ": " + singleData.white5Avg + " -- " + singleData.getDays(singleData.white5Avg,years,weeks));
        TextView ball5Min = (TextView)view.findViewById(R.id.ball5min);
        ball5Min.setText(minimum + ": " + singleData.white5Min + " -- " + singleData.getDays(singleData.white5Min,years,weeks));
        TextView ball4PowHit = (TextView)view.findViewById(R.id.ball4powerthit);
        ball4PowHit.setText(hits + ": " + singleData.white4PowHits);
        TextView ball4PowAvg = (TextView)view.findViewById(R.id.ball4poweravg);
        ball4PowAvg.setText(average + ": " + singleData.white4PowAvg + " -- " + singleData.getDays(singleData.white4PowAvg,years,weeks));
        TextView ball4PowMin = (TextView)view.findViewById(R.id.ball4powermin);
        ball4PowMin.setText(minimum + ": " + singleData.white4PowMin + " -- " + singleData.getDays(singleData.white4PowMin,years,weeks));
        TextView ball4Hit = (TextView)view.findViewById(R.id.ball4hit);
        ball4Hit.setText(hits + ": " + singleData.white4Hits);
        TextView ball4Avg = (TextView)view.findViewById(R.id.ball4avg);
        ball4Avg.setText(average + ": " + singleData.white4Avg + " -- " + singleData.getDays(singleData.white4Avg,years,weeks));
        TextView ball4Min = (TextView)view.findViewById(R.id.ball4min);
        ball4Min.setText(minimum + ": " + singleData.white4Min + " -- " + singleData.getDays(singleData.white4Min,years,weeks));
        TextView ball3PowHit = (TextView)view.findViewById(R.id.ball3powerhit);
        ball3PowHit.setText(hits + ": " + singleData.white3PowHits);
        TextView ball3PowAvg = (TextView)view.findViewById(R.id.ball3poweravg);
        ball3PowAvg.setText(average + ": " + singleData.white3PowAvg + " -- " + singleData.getDays(singleData.white3PowAvg,years,weeks));
        TextView ball3PowMin = (TextView)view.findViewById(R.id.ball3powermin);
        ball3PowMin.setText(minimum + ": " + singleData.white3PowMin + " -- " + singleData.getDays(singleData.white3PowMin,years,weeks));
        TextView ball3Hit = (TextView)view.findViewById(R.id.ball3hit);
        ball3Hit.setText(hits + ": " + singleData.white3Hits);
        TextView ball3Avg = (TextView)view.findViewById(R.id.ball3avg);
        ball3Avg.setText(average + ": " + singleData.white3Avg + " -- " + singleData.getDays(singleData.white3Avg,years,weeks));
        TextView ball3Min = (TextView)view.findViewById(R.id.ball3min);
        ball3Min.setText(minimum + ": " + singleData.white3Min + " -- " + singleData.getDays(singleData.white3Min,years,weeks));
        TextView ball2PowHit = (TextView)view.findViewById(R.id.ball2powerhit);
        ball2PowHit.setText(hits + ": " + singleData.white2PowHits);
        TextView ball2PowAvg = (TextView)view.findViewById(R.id.ball2poweravg);
        ball2PowAvg.setText(average + ": " + singleData.white2PowAvg + " -- " + singleData.getDays(singleData.white2PowAvg,years,weeks));
        TextView ball2PowMin = (TextView)view.findViewById(R.id.ball2powermin);
        ball2PowMin.setText(minimum + ": " + singleData.white2PowMin + " -- " + singleData.getDays(singleData.white2PowMin,years,weeks));
        TextView ball1PowHit = (TextView)view.findViewById(R.id.ball1powerhit);
        ball1PowHit.setText(hits + ": " + singleData.white1PowHits);
        TextView ball1PowAvg = (TextView)view.findViewById(R.id.ball1poweravg);
        ball1PowAvg.setText(average + ": " + singleData.white1PowAvg + " -- " + singleData.getDays(singleData.white1PowAvg,years,weeks));
        TextView ball1PowMin = (TextView)view.findViewById(R.id.ball1powermin);
        ball1PowMin.setText(minimum + ": " + singleData.white1PowMin + " -- " + singleData.getDays(singleData.white1PowMin,years,weeks));
        TextView powerHit = (TextView)view.findViewById(R.id.powerhit);
        powerHit.setText(hits + ": " + singleData.nowhitePowHits);
        TextView powerAvg = (TextView)view.findViewById(R.id.poweravg);
        powerAvg.setText(average + ": " + singleData.nowhitePowAvg + " -- " + singleData.getDays(singleData.nowhitePowAvg,years,weeks));
        TextView powerMin = (TextView)view.findViewById(R.id.powermin);
        powerMin.setText(minimum + ": " + singleData.nowhitePowMin + " -- " + singleData.getDays(singleData.nowhitePowMin,years,weeks));


    }
    public void resetButtonWork(){
        data.clear();
        SharedPrefHelper.setSimData(mContext,data,Constants.POWER_SIM);
        SharedPrefHelper.setLongPowerballCounter(mContext,0);
        ArrayList<MyTicket> tickets = SharedPrefHelper.getMyTickets(mContext,Constants.POWER_TICKETS);
        data = SharedPrefHelper.getSimData(mContext,Constants.POWER_SIM);
        long powerSimCounter = SharedPrefHelper.getLongPowerballCounter(mContext);
        if(tickets.size() > 0){
            boolean update = false;
            for(int x = 0;x < data.size();x++){
                boolean inside = false;
                for(int y = 0;y < tickets.size();y++){
                    if(data.get(x).number.equals(tickets.get(y).ticket)){
                        inside = true;
                        update = true;
                    }
                }
                if(!inside){
                    data.remove(x);
                }
            }
            //checking if each ticket has a corresponding simulatorData and if not making one
            for(int i = 0;i < tickets.size();i++){
                boolean contained = false;
                for(int j = 0;j < data.size();j++){
                    if(tickets.get(i).ticket.equals(data.get(j).number)){
                        contained = true;

                    }
                }
                if(!contained){
                    SimulatorData simDat = new SimulatorData();
                    simDat.number = tickets.get(i).ticket;
                    simDat.ofsetPlays = powerSimCounter;
                    data.add(simDat);
                    update = true;
                }

            }

            if(update){
                SharedPrefHelper.setSimData(mContext,data,Constants.POWER_SIM);
                if(PowerballSimulatorService.running)
                    PowerballSimulatorService.update = true;
            }
            linear.removeAllViews();
            viewList.clear();
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for(int q = 0;q < data.size();q++){
                View view = inflater.inflate(R.layout.power_sim, null);
                View include = view.findViewById(R.id.includesim);
                TextView ball1 = (TextView) view.findViewById(R.id.ball1);
                TextView ball2 = (TextView) view.findViewById(R.id.ball2);
                TextView ball3 = (TextView) view.findViewById(R.id.ball3);
                TextView ball4 = (TextView) view.findViewById(R.id.ball4);
                TextView ball5 = (TextView) view.findViewById(R.id.ball5);
                TextView ball6 = (TextView) view.findViewById(R.id.ball6);
                TextView win = (TextView)view.findViewById(R.id.win);
                win.setText("");
                TextView[] images = {ball1,ball2,ball3,ball4,ball5,ball6};
                String num = data.get(q).number;
                String[] split = num.split(" ");
                for(int j = 0;j < split.length;j++){
                    images[j].setText(split[j]);
                }
                viewList.add(view);
                linear.addView(view);
            }


        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


}
