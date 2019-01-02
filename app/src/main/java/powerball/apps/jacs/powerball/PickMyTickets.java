package powerball.apps.jacs.powerball;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class PickMyTickets extends Fragment {

public ViewPager viewPager;
public ViewPagerAdapter adapter;
public TabLayout tabLayout;
    public PickMyTickets() {
        // Required empty public constructor
    }



    public static PickMyTickets newInstance() {
        PickMyTickets fragment = new PickMyTickets();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new PickPowerballTickets(), getResources().getString(R.string.powerball));
        adapter.addFragment(new PickMegaMillionsTickets(), getResources().getString(R.string.megamillions));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pick_my_tickets, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return view;
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
        Log.d("timer","starting over again");

    }
}
