package powerball.apps.jacs.powerball;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class PastNumbers extends Fragment {
public ViewPager viewPager;
public ViewPagerAdapter adapter;
    public PastNumbers() {
        // Required empty public constructor
    }


    public static PastNumbers newInstance() {
        PastNumbers fragment = new PastNumbers();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new PowerballFragment(), getResources().getString(R.string.powerball));
        adapter.addFragment(new MegaMillionsFragment(), getResources().getString(R.string.megamillions));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past_numbers, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.pager1);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs1);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("timer","PastNumbers onactivity created");
    }

    @Override
    public void onStart() {
        super.onStart();



        // Add Fragments to adapter one by one



    }
}
