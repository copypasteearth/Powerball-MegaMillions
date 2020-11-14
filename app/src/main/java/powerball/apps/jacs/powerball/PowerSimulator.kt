/*
 * Author: John Rowan
 * Description: Fragment to view simulator data used in viewpager
 * Anyone may use this file or anything contained in this project for their own personal use.
 */
package powerball.apps.jacs.powerball

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import powerball.apps.jacs.powerball.PowerballSimulatorService.PowerballSimulatorServiceBinder
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [PowerSimulator.newInstance] factory method to
 * create an instance of this fragment.
 */
class PowerSimulator : Fragment() {
    var start: Button? = null
    var reset: Button? = null
    var linear: LinearLayout? = null
    var mContext: Context? = null
    var listener: PowerballSimulatorListener? = null
    var data: ArrayList<SimulatorData> = ArrayList()
    var mPlayService: PowerballSimulatorService? = null
    var viewList = ArrayList<View>()
    var binding = false
    private var mPlayServiceConnection: ServiceConnection? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        if (mPlayService != null) mPlayService!!.setPowerballSimulatorListener(null)
    }

    override fun onResume() {
        super.onResume()
        if (mPlayService != null) mPlayService!!.setPowerballSimulatorListener(listener)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (binding) mContext!!.unbindService(mPlayServiceConnection)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_power_simulator, container, false)
        start = view.findViewById<View>(R.id.start) as Button
        if (PowerballSimulatorService.running) start!!.setText(R.string.stop)
        reset = view.findViewById<View>(R.id.reset) as Button
        linear = view.findViewById<View>(R.id.powersimlinear) as LinearLayout
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val tickets = SharedPrefHelper.getMyTickets(mContext!!, Constants.POWER_TICKETS)
        data = if (PowerballSimulatorService.running) {
            PowerballSimulatorService.data
        } else {
            SharedPrefHelper.getSimData(mContext!!, Constants.POWER_SIM)
        }
        Log.d("powerservice", "fragment size: $data")
        val powerSimCounter = SharedPrefHelper.getLongPowerballCounter(mContext!!)
        if (tickets.size > 0) {
            var update = false
            for (x in data!!.indices) {
                var inside = false
                for (y in tickets.indices) {
                    if (data!![x]!!.number == tickets[y].ticket) {
                        inside = true
                    }
                }
                if (!inside) {
                    data!!.removeAt(x)
                    update = true
                }
            }
            //checking if each ticket has a corresponding simulatorData and if not making one
            for (i in tickets.indices) {
                var contained = false
                for (j in data!!.indices) {
                    if (tickets[i].ticket == data!![j]!!.number) {
                        contained = true
                    }
                }
                if (!contained) {
                    val simDat = SimulatorData()
                    simDat.number = tickets[i].ticket
                    if (PowerballSimulatorService.running) {
                        simDat.ofsetPlays = PowerballSimulatorService.counter
                    } else {
                        simDat.ofsetPlays = powerSimCounter
                    }
                    data!!.add(simDat)
                    update = true
                }
            }
            if (update) {
                SharedPrefHelper.setSimData(mContext!!, data, Constants.POWER_SIM)
                if (PowerballSimulatorService.running) {
                    SharedPrefHelper.setLongPowerballCounter(mContext!!, PowerballSimulatorService.counter)
                    PowerballSimulatorService.update = true
                }
            }
            reset!!.setOnClickListener {
                val builder = AlertDialog.Builder(mContext)
                builder.setMessage(getString(R.string.messageDialog))
                builder.setPositiveButton(R.string.yes) { dialog, which ->
                    resetButtonWork()
                    dialog.dismiss()
                }
                builder.setNegativeButton(R.string.no) { dialog, which -> dialog.dismiss() }
                val dialog1 = builder.create()
                dialog1.setTitle(R.string.resetyes)
                dialog1.show()
            }
            val inflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            for (q in data!!.indices) {
                val singleData = data!![q]
                val view = inflater.inflate(R.layout.power_sim, null)
                val include = view.findViewById<View>(R.id.includesim)
                val ball1 = view.findViewById<View>(R.id.ball1) as TextView
                val ball2 = view.findViewById<View>(R.id.ball2) as TextView
                val ball3 = view.findViewById<View>(R.id.ball3) as TextView
                val ball4 = view.findViewById<View>(R.id.ball4) as TextView
                val ball5 = view.findViewById<View>(R.id.ball5) as TextView
                val ball6 = view.findViewById<View>(R.id.ball6) as TextView
                val win = view.findViewById<View>(R.id.win) as TextView
                win.text = ""
                val images = arrayOf(ball1, ball2, ball3, ball4, ball5, ball6)
                val num = singleData!!.number
                val split = num!!.split(" ").toTypedArray()
                for (j in split.indices) {
                    images[j].text = split[j]
                }
                setViews(singleData, view)
                viewList.add(view)
                linear!!.addView(view)
            }
            listener = object : PowerballSimulatorListener {
                override fun onLottoResult(listData: ArrayList<SimulatorData>) {
                    Log.d("powerservice", "onlottoresult" + listData!!.size)
                    for (i in listData.indices) {
                        val singleData = listData[i]
                        val view = viewList[i]
                        setViews(singleData, view)
                    }
                }

                override fun stopLottoService() {
                    val intent = Intent(mContext, PowerballSimulatorService::class.java)
                    mContext!!.unbindService(mPlayServiceConnection)
                    binding = false
                    mContext!!.stopService(intent)
                    start!!.setText(R.string.start)
                }
            }
            mPlayServiceConnection = object : ServiceConnection {
                override fun onServiceConnected(className: ComponentName, service: IBinder) {
                    val binder = service as PowerballSimulatorServiceBinder
                    mPlayService = binder.service
                    mPlayService!!.setPowerballSimulatorListener(listener)
                    // mPlayService.setMainActivity(getActivity());
                    Log.d("powerservice", "service binded")
                }

                override fun onServiceDisconnected(className: ComponentName) {
                    Log.d("powerservice", "service unbinded")
                }
            }
            start!!.setOnClickListener {
                if (PowerballSimulatorService.running) {
                    val intent = Intent(mContext, PowerballSimulatorService::class.java)
                    mContext!!.unbindService(mPlayServiceConnection)
                    binding = false
                    mContext!!.stopService(intent)
                    start!!.setText(R.string.start)
                } else {
                    val intent = Intent(mContext, PowerballSimulatorService::class.java)
                    if (Build.VERSION.SDK_INT > 25) {
                        mContext!!.startForegroundService(intent)
                    } else {
                        mContext!!.startService(intent)
                    }
                    mContext!!.bindService(intent, mPlayServiceConnection, Context.BIND_AUTO_CREATE)
                    binding = true
                    start!!.setText(R.string.stop)
                }
            }
            if (PowerballSimulatorService.running) {
                val intent = Intent(mContext, PowerballSimulatorService::class.java)
                mContext!!.bindService(intent, mPlayServiceConnection, Context.BIND_AUTO_CREATE)
                binding = true
            }
        } else {
            start!!.isEnabled = false
            reset!!.isEnabled = false
        }
    }

    fun setViews(singleData: SimulatorData?, view: View) {
        //set the views details from the simulator data
        val years = getString(R.string.years)
        val weeks = getString(R.string.weeks)
        val average = getString(R.string.average)
        val hits = getString(R.string.hits)
        val minimum = getString(R.string.minimum)
        val totalPlays = view.findViewById<View>(R.id.totalplays) as TextView
        totalPlays.text = getString(R.string.totalplays) + ": " + singleData!!.plays + " -- " + singleData.getDays(singleData.plays, years, weeks)
        val jackPotHit = view.findViewById<View>(R.id.jackpothit) as TextView
        jackPotHit.text = hits + ": " + singleData.jackpotHits
        val jackPotAvg = view.findViewById<View>(R.id.jackpotavg) as TextView
        jackPotAvg.text = average + ": " + singleData.jackpotAvg + " -- " + singleData.getDays(singleData.jackpotAvg, years, weeks)
        val jackPotMin = view.findViewById<View>(R.id.jackpotmin) as TextView
        jackPotMin.text = minimum + ": " + singleData.jackpotMin + " -- " + singleData.getDays(singleData.jackpotMin, years, weeks)
        val ball5Hit = view.findViewById<View>(R.id.ball5hit) as TextView
        ball5Hit.text = hits + ": " + singleData.white5Hits
        val ball5Avg = view.findViewById<View>(R.id.ball5avg) as TextView
        ball5Avg.text = average + ": " + singleData.white5Avg + " -- " + singleData.getDays(singleData.white5Avg, years, weeks)
        val ball5Min = view.findViewById<View>(R.id.ball5min) as TextView
        ball5Min.text = minimum + ": " + singleData.white5Min + " -- " + singleData.getDays(singleData.white5Min, years, weeks)
        val ball4PowHit = view.findViewById<View>(R.id.ball4powerthit) as TextView
        ball4PowHit.text = hits + ": " + singleData.white4PowHits
        val ball4PowAvg = view.findViewById<View>(R.id.ball4poweravg) as TextView
        ball4PowAvg.text = average + ": " + singleData.white4PowAvg + " -- " + singleData.getDays(singleData.white4PowAvg, years, weeks)
        val ball4PowMin = view.findViewById<View>(R.id.ball4powermin) as TextView
        ball4PowMin.text = minimum + ": " + singleData.white4PowMin + " -- " + singleData.getDays(singleData.white4PowMin, years, weeks)
        val ball4Hit = view.findViewById<View>(R.id.ball4hit) as TextView
        ball4Hit.text = hits + ": " + singleData.white4Hits
        val ball4Avg = view.findViewById<View>(R.id.ball4avg) as TextView
        ball4Avg.text = average + ": " + singleData.white4Avg + " -- " + singleData.getDays(singleData.white4Avg, years, weeks)
        val ball4Min = view.findViewById<View>(R.id.ball4min) as TextView
        ball4Min.text = minimum + ": " + singleData.white4Min + " -- " + singleData.getDays(singleData.white4Min, years, weeks)
        val ball3PowHit = view.findViewById<View>(R.id.ball3powerhit) as TextView
        ball3PowHit.text = hits + ": " + singleData.white3PowHits
        val ball3PowAvg = view.findViewById<View>(R.id.ball3poweravg) as TextView
        ball3PowAvg.text = average + ": " + singleData.white3PowAvg + " -- " + singleData.getDays(singleData.white3PowAvg, years, weeks)
        val ball3PowMin = view.findViewById<View>(R.id.ball3powermin) as TextView
        ball3PowMin.text = minimum + ": " + singleData.white3PowMin + " -- " + singleData.getDays(singleData.white3PowMin, years, weeks)
        val ball3Hit = view.findViewById<View>(R.id.ball3hit) as TextView
        ball3Hit.text = hits + ": " + singleData.white3Hits
        val ball3Avg = view.findViewById<View>(R.id.ball3avg) as TextView
        ball3Avg.text = average + ": " + singleData.white3Avg + " -- " + singleData.getDays(singleData.white3Avg, years, weeks)
        val ball3Min = view.findViewById<View>(R.id.ball3min) as TextView
        ball3Min.text = minimum + ": " + singleData.white3Min + " -- " + singleData.getDays(singleData.white3Min, years, weeks)
        val ball2PowHit = view.findViewById<View>(R.id.ball2powerhit) as TextView
        ball2PowHit.text = hits + ": " + singleData.white2PowHits
        val ball2PowAvg = view.findViewById<View>(R.id.ball2poweravg) as TextView
        ball2PowAvg.text = average + ": " + singleData.white2PowAvg + " -- " + singleData.getDays(singleData.white2PowAvg, years, weeks)
        val ball2PowMin = view.findViewById<View>(R.id.ball2powermin) as TextView
        ball2PowMin.text = minimum + ": " + singleData.white2PowMin + " -- " + singleData.getDays(singleData.white2PowMin, years, weeks)
        val ball1PowHit = view.findViewById<View>(R.id.ball1powerhit) as TextView
        ball1PowHit.text = hits + ": " + singleData.white1PowHits
        val ball1PowAvg = view.findViewById<View>(R.id.ball1poweravg) as TextView
        ball1PowAvg.text = average + ": " + singleData.white1PowAvg + " -- " + singleData.getDays(singleData.white1PowAvg, years, weeks)
        val ball1PowMin = view.findViewById<View>(R.id.ball1powermin) as TextView
        ball1PowMin.text = minimum + ": " + singleData.white1PowMin + " -- " + singleData.getDays(singleData.white1PowMin, years, weeks)
        val powerHit = view.findViewById<View>(R.id.powerhit) as TextView
        powerHit.text = hits + ": " + singleData.nowhitePowHits
        val powerAvg = view.findViewById<View>(R.id.poweravg) as TextView
        powerAvg.text = average + ": " + singleData.nowhitePowAvg + " -- " + singleData.getDays(singleData.nowhitePowAvg, years, weeks)
        val powerMin = view.findViewById<View>(R.id.powermin) as TextView
        powerMin.text = minimum + ": " + singleData.nowhitePowMin + " -- " + singleData.getDays(singleData.nowhitePowMin, years, weeks)
    }

    fun resetButtonWork() {
        data!!.clear()
        SharedPrefHelper.setSimData(mContext!!, data, Constants.POWER_SIM)
        SharedPrefHelper.setLongPowerballCounter(mContext!!, 0)
        val tickets = SharedPrefHelper.getMyTickets(mContext!!, Constants.POWER_TICKETS)
        data = SharedPrefHelper.getSimData(mContext!!, Constants.POWER_SIM)
        val powerSimCounter = SharedPrefHelper.getLongPowerballCounter(mContext!!)
        if (tickets.size > 0) {
            var update = false
            for (x in data?.indices!!) {
                var inside = false
                for (y in tickets.indices) {
                    if (data?.get(x)?.number!! == tickets[y].ticket) {
                        inside = true
                        update = true
                    }
                }
                if (!inside) {
                    data?.removeAt(x)
                }
            }
            //checking if each ticket has a corresponding simulatorData and if not making one
            for (i in tickets.indices) {
                var contained = false
                for (j in data?.indices!!) {
                    if (tickets[i].ticket == data?.get(j)?.number!!) {
                        contained = true
                    }
                }
                if (!contained) {
                    val simDat = SimulatorData()
                    simDat.number = tickets[i].ticket
                    simDat.ofsetPlays = powerSimCounter
                    data?.add(simDat)
                    update = true
                }
            }
            if (update) {
                SharedPrefHelper.setSimData(mContext!!, data, Constants.POWER_SIM)
                if (PowerballSimulatorService.running) PowerballSimulatorService.update = true
            }
            linear!!.removeAllViews()
            viewList.clear()
            val inflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            for (q in data?.indices!!) {
                val view = inflater.inflate(R.layout.power_sim, null)
                val include = view.findViewById<View>(R.id.includesim)
                val ball1 = view.findViewById<View>(R.id.ball1) as TextView
                val ball2 = view.findViewById<View>(R.id.ball2) as TextView
                val ball3 = view.findViewById<View>(R.id.ball3) as TextView
                val ball4 = view.findViewById<View>(R.id.ball4) as TextView
                val ball5 = view.findViewById<View>(R.id.ball5) as TextView
                val ball6 = view.findViewById<View>(R.id.ball6) as TextView
                val win = view.findViewById<View>(R.id.win) as TextView
                win.text = ""
                val images = arrayOf(ball1, ball2, ball3, ball4, ball5, ball6)
                val num = data?.get(q)?.number!!
                val split = num.split(" ").toTypedArray()
                for (j in split.indices) {
                    images[j].text = split[j]
                }
                viewList.add(view)
                linear!!.addView(view)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment PowerSimulator.
         */
        fun newInstance(): PowerSimulator {
            return PowerSimulator()
        }
    }
}