/*
 * Author: John Rowan
 * Description: fragment with viewpager that holds PowerSimulator and MegaSimulator fragments
 * Anyone may use this file or anything contained in this project for their own personal use.
 */
package powerball.apps.jacs.powerball

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * A simple [Fragment] subclass.
 * Use the [SimulatorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SimulatorFragment : Fragment() {
    var viewPager: ViewPager? = null
    var adapter: ViewPagerAdapter? = null
    var tabLayout: TabLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ViewPagerAdapter(childFragmentManager)
        adapter!!.addFragment(PowerSimulator(), resources.getString(R.string.powerball))
        adapter!!.addFragment(MegaSimulator(), resources.getString(R.string.megamillions))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_simulator, container, false)
        viewPager = view.findViewById<View>(R.id.pagersim) as ViewPager
        viewPager!!.adapter = adapter
        viewPager!!.offscreenPageLimit = 2
        tabLayout = view.findViewById<View>(R.id.tabssim) as TabLayout
        tabLayout!!.setupWithViewPager(viewPager)
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment SimulatorFragment.
         */
        fun newInstance(): SimulatorFragment {
            return SimulatorFragment()
        }
    }
}