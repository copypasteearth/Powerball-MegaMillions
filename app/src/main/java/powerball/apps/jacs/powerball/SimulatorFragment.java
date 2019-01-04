/*
 * Author: John Rowan
 * Description: fragment with viewpager that holds PowerSimulator and MegaSimulator fragments
 * Anyone may use this file or anything contained in this project for their own personal use.
 */

package powerball.apps.jacs.powerball;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SimulatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SimulatorFragment extends Fragment {
    public ViewPager viewPager;
    public ViewPagerAdapter adapter;
    public TabLayout tabLayout;

    public SimulatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SimulatorFragment.
     */

    public static SimulatorFragment newInstance() {
        SimulatorFragment fragment = new SimulatorFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new PowerSimulator(), getResources().getString(R.string.powerball));
        adapter.addFragment(new MegaSimulator(), getResources().getString(R.string.megamillions));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_simulator, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.pagersim);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout = (TabLayout) view.findViewById(R.id.tabssim);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

}
