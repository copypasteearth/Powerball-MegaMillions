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


public class PickMegaMillionsTickets extends Fragment {
    public TextView megaMillions;
    public ArrayList<MyTicket> megaNumbers;
    public RecyclerView mega;
    public MyTicketAdapter megaAdapter;
    public PickMegaMillionsTickets() {
        // Required empty public constructor
    }


    public static PickMegaMillionsTickets newInstance(String param1, String param2) {
        PickMegaMillionsTickets fragment = new PickMegaMillionsTickets();

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
        return inflater.inflate(R.layout.fragment_pick_mega_millions_tickets, container, false);
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
        View mega1 = (View)getView().findViewById(R.id.include1);
        megaMillions = (TextView)mega1.findViewById(R.id.winm);
        mega = (RecyclerView)getView().findViewById(R.id.megaRecycle);
    }
    public void getMyTickets(){
        megaNumbers = SharedPrefHelper.getMyTickets(getActivity(),"megamillions");
    }
    public void setMyTickets(){
        SharedPrefHelper.setMyTickets(getActivity(),megaNumbers,"megamillions");
    }
    public void populateRecyclerViews(){

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

    public void setClickListeners(){
        megaMillions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              createPickDialog();
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
        one.setMaxValue(70);
        final NumberPicker two = (NumberPicker)dialogView.findViewById(R.id.two);
        two.setMinValue(1);
        two.setMaxValue(70);
        final NumberPicker three = (NumberPicker)dialogView.findViewById(R.id.three);
        three.setMinValue(1);
        three.setMaxValue(70);
        final NumberPicker four = (NumberPicker)dialogView.findViewById(R.id.four);
        four.setMinValue(1);
        four.setMaxValue(70);
        final NumberPicker five = (NumberPicker)dialogView.findViewById(R.id.five);
        five.setMinValue(1);
        five.setMaxValue(70);
        final NumberPicker six = (NumberPicker)dialogView.findViewById(R.id.six);
        six.setMinValue(1);
        six.setMaxValue(25);
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
                Integer[] numbers = {one.getValue(),two.getValue(),three.getValue(),four.getValue(),five.getValue()};
                Arrays.sort(numbers);
                Set<Integer> set = new HashSet<Integer>(Arrays.asList(numbers));
                if(set.size() == numbers.length){
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
                    megaNumbers.add(ticket1);
                    megaAdapter.notifyItemInserted(megaNumbers.size() - 1);
                    //megaAdapter.notifyDataSetChanged();
                    SharedPrefHelper.setMyTickets(getActivity(),megaNumbers,"megamillions");
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
}
