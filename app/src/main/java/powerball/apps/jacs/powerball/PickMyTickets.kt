/*
 * Author: John Rowan
 * Description: main fragment with viewpager that holds a powerball and a mega millions fragment which are
 * used for picking the users tickets and saving them
 * Anyone may use this file or anything contained in this project for their own personal use.
 */
package powerball.apps.jacs.powerball

import android.content.Context
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class PickMyTickets : Fragment() {
    var viewPager: ViewPager? = null
    var adapter: ViewPagerAdapter? = null
    var tabLayout: TabLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ViewPagerAdapter(childFragmentManager)
        adapter!!.addFragment(PickPowerballTickets(), resources.getString(R.string.powerball))
        adapter!!.addFragment(PickMegaMillionsTickets(), resources.getString(R.string.megamillions))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pick_my_tickets, container, false)
        viewPager = view.findViewById<View>(R.id.pager) as ViewPager
        viewPager!!.adapter = adapter
        viewPager!!.offscreenPageLimit = 2
        tabLayout = view.findViewById<View>(R.id.tabs) as TabLayout
        tabLayout!!.setupWithViewPager(viewPager)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onStart() {
        super.onStart()
        Log.d("timer", "starting over again")
    }

    companion object {
        fun newInstance(): PickMyTickets {
            return PickMyTickets()
        }
    }
}