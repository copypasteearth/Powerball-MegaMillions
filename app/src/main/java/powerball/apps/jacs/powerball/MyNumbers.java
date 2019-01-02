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


public class MyNumbers extends Fragment {
    public TextView powerBall,megaMillions;
    public ArrayList<MyTicket> powerNumbers,megaNumbers;
    public RecyclerView power,mega;
    public MyTicketAdapter powerAdapter,megaAdapter;
    public MyNumbers() {
        // Required empty public constructor
    }


    public static MyNumbers newInstance() {
        MyNumbers fragment = new MyNumbers();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_numbers, container, false);
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
        View mega1 = (View)getView().findViewById(R.id.include1);
        powerBall = (TextView)power1.findViewById(R.id.win);
        megaMillions = (TextView)mega1.findViewById(R.id.winm);
        power = (RecyclerView)getView().findViewById(R.id.powerRecycle);
        mega = (RecyclerView)getView().findViewById(R.id.megaRecycle);
    }
    public void getMyTickets(){
        powerNumbers = SharedPrefHelper.getMyTickets(getActivity(),"powerball");
        megaNumbers = SharedPrefHelper.getMyTickets(getActivity(),"megamillions");
    }
    public void setMyTickets(){
        SharedPrefHelper.setMyTickets(getActivity(),powerNumbers,"powerball");
        SharedPrefHelper.setMyTickets(getActivity(),megaNumbers,"megamillions");
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

        //mega.setHasFixedSize(true);
        LinearLayoutManager llm1 = new LinearLayoutManager(getActivity());
        mega.setLayoutManager(llm1);
        megaAdapter = new MyTicketAdapter(this,megaNumbers,"megamillions");

        mega.setAdapter(megaAdapter);
        megaAdapter.setOnRemoveListener(new OnRemoveListener() {
            @Override
            public void onRemove(int position) {
                megaNumbers.remove(position);
                mega.removeAllViews();
                megaAdapter.notifyDataSetChanged();
                SharedPrefHelper.setMyTickets(getActivity(),megaNumbers,"megamillions");
            }
        });
    }
    public void removeView(String str,int position){
        if(str.equals("powerball")){
            power.removeViewAt(position);
        }
    }
    public void setClickListeners(){
        powerBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        String ticket = one.getValue() + " " + two.getValue() + " " + three.getValue() +
                                " " + four.getValue() + " " + five.getValue() + " " + six.getValue();
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

                        Log.d("adapter",powerNumbers.size()+"");
                        powerAdapter.notifyItemInserted(powerNumbers.size() - 1);
                        SharedPrefHelper.setMyTickets(getActivity(),powerNumbers,"powerball");
                        alertDialog.dismiss();

                    }
                });
                alertDialog.show();
            }
        });
        megaMillions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                six.setBackgroundColor(Color.YELLOW);
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
                        //megaNumbers.clear();
                        //megaNumbers = SharedPrefHelper.getMyTickets(getActivity(),"megamillions");
                       // megaNumbers = new ArrayList<MyTicket>(megaAdapter.mDataset);
                        String ticket = one.getValue() + " " + two.getValue() + " " + three.getValue() +
                                " " + four.getValue() + " " + five.getValue() + " " + six.getValue();
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
                        megaNumbers.add(ticket1);
                        megaAdapter.notifyItemInserted(megaNumbers.size() - 1);
                        //megaAdapter.notifyDataSetChanged();
                        SharedPrefHelper.setMyTickets(getActivity(),megaNumbers,"megamillions");
                        alertDialog.dismiss();

                    }
                });
                alertDialog.show();
            }
        });
    }
}
