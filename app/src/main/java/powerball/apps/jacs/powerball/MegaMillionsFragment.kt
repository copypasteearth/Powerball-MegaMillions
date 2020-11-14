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
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.text.SimpleDateFormat
import java.util.*

//https://data.ny.gov/resource/5xaw-6ayf.json
class MegaMillionsFragment : Fragment() {
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
        val url = "https://www.megamillions.com/cmspages/utilservice.asmx/GetDrawingPagingData?pageNumber=1&pageSize=21&startDate=" + inputFormat.format(past.getTime()) + "&endDate=" + inputFormat.format(now.getTime())
        Log.d("timer", url)
        val stringRequest = StringRequest(Request.Method.GET, url,
                { response ->
                    try {
                        val factory = XmlPullParserFactory.newInstance()
                        factory.isNamespaceAware = true
                        val parser = factory.newPullParser()
                        parser.setInput(StringReader(response))
                        var eventType = parser.eventType
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            if (eventType == XmlPullParser.START_TAG) {
                                val name = parser.name
                                if (name == "string") {
                                    if (parser.next() == XmlPullParser.TEXT) {
                                        val json = parser.text
                                        val `object` = JSONObject(json)
                                        val array = `object`.getJSONArray("DrawingData")
                                        for (i in 0 until array.length()) {
                                            val jsonObject = array.getJSONObject(i)
                                            val person = WinningTicket()
                                            var ball1 = 0
                                            var ball2 = 0
                                            var ball3 = 0
                                            var ball4 = 0
                                            var ball5 = 0
                                            var ball6 = 0
                                            var mega: Int
                                            if (!jsonObject.isNull("PlayDate")) {
                                                person.date = jsonObject.getString("PlayDate")
                                            }
                                            if (!jsonObject.isNull("N1")) {
                                                ball1 = jsonObject.getInt("N1")
                                            }
                                            if (!jsonObject.isNull("N2")) {
                                                ball2 = jsonObject.getInt("N2")
                                            }
                                            if (!jsonObject.isNull("N3")) {
                                                ball3 = jsonObject.getInt("N3")
                                            }
                                            if (!jsonObject.isNull("N4")) {
                                                ball4 = jsonObject.getInt("N4")
                                            }
                                            if (!jsonObject.isNull("N5")) {
                                                ball5 = jsonObject.getInt("N5")
                                            }
                                            if (!jsonObject.isNull("MBall")) {
                                                ball6 = jsonObject.getInt("MBall")
                                            }
                                            if (!jsonObject.isNull("Megaplier")) {
                                                person.multiplier = jsonObject.getString("Megaplier")
                                            }
                                            person.winningNumber = "$ball1,$ball2,$ball3,$ball4,$ball5,$ball6"
                                            //if(!first){
                                            //    WinningTicket tick = SharedPrefHelper.getSharedOBJECT(getApplicationContext(),"ticket");
                                            //    if(tick == null || !tick.equals(person)){
                                            //        SharedPrefHelper.setSharedOBJECT(getApplicationContext(),"ticket",person);
                                            //    }
                                            //    first = true;
                                            //}
                                            tickets.add(counter, person)
                                            rvAdapter.notifyItemInserted(counter)
                                            counter++
                                        }
                                        Log.d("timer", "json:$json")
                                    }
                                }
                            } else if (eventType == XmlPullParser.END_TAG) {
                            }
                            eventType = parser.next()
                        }
                    } catch (e: Exception) {
                        Log.d("timer", "Error in ParseXML()", e)
                    }
                }
        ) {
            // handle error response
        }
        requestQueue.add(stringRequest)
        rvAdapter.setOnLoadMoreListener (object : OnLoadMoreListener {
            override fun onLoadMore() {
                Log.d("timer", "loading more")
                tickets.add(null)
                rvAdapter.notifyItemInserted(tickets.size - 1)
                now = past.clone() as Calendar
                past.add(Calendar.MONTH, -1)
                Log.d("timer", "dates:::::: " + now!!.time.toString() + "  :  " + past.getTime().toString())
                val url = "https://www.megamillions.com/cmspages/utilservice.asmx/GetDrawingPagingData?pageNumber=1&pageSize=21&startDate=" + inputFormat.format(past.getTime()) + "&endDate=" + inputFormat.format(now!!.time)
                val stringRequest = StringRequest(Request.Method.GET, url,
                        { response ->
                            try {
                                tickets.removeAt(tickets.size - 1)
                                rvAdapter.notifyItemRemoved(tickets.size)
                                val factory = XmlPullParserFactory.newInstance()
                                factory.isNamespaceAware = true
                                val parser = factory.newPullParser()
                                parser.setInput(StringReader(response))
                                var eventType = parser.eventType
                                while (eventType != XmlPullParser.END_DOCUMENT) {
                                    if (eventType == XmlPullParser.START_TAG) {
                                        val name = parser.name
                                        if (name == "string") {
                                            if (parser.next() == XmlPullParser.TEXT) {
                                                val json = parser.text
                                                val `object` = JSONObject(json)
                                                val array = `object`.getJSONArray("DrawingData")
                                                for (i in 0 until array.length()) {
                                                    val jsonObject = array.getJSONObject(i)
                                                    val person = WinningTicket()
                                                    var ball1 = 0
                                                    var ball2 = 0
                                                    var ball3 = 0
                                                    var ball4 = 0
                                                    var ball5 = 0
                                                    var ball6 = 0
                                                    var mega: Int
                                                    if (!jsonObject.isNull("PlayDate")) {
                                                        person.date = jsonObject.getString("PlayDate")
                                                    }
                                                    if (!jsonObject.isNull("N1")) {
                                                        ball1 = jsonObject.getInt("N1")
                                                    }
                                                    if (!jsonObject.isNull("N2")) {
                                                        ball2 = jsonObject.getInt("N2")
                                                    }
                                                    if (!jsonObject.isNull("N3")) {
                                                        ball3 = jsonObject.getInt("N3")
                                                    }
                                                    if (!jsonObject.isNull("N4")) {
                                                        ball4 = jsonObject.getInt("N4")
                                                    }
                                                    if (!jsonObject.isNull("N5")) {
                                                        ball5 = jsonObject.getInt("N5")
                                                    }
                                                    if (!jsonObject.isNull("MBall")) {
                                                        ball6 = jsonObject.getInt("MBall")
                                                    }
                                                    if (!jsonObject.isNull("Megaplier")) {
                                                        person.multiplier = jsonObject.getString("Megaplier")
                                                    }
                                                    person.winningNumber = "$ball1,$ball2,$ball3,$ball4,$ball5,$ball6"
                                                    //if(!first){
                                                    //    WinningTicket tick = SharedPrefHelper.getSharedOBJECT(getApplicationContext(),"ticket");
                                                    //    if(tick == null || !tick.equals(person)){
                                                    //        SharedPrefHelper.setSharedOBJECT(getApplicationContext(),"ticket",person);
                                                    //    }
                                                    //    first = true;
                                                    //}
                                                    if (!tickets.contains(person)) {
                                                        tickets.add(counter, person)
                                                        rvAdapter.notifyItemInserted(counter)
                                                        counter++
                                                    }
                                                }
                                                Log.d("timer", "json:$json")
                                                rvAdapter.setLoaded()
                                            }
                                        }
                                    } else if (eventType == XmlPullParser.END_TAG) {
                                    }
                                    eventType = parser.next()
                                }
                            } catch (e: Exception) {
                                Log.d("timer", "Error in ParseXML()", e)
                            }
                        }
                ) {
                    // handle error response
                }
                requestQueue.add(stringRequest)
            }
        }

        )
    }

    companion object {
        fun newInstance(param1: String?, param2: String?): MegaMillionsFragment {
            return MegaMillionsFragment()
        }
    }
}