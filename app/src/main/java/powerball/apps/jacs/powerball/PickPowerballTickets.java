/*
 * Author: John Rowan
 * Description: fragment for picking powerball tickets and saving them to your device
 * Anyone may use this file or anything contained in this project for their own personal use.
 */

package powerball.apps.jacs.powerball;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class PickPowerballTickets extends Fragment {
    public TextView powerBall;
    public ArrayList<MyTicket> powerNumbers;
    public RecyclerView power;
    public MyTicketAdapter powerAdapter;
    public PickPowerballTickets() {
        // Required empty public constructor
    }


    public static PickPowerballTickets newInstance(String param1, String param2) {
        PickPowerballTickets fragment = new PickPowerballTickets();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pick_powerball_tickets, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onStart() {
        super.onStart();
        instantiateViews();
        getMyTickets();
        populateRecyclerViews();
        setClickListeners();
    }
    public void instantiateViews(){
        View power1 = (View)getView().findViewById(R.id.include);
        powerBall = (TextView)power1.findViewById(R.id.win);
        power = (RecyclerView)getView().findViewById(R.id.powerRecycle);
    }
    public void getMyTickets(){
        powerNumbers = SharedPrefHelper.getMyTickets(getActivity(),"powerball");
    }
    public void populateRecyclerViews(){
        //power.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        power.setLayoutManager(llm);
        powerAdapter = new MyTicketAdapter(this,powerNumbers,"powerball");
        power.setAdapter(powerAdapter);
        powerAdapter.setOnRemoveListener(new OnRemoveListener() {
            @Override
            public void onRemove(int position) {

                powerNumbers.remove(position);
                Log.d("adapter",powerNumbers.size()+"");
                //power.removeViewAt(position);
                power.removeAllViews();
                //powerAdapter.notifyItemRemoved(position);
                //powerAdapter.notifyItemRangeChanged(position, powerNumbers.size());
                powerAdapter.notifyDataSetChanged();
                SharedPrefHelper.setMyTickets(getActivity(),powerNumbers,"powerball");
            }
        });

    }
    public void createPickDialog(){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.ticket_selector, null);
        final NumberPicker one = (NumberPicker)dialogView.findViewById(R.id.one);
        one.setMinValue(1);
        one.setMaxValue(69);
        final NumberPicker two = (NumberPicker)dialogView.findViewById(R.id.two);
        two.setMinValue(1);
        two.setMaxValue(69);
        final NumberPicker three = (NumberPicker)dialogView.findViewById(R.id.three);
        three.setMinValue(1);
        three.setMaxValue(69);
        final NumberPicker four = (NumberPicker)dialogView.findViewById(R.id.four);
        four.setMinValue(1);
        four.setMaxValue(69);
        final NumberPicker five = (NumberPicker)dialogView.findViewById(R.id.five);
        five.setMinValue(1);
        five.setMaxValue(69);
        final NumberPicker six = (NumberPicker)dialogView.findViewById(R.id.six);
        six.setMinValue(1);
        six.setMaxValue(26);
        final RadioGroup group = (RadioGroup)dialogView.findViewById(R.id.group);

        dialogBuilder.setView(dialogView);




        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //powerNumbers = SharedPrefHelper.getMyTickets(getActivity(),"powerball");
                //powerNumbers = powerAdapter.mDataset;
                Integer[] numbers = {one.getValue(),two.getValue(),three.getValue(),four.getValue(),five.getValue()};
                Arrays.sort(numbers);
                Set<Integer> set = new HashSet<Integer>(Arrays.asList(numbers));
                if(set.size() == numbers.length) {
                    String ticket = numbers[0] + " " + numbers[1] + " " + numbers[2] +
                            " " + numbers[3] + " " + numbers[4] + " " + six.getValue();
                    int mul = group.getCheckedRadioButtonId();
                    RadioButton but = dialogView.findViewById(mul);
                    String multi = but.getText().toString();
                    boolean ticketMultiplier = false;
                    if(multi.equals("Yes")){
                        ticketMultiplier = true;
                    }
                    MyTicket ticket1 = new MyTicket();
                    ticket1.ticket = ticket;
                    ticket1.multi = ticketMultiplier;
                    powerNumbers.add(ticket1);

                    Log.d("adapter", powerNumbers.size() + "");
                    powerAdapter.notifyItemInserted(powerNumbers.size() - 1);
                    SharedPrefHelper.setMyTickets(getActivity(), powerNumbers, "powerball");
                    alertDialog.dismiss();
                }else{
                    final AlertDialog.Builder tryagain = new AlertDialog.Builder(getActivity());
                    tryagain.setMessage(getResources().getString(R.string.alertticket));
                    final AlertDialog tryagaindialog = tryagain.create();
                    tryagaindialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            createPickDialog();
                        }
                    });
                    tryagaindialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tryagaindialog.dismiss();
                        }
                    });
                    tryagaindialog.show();
                }

            }
        });
        alertDialog.show();
    }
    public void setClickListeners(){
        powerBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               createPickDialog();
            }
        });

    }
}
