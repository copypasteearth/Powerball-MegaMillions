/*
 * Author: John Rowan
 * Description: this is a fragment with a viewpager that has a fragment for each the powerball and the megamillions
 * that both deal with the past results of each lottery
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

class PastNumbers : Fragment() {
    var viewPager: ViewPager? = null
    var adapter: ViewPagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ViewPagerAdapter(childFragmentManager)
        adapter!!.addFragment(PowerballFragment(), resources.getString(R.string.powerball))
        adapter!!.addFragment(MegaMillionsFragment(), resources.getString(R.string.megamillions))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_past_numbers, container, false)
        viewPager = view.findViewById<View>(R.id.pager1) as ViewPager
        viewPager!!.adapter = adapter
        viewPager!!.offscreenPageLimit = 2
        val tabLayout = view.findViewById<View>(R.id.tabs1) as TabLayout
        tabLayout.setupWithViewPager(viewPager)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("timer", "PastNumbers onactivity created")
    }

    override fun onStart() {
        super.onStart()


        // Add Fragments to adapter one by one
    }

    companion object {
        fun newInstance(): PastNumbers {
            return PastNumbers()
        }
    }
}