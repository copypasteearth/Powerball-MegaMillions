/*
 * Author: John Rowan
 * Description: fragment for retrieving past results for the powerball and displaying them along with the results
 * of the users tickets.
 * Anyone may use this file or anything contained in this project for their own personal use.
 */
package powerball.apps.jacs.powerball

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import powerball.apps.jacs.powerball.data.power.PowerballData
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

//https://data.ny.gov/resource/d6yy-54nr.json
class PowerballFragment : Fragment() {
    val url = "https://data.ny.gov/resource/d6yy-54nr.json"
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
        val rv = requireView().findViewById<View>(R.id.recycle) as RecyclerView
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

        GlobalScope.launch {
            withContext(Dispatchers.IO){
                try{
                    val json = URL(url).readText()
                    Log.d("jsondata", json)
                    val gson = Gson()
                    val powerballData = gson.fromJson(json, PowerballData::class.java)
                    for(ticket in powerballData){
                        val person = WinningTicket()
                        person.date = ticket.draw_date//jsonObject.getString("field_draw_date")
                        person.winningNumber = ticket.winning_numbers
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
        fun newInstance(): PowerballFragment {
            return PowerballFragment()
        }
    }
}