/*
 * Author: John Rowan
 * Description: fragment for retrieving past results for the powerball and displaying them along with the results
 * of the users tickets.
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
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.text.SimpleDateFormat
import java.util.*

//https://data.ny.gov/resource/d6yy-54nr.json
class PowerballFragment : Fragment() {
    var tickets = ArrayList<WinningTicket?>()
    var inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
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
        return inflater.inflate(R.layout.fragment_powerball, container, false)
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
        val rv = view!!.findViewById<View>(R.id.recycle) as RecyclerView
        rv.removeAllViews()
        rv.setHasFixedSize(true)
        rv.addItemDecoration(DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL))
        val llm = LinearLayoutManager(mContext)
        rv.layoutManager = llm
        val rvAdapter = RVAdapter(tickets, rv, Constants.POWERBALL)
        rv.adapter = rvAdapter
        requestQueue = Volley.newRequestQueue(mContext)
        now = Calendar.getInstance()
        past = Calendar.getInstance()
        past.add(Calendar.MONTH, -1)
        //String url = "http://data.ny.gov/resource/d6yy-54nr.json";
        val url = "https://www.powerball.com/api/v1/numbers/powerball?_format=json&fromDate=" + inputFormat.format(past.getTime()).replace(" ", "%20") + "&toDate=" + inputFormat.format(now.getTime()).replace(" ", "%20")
        Log.d("timer", url)
        val jsonArrayRequest = JsonArrayRequest(url, { response ->
            try {
                if (response.length() > 0) {
                    Log.d("timer", "response$response")
                    tickets.clear()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val person = WinningTicket()
                        if (!jsonObject.isNull("field_draw_date")) {
                            person.date = jsonObject.getString("field_draw_date")
                        }
                        if (!jsonObject.isNull("field_winning_numbers")) {
                            person.winningNumber = jsonObject.getString("field_winning_numbers")
                        }
                        if (!jsonObject.isNull("field_multiplier")) {
                            person.multiplier = jsonObject.getString("field_multiplier")
                        }
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
                    // rvAdapter.notifyDataSetChanged();
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }) { error -> // do something
            Log.d("timer", "error : $error")
        }
        requestQueue.add(jsonArrayRequest)
        rvAdapter.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                Log.d("timer", "loading more")
                tickets.add(null)
                rvAdapter.notifyItemInserted(tickets.size - 1)
                now = past.clone() as Calendar
                past.add(Calendar.MONTH, -1)
                Log.d("timer", "dates:::::: " + now!!.time.toString() + "  :  " + past.getTime().toString())
                val url = "https://www.powerball.com/api/v1/numbers/powerball?_format=json&min=" + inputFormat.format(past.getTime()).replace(" ", "%20") + "&max=" + inputFormat.format(now!!.time).replace(" ", "%20")
                val jsonArrayRequest = JsonArrayRequest(url, { response ->
                    try {
                        if (response.length() > 0) {
                            Log.d("timer", "response$response")
                            //tickets.clear();
                            tickets.removeAt(tickets.size - 1)
                            rvAdapter.notifyItemRemoved(tickets.size)
                            for (i in 0 until response.length()) {
                                val jsonObject = response.getJSONObject(i)
                                val person = WinningTicket()
                                if (!jsonObject.isNull("field_draw_date")) {
                                    person.date = jsonObject.getString("field_draw_date")
                                }
                                if (!jsonObject.isNull("field_winning_numbers")) {
                                    person.winningNumber = jsonObject.getString("field_winning_numbers")
                                }
                                if (!jsonObject.isNull("field_multiplier")) {
                                    person.multiplier = jsonObject.getString("field_multiplier")
                                }
                                //if(!first){
                                //    WinningTicket tick = SharedPrefHelper.getSharedOBJECT(getApplicationContext(),"ticket");
                                //    if(tick == null || !tick.equals(person)){
                                //        SharedPrefHelper.setSharedOBJECT(getApplicationContext(),"ticket",person);
                                //    }
                                //    first = true;
                                //}
                                //tickets.add( person);
                                if (!tickets.contains(person)) {
                                    tickets.add(counter, person)
                                    rvAdapter.notifyItemInserted(counter)
                                    counter++
                                }
                            }
                            // rvAdapter.notifyDataSetChanged();
                            //contactAdapter.notifyDataSetChanged();
                            rvAdapter.setLoaded()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }) { error -> // do something
                    Log.d("timer", "error : " + error.localizedMessage + error.networkResponse.statusCode)
                }
                requestQueue.add(jsonArrayRequest)
            }
        })
    }

    companion object {
        fun newInstance(): PowerballFragment {
            return PowerballFragment()
        }
    }
}