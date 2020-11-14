package powerball.apps.jacs.powerball

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import java.util.*

class MyNumbers : Fragment() {
    var powerBall: TextView? = null
    var megaMillions: TextView? = null
    var powerNumbers: ArrayList<MyTicket>? = null
    var megaNumbers: ArrayList<MyTicket>? = null
    var power: RecyclerView? = null
    var mega: RecyclerView? = null
    var powerAdapter: MyTicketAdapter? = null
    var megaAdapter: MyTicketAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_numbers, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onStart() {
        super.onStart()
        instantiateViews()
        myTickets
        populateRecyclerViews()
        setClickListeners()
    }

    fun instantiateViews() {
        val power1 = view!!.findViewById(R.id.include) as View
        val mega1 = view!!.findViewById(R.id.include1) as View
        powerBall = power1.findViewById<View>(R.id.win) as TextView
        megaMillions = mega1.findViewById<View>(R.id.winm) as TextView
        power = view!!.findViewById<View>(R.id.powerRecycle) as RecyclerView
        mega = view!!.findViewById<View>(R.id.megaRecycle) as RecyclerView
    }

    val myTickets: Unit
        get() {
            powerNumbers = SharedPrefHelper.getMyTickets(this.context!!, "powerball")
            megaNumbers = SharedPrefHelper.getMyTickets(this.context!!, "megamillions")
        }

    fun setMyTickets() {
        SharedPrefHelper.setMyTickets(this.context!!, powerNumbers, "powerball")
        SharedPrefHelper.setMyTickets(this.context!!, megaNumbers, "megamillions")
    }

    fun populateRecyclerViews() {
        //power.setHasFixedSize(true);
        val llm = LinearLayoutManager(activity)
        power!!.layoutManager = llm
        powerAdapter = MyTicketAdapter(this, powerNumbers!!, "powerball")
        power!!.adapter = powerAdapter
        val onRemoveListener = object: OnRemoveListener{
            override fun onRemove(position: Int) {
                powerNumbers!!.removeAt(position)
                Log.d("adapter", powerNumbers!!.size.toString() + "")
                //power.removeViewAt(position);
                power!!.removeAllViews()
                //powerAdapter.notifyItemRemoved(position);
                //powerAdapter.notifyItemRangeChanged(position, powerNumbers.size());
                powerAdapter!!.notifyDataSetChanged()
                SharedPrefHelper.setMyTickets(this@MyNumbers.context!!, powerNumbers, "powerball")
            }

        }
        powerAdapter!!.listener = onRemoveListener

        //mega.setHasFixedSize(true);
        val llm1 = LinearLayoutManager(activity)
        mega!!.layoutManager = llm1
        megaAdapter = MyTicketAdapter(this, megaNumbers!!, "megamillions")
        mega!!.adapter = megaAdapter
        val listener = object : OnRemoveListener{
            override fun onRemove(position: Int) {
                megaNumbers!!.removeAt(position)
                mega!!.removeAllViews()
                megaAdapter!!.notifyDataSetChanged()
                SharedPrefHelper.setMyTickets(this@MyNumbers.context!!, megaNumbers, "megamillions")
            }

        }
        megaAdapter!!.listener = listener
    }

    fun removeView(str: String, position: Int) {
        if (str == "powerball") {
            power!!.removeViewAt(position)
        }
    }

