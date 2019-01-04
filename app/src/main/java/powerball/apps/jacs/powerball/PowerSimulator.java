/*
 * Author: John Rowan
 * Description: Fragment to view simulator data used in viewpager
 * Anyone may use this file or anything contained in this project for their own personal use.
 */

package powerball.apps.jacs.powerball;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PowerSimulator#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PowerSimulator extends Fragment {



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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_power_simulator, container, false);
    }

}
