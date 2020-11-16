/*
 * Author: John Rowan
 * Description: Fragment used to view simulator data used in viewpager
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
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [MegaSimulator.newInstance] factory method to
 * create an instance of this fragment.
 */
class MegaSimulator : Fragment() {
    var start: Button? = null
    var reset: Button? = null
    var linear: LinearLayout? = null
    var mContext: Context? = null
    var listener: MegamillionsSimulatorListener? = null
    var data: ArrayList<SimulatorData> = ArrayList()
    var mPlayService: MegaMillionsSimulatorService? = null
    var viewList = ArrayList<View>()
    var binding = false
    private var mPlayServiceConnection: ServiceConnection? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onPause() {
        super.onPause()
        if (mPlayService != null) mPlayService!!.setMegaMillionsSimulatorListener(null)
    }

    override fun onResume() {
        super.onResume()
        if (mPlayService != null) mPlayService!!.setMegaMillionsSimulatorListener(listener)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (binding) mContext!!.unbindService(mPlayServiceConnection)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mega_simulator, container, false)
        start = view.findViewById<View>(R.id.startm) as Button
        if (PowerballSimulatorService.running) start!!.setText(R.string.stop)
        reset = view.findViewById<View>(R.id.resetm) as Button
        linear = view.findViewById<View>(R.id.megasimlinear) as LinearLayout
        return view
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val tickets = SharedPrefHelper.getMyTickets(mContext!!, Constants.MEGA_TICKETS)
        data = if (MegaMillionsSimulatorService.running) {
            MegaMillionsSimulatorService.data
        } else {
            SharedPrefHelper.getSimData(mContext!!, Constants.MEGA_SIM)
        }
        Log.d("powerservice", "fragment size: $data")
        val powerSimCounter = SharedPrefHelper.getLongMegaCounter(mContext!!)
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
                    if (MegaMillionsSimulatorService.running) {
                        simDat.ofsetPlays = MegaMillionsSimulatorService.counter
                    } else {
                        simDat.ofsetPlays = powerSimCounter
                    }
                    data!!.add(simDat)
                    update = true
                }
            }
            if (update) {
                SharedPrefHelper.setSimData(mContext!!, data, Constants.MEGA_SIM)
                if (MegaMillionsSimulatorService.running) {
                    SharedPrefHelper.setLongMegaCounter(mContext!!, MegaMillionsSimulatorService.counter)
                    MegaMillionsSimulatorService.update = true
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
                val view = inflater.inflate(R.layout.mega_sim, null)
                val include = view.findViewById<View>(R.id.includesim1)
                val ball1 = view.findViewById<View>(R.id.ball1m) as TextView
                val ball2 = view.findViewById<View>(R.id.ball2m) as TextView
                val ball3 = view.findViewById<View>(R.id.ball3m) as TextView
                val ball4 = view.findViewById<View>(R.id.ball4m) as TextView
                val ball5 = view.findViewById<View>(R.id.ball5m) as TextView
                val ball6 = view.findViewById<View>(R.id.ball6m) as TextView
                val win = view.findViewById<View>(R.id.winm) as TextView
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
            listener = object : MegamillionsSimulatorListener {
                override fun onLottoResult(listData: ArrayList<SimulatorData>) {
                    Log.d("powerservice", "onlottoresult" + listData!!.size)
                    for (i in listData.indices) {
                        val singleData = listData[i]
                        val view = viewList[i]
                        setViews(singleData, view)
                    }
                }

                override fun stopLottoService() {
                    val intent = Intent(mContext, MegaMillionsSimulatorService::class.java)
                    mContext!!.unbindService(mPlayServiceConnection)
                    binding = false
                    mContext!!.stopService(intent)
                    start!!.setText(R.string.start)
                }
            }
            mPlayServiceConnection = object : ServiceConnection {
                override fun onServiceConnected(className: ComponentName, service: IBinder) {
                    val binder = service as MegaMillionsSimulatorService.MegaMillionsSimulatorServiceBinder
                    mPlayService = binder.service
                    mPlayService!!.setMegaMillionsSimulatorListener(listener)
                    // mPlayService.setMainActivity(getActivity());
                    Log.d("powerservice", "service binded")
                }

                override fun onServiceDisconnected(className: ComponentName) {
                    Log.d("powerservice", "service unbinded")
                }
            }
            start!!.setOnClickListener {
                if (MegaMillionsSimulatorService.running) {
                    val intent = Intent(mContext, MegaMillionsSimulatorService::class.java)
                    mContext!!.unbindService(mPlayServiceConnection)
                    binding = false
                    mContext!!.stopService(intent)
                    start!!.setText(R.string.start)
                } else {
                    val intent = Intent(mContext, MegaMillionsSimulatorService::class.java)
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
            if (MegaMillionsSimulatorService.running) {
                val intent = Intent(mContext, MegaMillionsSimulatorService::class.java)
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
        val totalPlays = view.findViewById<View>(R.id.totalplays1) as TextView
        totalPlays.text = getString(R.string.totalplays) + ": " + singleData!!.plays + " -- " + singleData.getDays(singleData.plays, years, weeks)
        val jackPotHit = view.findViewById<View>(R.id.jackpothitm) as TextView
        jackPotHit.text = hits + ": " + singleData.jackpotHits
        val jackPotAvg = view.findViewById<View>(R.id.jackpotavgm) as TextView
        jackPotAvg.text = average + ": " + singleData.jackpotAvg + " -- " + singleData.getDays(singleData.jackpotAvg, years, weeks)
        val jackPotMin = view.findViewById<View>(R.id.jackpotminm) as TextView
        jackPotMin.text = minimum + ": " + singleData.jackpotMin + " -- " + singleData.getDays(singleData.jackpotMin, years, weeks)
        val ball5Hit = view.findViewById<View>(R.id.ball5hitm) as TextView
        ball5Hit.text = hits + ": " + singleData.white5Hits
        val ball5Avg = view.findViewById<View>(R.id.ball5avgm) as TextView
        ball5Avg.text = average + ": " + singleData.white5Avg + " -- " + singleData.getDays(singleData.white5Avg, years, weeks)
        val ball5Min = view.findViewById<View>(R.id.ball5minm) as TextView
        ball5Min.text = minimum + ": " + singleData.white5Min + " -- " + singleData.getDays(singleData.white5Min, years, weeks)
        val ball4PowHit = view.findViewById<View>(R.id.ball4megathit) as TextView
        ball4PowHit.text = hits + ": " + singleData.white4PowHits
        val ball4PowAvg = view.findViewById<View>(R.id.ball4megaavg) as TextView
        ball4PowAvg.text = average + ": " + singleData.white4PowAvg + " -- " + singleData.getDays(singleData.white4PowAvg, years, weeks)
        val ball4PowMin = view.findViewById<View>(R.id.ball4megamin) as TextView
        ball4PowMin.text = minimum + ": " + singleData.white4PowMin + " -- " + singleData.getDays(singleData.white4PowMin, years, weeks)
        val ball4Hit = view.findViewById<View>(R.id.ball4hitm) as TextView
        ball4Hit.text = hits + ": " + singleData.white4Hits
        val ball4Avg = view.findViewById<View>(R.id.ball4avgm) as TextView
        ball4Avg.text = average + ": " + singleData.white4Avg + " -- " + singleData.getDays(singleData.white4Avg, years, weeks)
        val ball4Min = view.findViewById<View>(R.id.ball4minm) as TextView
        ball4Min.text = minimum + ": " + singleData.white4Min + " -- " + singleData.getDays(singleData.white4Min, years, weeks)
        val ball3PowHit = view.findViewById<View>(R.id.ball3megahit) as TextView
        ball3PowHit.text = hits + ": " + singleData.white3PowHits
        val ball3PowAvg = view.findViewById<View>(R.id.ball3megaavg) as TextView
        ball3PowAvg.text = average + ": " + singleData.white3PowAvg + " -- " + singleData.getDays(singleData.white3PowAvg, years, weeks)
        val ball3PowMin = view.findViewById<View>(R.id.ball3megamin) as TextView
        ball3PowMin.text = minimum + ": " + singleData.white3PowMin + " -- " + singleData.getDays(singleData.white3PowMin, years, weeks)
        val ball3Hit = view.findViewById<View>(R.id.ball3hitm) as TextView
        ball3Hit.text = hits + ": " + singleData.white3Hits
        val ball3Avg = view.findViewById<View>(R.id.ball3avgm) as TextView
        ball3Avg.text = average + ": " + singleData.white3Avg + " -- " + singleData.getDays(singleData.white3Avg, years, weeks)
        val ball3Min = view.findViewById<View>(R.id.ball3minm) as TextView
        ball3Min.text = minimum + ": " + singleData.white3Min + " -- " + singleData.getDays(singleData.white3Min, years, weeks)
        val ball2PowHit = view.findViewById<View>(R.id.ball2megahit) as TextView
        ball2PowHit.text = hits + ": " + singleData.white2PowHits
        val ball2PowAvg = view.findViewById<View>(R.id.ball2megaavg) as TextView
        ball2PowAvg.text = average + ": " + singleData.white2PowAvg + " -- " + singleData.getDays(singleData.white2PowAvg, years, weeks)
        val ball2PowMin = view.findViewById<View>(R.id.ball2megamin) as TextView
        ball2PowMin.text = minimum + ": " + singleData.white2PowMin + " -- " + singleData.getDays(singleData.white2PowMin, years, weeks)
        val ball1PowHit = view.findViewById<View>(R.id.ball1megahit) as TextView
        ball1PowHit.text = hits + ": " + singleData.white1PowHits
        val ball1PowAvg = view.findViewById<View>(R.id.ball1megaavg) as TextView
        ball1PowAvg.text = average + ": " + singleData.white1PowAvg + " -- " + singleData.getDays(singleData.white1PowAvg, years, weeks)
        val ball1PowMin = view.findViewById<View>(R.id.ball1megamin) as TextView
        ball1PowMin.text = minimum + ": " + singleData.white1PowMin + " -- " + singleData.getDays(singleData.white1PowMin, years, weeks)
        val powerHit = view.findViewById<View>(R.id.megahit) as TextView
        powerHit.text = hits + ": " + singleData.nowhitePowHits
        val powerAvg = view.findViewById<View>(R.id.megaavg) as TextView
        powerAvg.text = average + ": " + singleData.nowhitePowAvg + " -- " + singleData.getDays(singleData.nowhitePowAvg, years, weeks)
        val powerMin = view.findViewById<View>(R.id.megamin) as TextView
        powerMin.text = minimum + ": " + singleData.nowhitePowMin + " -- " + singleData.getDays(singleData.nowhitePowMin, years, weeks)
    }

    fun resetButtonWork() {
        data!!.clear()
        SharedPrefHelper.setSimData(mContext!!, data, Constants.MEGA_SIM)
        SharedPrefHelper.setLongMegaCounter(mContext!!, 0)
        val tickets = SharedPrefHelper.getMyTickets(mContext!!, Constants.MEGA_TICKETS)
        data = SharedPrefHelper.getSimData(mContext!!, Constants.MEGA_SIM)
        val powerSimCounter = SharedPrefHelper.getLongMegaCounter(mContext!!)
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
                SharedPrefHelper.setSimData(mContext!!, data, Constants.MEGA_SIM)
                if (MegaMillionsSimulatorService.running) MegaMillionsSimulatorService.update = true
            }
            linear!!.removeAllViews()
            viewList.clear()
            val inflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            for (q in data?.indices!!) {
                val view = inflater.inflate(R.layout.mega_sim, null)
                val include = view.findViewById<View>(R.id.includesim1)
                val ball1 = view.findViewById<View>(R.id.ball1m) as TextView
                val ball2 = view.findViewById<View>(R.id.ball2m) as TextView
                val ball3 = view.findViewById<View>(R.id.ball3m) as TextView
                val ball4 = view.findViewById<View>(R.id.ball4m) as TextView
                val ball5 = view.findViewById<View>(R.id.ball5m) as TextView
                val ball6 = view.findViewById<View>(R.id.ball6m) as TextView
                val win = view.findViewById<View>(R.id.winm) as TextView
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
         * @return A new instance of fragment MegaSimulator.
         */
        fun newInstance(): MegaSimulator {
            return MegaSimulator()
        }
    }
}