    fun setClickListeners() {
        powerBall!!.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(activity!!)
            // ...Irrelevant code for customizing the buttons and title
            val inflater = activity!!.layoutInflater
            val dialogView = inflater.inflate(R.layout.ticket_selector, null)
            val one = dialogView.findViewById<View>(R.id.one) as NumberPicker
            one.minValue = 1
            one.maxValue = 69
            val two = dialogView.findViewById<View>(R.id.two) as NumberPicker
            two.minValue = 1
            two.maxValue = 69
            val three = dialogView.findViewById<View>(R.id.three) as NumberPicker
            three.minValue = 1
            three.maxValue = 69
            val four = dialogView.findViewById<View>(R.id.four) as NumberPicker
            four.minValue = 1
            four.maxValue = 69
            val five = dialogView.findViewById<View>(R.id.five) as NumberPicker
            five.minValue = 1
            five.maxValue = 69
            val six = dialogView.findViewById<View>(R.id.six) as NumberPicker
            six.minValue = 1
            six.maxValue = 26
            val group = dialogView.findViewById<View>(R.id.group) as RadioGroup
            dialogBuilder.setView(dialogView)
            val alertDialog = dialogBuilder.create()
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", null) { dialog, which -> alertDialog.dismiss() }
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add", null) { dialog, which -> //powerNumbers = SharedPrefHelper.getMyTickets(getActivity(),"powerball");
                //powerNumbers = powerAdapter.mDataset;
                val ticket = one.value.toString() + " " + two.value + " " + three.value +
                        " " + four.value + " " + five.value + " " + six.value
                val mul = group.checkedRadioButtonId
                val but = dialogView.findViewById<RadioButton>(mul)
                val multi = but.text.toString()
                var ticketMultiplier = false
                if (multi == "Yes") {
                    ticketMultiplier = true
                }
                val ticket1 = MyTicket()
                ticket1.ticket = ticket
                ticket1.multi = ticketMultiplier
                powerNumbers!!.add(ticket1)
                Log.d("adapter", powerNumbers!!.size.toString() + "")
                powerAdapter!!.notifyItemInserted(powerNumbers!!.size - 1)
                SharedPrefHelper.setMyTickets(this.context!!, powerNumbers, "powerball")
                alertDialog.dismiss()
            }
            alertDialog.show()
        }
        megaMillions!!.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(activity!!)
            // ...Irrelevant code for customizing the buttons and title
            val inflater = activity!!.layoutInflater
            val dialogView = inflater.inflate(R.layout.ticket_selector, null)
            val one = dialogView.findViewById<View>(R.id.one) as NumberPicker
            one.minValue = 1
            one.maxValue = 69
            val two = dialogView.findViewById<View>(R.id.two) as NumberPicker
            two.minValue = 1
            two.maxValue = 69
            val three = dialogView.findViewById<View>(R.id.three) as NumberPicker
            three.minValue = 1
            three.maxValue = 69
            val four = dialogView.findViewById<View>(R.id.four) as NumberPicker
            four.minValue = 1
            four.maxValue = 69
            val five = dialogView.findViewById<View>(R.id.five) as NumberPicker
            five.minValue = 1
            five.maxValue = 69
            val six = dialogView.findViewById<View>(R.id.six) as NumberPicker
            six.minValue = 1
            six.maxValue = 26
            six.setBackgroundColor(Color.YELLOW)
            val group = dialogView.findViewById<View>(R.id.group) as RadioGroup
            dialogBuilder.setView(dialogView)
            val alertDialog = dialogBuilder.create()
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", null) { dialog, which -> alertDialog.dismiss() }
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add", null) { dialog, which -> //megaNumbers.clear();
                //megaNumbers = SharedPrefHelper.getMyTickets(getActivity(),"megamillions");
                // megaNumbers = new ArrayList<MyTicket>(megaAdapter.mDataset);
                val ticket = one.value.toString() + " " + two.value + " " + three.value +
                        " " + four.value + " " + five.value + " " + six.value
                val mul = group.checkedRadioButtonId
                val but = dialogView.findViewById<RadioButton>(mul)
                val multi = but.text.toString()
                var ticketMultiplier = false
                if (multi == "Yes") {
                    ticketMultiplier = true
                }
                val ticket1 = MyTicket()
                ticket1.ticket = ticket
                ticket1.multi = ticketMultiplier
                megaNumbers!!.add(ticket1)
                megaAdapter!!.notifyItemInserted(megaNumbers!!.size - 1)
                //megaAdapter.notifyDataSetChanged();
                SharedPrefHelper.setMyTickets(this.context!!, megaNumbers, "megamillions")
                alertDialog.dismiss()
            }
            alertDialog.show()
        }
    }

    companion object {
        fun newInstance(): MyNumbers {
            return MyNumbers()
        }
    }
}