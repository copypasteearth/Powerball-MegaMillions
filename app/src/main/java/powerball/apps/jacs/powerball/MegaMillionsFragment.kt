/*
 * Author: John Rowan
 * Description: this fragment is open from the past results navigation menu option and it fetches
 * the winning numbers starting at the current day and going into the past. it compares all of the users
 * picked numbers with the winning numbers and displays the results.
 * Anyone may use this file or anything contained in this project for their own personal use.
 */
package powerball.apps.jacs.powerball

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import powerball.apps.jacs.powerball.data.mega.MegamillionsData
import powerball.apps.jacs.powerball.data.power.PowerballData
import java.io.StringReader
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

//https://data.ny.gov/resource/5xaw-6ayf.json
class MegaMillionsFragment : Fragment() {
    val url = "https://data.ny.gov/resource/5xaw-6ayf.json"
    var tickets = ArrayList<WinningTicket?>()
    var inputFormat = SimpleDateFormat("yyyy-MM-dd")
    lateinit var now: Calendar
    lateinit var past: Calendar
    lateinit var requestQueue: RequestQueue
    var counter = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mega_millions, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setRecyclerView(activity)
        Log.d("timer", "onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d("timer", "onstart")
        //setRecyclerView(getActivity());
    }

    fun setRecyclerView(mContext: Context?) {
        counter = 0
        tickets = ArrayList()
        val rv = view!!.findViewById<View>(R.id.recycle1) as RecyclerView
        rv.removeAllViews()
        rv.setHasFixedSize(true)
        rv.addItemDecoration(DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL))
        val llm = LinearLayoutManager(mContext)
        rv.layoutManager = llm
        val rvAdapter = RVAdapter(tickets, rv, Constants.MEGA_MILLIONS)
        rv.adapter = rvAdapter
        requestQueue = Volley.newRequestQueue(mContext)
        now = Calendar.getInstance()
        past = Calendar.getInstance()
        past.add(Calendar.MONTH, -1)
        //String url = "http://data.ny.gov/resource/d6yy-54nr.json";

        GlobalScope.launch {
            withContext(Dispatchers.IO){
                try{
                    val json = URL(url).readText()
                    Log.d("jsondata", json)
                    val gson = Gson()
                    val megamillionsData = gson.fromJson(json, MegamillionsData::class.java)
                    for(ticket in megamillionsData){
                        val person = WinningTicket()
                        person.date = ticket.draw_date//jsonObject.getString("field_draw_date")
                        person.winningNumber = ticket.winning_numbers + " " + ticket.mega_ball
                        person.multiplier = ticket.multiplier
                        tickets.add(counter, person)
                        rv.post{
                            rvAdapter.notifyItemInserted(counter)
                        }
                        counter++
                    }
                }catch(e : Exception){
                    Log.e("exception coroutine", "exception ${e.toString()}")
                }

            }
        }
    }

    companion object {
        fun newInstance(param1: String?, param2: String?): MegaMillionsFragment {
            return MegaMillionsFragment()
        }
    }
}