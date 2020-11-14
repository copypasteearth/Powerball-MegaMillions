/*
 * Author: John Rowan
 * Description: The service for running powerball simulator
 * Anyone may use this file or anything contained in this project for their own personal use.
 */
package powerball.apps.jacs.powerball

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.support.v4.app.FragmentActivity
import android.support.v4.app.NotificationCompat
import android.util.Log
import java.util.*

class PowerballSimulatorService : Service() {
    private val mBinder: IBinder = PowerballSimulatorServiceBinder()
    private var listener: PowerballSimulatorListener? = null
    var thread: Thread? = null
    private var mainActivity: FragmentActivity? = null
    var handler = Handler()
    var serviceRunnable: Runnable = object : Runnable {
        override fun run() {
            if (update) {
                data = SharedPrefHelper.getSimData(this@PowerballSimulatorService, Constants.POWER_SIM)
                counter = SharedPrefHelper.getLongPowerballCounter(this@PowerballSimulatorService)
                update = false
            }
            val random = Random()
            val nums = ArrayList<Int>()
            while (nums.size < 6) {
                val num = random.nextInt(69) + 1
                if (!nums.contains(num)) {
                    nums.add(num)
                }
            }
            val power = random.nextInt(26) + 1
            Collections.sort(nums)
            val number = nums[0].toString() + "," + nums[1] + "," + nums[2] + "," + nums[3] + "," + nums[4] + "," + nums[5] + "," + power

            //Log.d("powerballnumber","number : " + number);
            val ticket = WinningTicket()
            ticket.winningNumber = number
            counter++
            //SharedPrefHelper.setLongPowerballCounter(PowerballSimulatorService.this,counter);
            for (i in data!!.indices) {
                data!![i]!!.plays = counter - data!![i]!!.ofsetPlays
                data!![i]!!.incrementCounter()
                val winning = ticket.calculateWin(data!![i]!!.number!!)
                if (winning == "Nothing :(") {
                    continue
                }
                if (winning == "Jackpot!!!") {
                    data!![i]!!.jackpotHits++
                    data!![i]!!.jackpotAvg = data!![i]!!.plays / data!![i]!!.jackpotHits
                    if (data!![i]!!.jackpotMin <= 0) {
                        data!![i]!!.jackpotMin = data!![i]!!.getCounter(0)
                        data!![i]!!.resetCounter(0)
                    } else {
                        if (data!![i]!!.getCounter(0) < data!![i]!!.jackpotMin) {
                            data!![i]!!.jackpotMin = data!![i]!!.getCounter(0)
                            data!![i]!!.resetCounter(0)
                        } else {
                            data!![i]!!.resetCounter(0)
                        }
                    }
                } else if (winning == "1 Million winner!!") {
                    data!![i]!!.white5Hits++
                    data!![i]!!.white5Avg = data!![i]!!.plays / data!![i]!!.white5Hits
                    if (data!![i]!!.white5Min <= 0) {
                        data!![i]!!.white5Min = data!![i]!!.getCounter(1)
                        data!![i]!!.resetCounter(1)
                    } else {
                        if (data!![i]!!.getCounter(1) < data!![i]!!.white5Min) {
                            data!![i]!!.white5Min = data!![i]!!.getCounter(1)
                            data!![i]!!.resetCounter(1)
                        } else {
                            data!![i]!!.resetCounter(1)
                        }
                    }
                } else if (winning == "50,000 hit!!!") {
                    data!![i]!!.white4PowHits++
                    data!![i]!!.white4PowAvg = data!![i]!!.plays / data!![i]!!.white4PowHits
                    if (data!![i]!!.white4PowMin <= 0) {
                        data!![i]!!.white4PowMin = data!![i]!!.getCounter(2)
                        data!![i]!!.resetCounter(2)
                    } else {
                        if (data!![i]!!.getCounter(2) < data!![i]!!.white4PowMin) {
                            data!![i]!!.white4PowMin = data!![i]!!.getCounter(2)
                            data!![i]!!.resetCounter(2)
                        } else {
                            data!![i]!!.resetCounter(2)
                        }
                    }
                } else if (winning == "$100 hit") {
                    data!![i]!!.white4Hits++
                    data!![i]!!.white4Avg = data!![i]!!.plays / data!![i]!!.white4Hits
                    if (data!![i]!!.white4Min <= 0) {
                        data!![i]!!.white4Min = data!![i]!!.getCounter(3)
                        data!![i]!!.resetCounter(3)
                    } else {
                        if (data!![i]!!.getCounter(3) < data!![i]!!.white4Min) {
                            data!![i]!!.white4Min = data!![i]!!.getCounter(3)
                            data!![i]!!.resetCounter(3)
                        } else {
                            data!![i]!!.resetCounter(3)
                        }
                    }
                } else if (winning == "$100 hit-") {
                    data!![i]!!.white3PowHits++
                    data!![i]!!.white3PowAvg = data!![i]!!.plays / data!![i]!!.white3PowHits
                    if (data!![i]!!.white3PowMin <= 0) {
                        data!![i]!!.white3PowMin = data!![i]!!.getCounter(4)
                        data!![i]!!.resetCounter(4)
                    } else {
                        if (data!![i]!!.getCounter(4) < data!![i]!!.white3PowMin) {
                            data!![i]!!.white3PowMin = data!![i]!!.getCounter(4)
                            data!![i]!!.resetCounter(4)
                        } else {
                            data!![i]!!.resetCounter(4)
                        }
                    }
                } else if (winning == "$7 hit") {
                    data!![i]!!.white3Hits++
                    data!![i]!!.white3Avg = data!![i]!!.plays / data!![i]!!.white3Hits
                    if (data!![i]!!.white3Min <= 0) {
                        data!![i]!!.white3Min = data!![i]!!.getCounter(5)
                        data!![i]!!.resetCounter(5)
                    } else {
                        if (data!![i]!!.getCounter(5) < data!![i]!!.white3Min) {
                            data!![i]!!.white3Min = data!![i]!!.getCounter(5)
                            data!![i]!!.resetCounter(5)
                        } else {
                            data!![i]!!.resetCounter(5)
                        }
                    }
                } else if (winning == "$7 hit-") {
                    data!![i]!!.white2PowHits++
                    data!![i]!!.white2PowAvg = data!![i]!!.plays / data!![i]!!.white2PowHits
                    if (data!![i]!!.white2PowMin <= 0) {
                        data!![i]!!.white2PowMin = data!![i]!!.getCounter(6)
                        data!![i]!!.resetCounter(6)
                    } else {
                        if (data!![i]!!.getCounter(6) < data!![i]!!.white2PowMin) {
                            data!![i]!!.white2PowMin = data!![i]!!.getCounter(6)
                            data!![i]!!.resetCounter(6)
                        } else {
                            data!![i]!!.resetCounter(6)
                        }
                    }
                } else if (winning == "$4 hit") {
                    data!![i]!!.white1PowHits++
                    data!![i]!!.white1PowAvg = data!![i]!!.plays / data!![i]!!.white1PowHits
                    if (data!![i]!!.white1PowMin <= 0) {
                        data!![i]!!.white1PowMin = data!![i]!!.getCounter(7)
                        data!![i]!!.resetCounter(7)
                    } else {
                        if (data!![i]!!.getCounter(7) < data!![i]!!.white1PowMin) {
                            data!![i]!!.white1PowMin = data!![i]!!.getCounter(7)
                            data!![i]!!.resetCounter(7)
                        } else {
                            data!![i]!!.resetCounter(7)
                        }
                    }
                } else if (winning == "$4 hit-") {
                    data!![i]!!.nowhitePowHits++
                    data!![i]!!.nowhitePowAvg = data!![i]!!.plays / data!![i]!!.nowhitePowHits
                    if (data!![i]!!.nowhitePowMin <= 0) {
                        data!![i]!!.nowhitePowMin = data!![i]!!.getCounter(8)
                        data!![i]!!.resetCounter(8)
                    } else {
                        if (data!![i]!!.getCounter(8) < data!![i]!!.nowhitePowMin) {
                            data!![i]!!.nowhitePowMin = data!![i]!!.getCounter(8)
                            data!![i]!!.resetCounter(8)
                        } else {
                            data!![i]!!.resetCounter(8)
                        }
                    }
                }
            }
            //SharedPrefHelper.setSimData(PowerballSimulatorService.this,data,Constants.POWER_SIM);
            if (listener != null) {
                listener!!.onLottoResult(data)
                Log.d("powerservice", "fired listener")
            }
            handler.postDelayed(this, 0)
        }
    }

    fun setPowerballSimulatorListener(thelistener: PowerballSimulatorListener?) {
        listener = thelistener
        Log.d("powerservice", "service listener set")
    }

    fun setMainActivity(main: FragmentActivity?) {
        mainActivity = main
    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    override fun onUnbind(intent: Intent): Boolean {
        listener = null
        return super.onUnbind(intent)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent != null) {
            if (intent.hasExtra("shutdown")) {
                if (intent.getStringExtra("shutdown") == "shutdown") {
                    if (listener != null) {
                        listener!!.stopLottoService()
                    } else {
                        stopSelf()
                    }
                }
            } else {
                setUpForeground()
            }
        } else {
            setUpForeground()
        }
        return START_STICKY
    }

    fun setUpForeground() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val serviceIntent = Intent(this, PowerballSimulatorService::class.java)
        serviceIntent.putExtra("shutdown", "shutdown")
        val pendingServiceShutdown = PendingIntent.getService(this, 20, serviceIntent, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Powerball"
            val description = "Powerball Notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("10101010101", name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(this, "10101010101")
                .setContentTitle(getText(R.string.powerball))
                .setContentText(getText(R.string.simrunning))
                .setSmallIcon(R.drawable.powerstar1)
                .setContentIntent(pendingIntent)
                .setTicker(getText(R.string.powerball))
                .addAction(R.drawable.powerstar1, getText(R.string.stop), pendingServiceShutdown)
                .build()
        startForeground(Constants.POWER_SIM_FOREGROUND, notification)
        running = true
        counter = SharedPrefHelper.getLongPowerballCounter(this)
        data = SharedPrefHelper.getSimData(this, Constants.POWER_SIM)
        Log.d("powerservice", "data size: " + data?.size)
        handler.postDelayed(serviceRunnable, 0)
        Log.d("powerservice", "onstartcommand")
    }

    override fun onDestroy() {
        super.onDestroy()
        running = false
        //thread.interrupt();
        handler.removeCallbacks(serviceRunnable)
        SharedPrefHelper.setSimData(this, data, Constants.POWER_SIM)
        SharedPrefHelper.setLongPowerballCounter(this, counter)
        Log.d("powerservice", "service destroyed")
    }

    inner class PowerballSimulatorServiceBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        val service: PowerballSimulatorService
            get() =// Return this instance of LocalService so clients can call public methods
                this@PowerballSimulatorService
    }

    companion object {
        @JvmField
        var running = false
        @JvmField
        var update = false

        lateinit var data: ArrayList<SimulatorData>
        @JvmField
        var counter: Long = 0
    }
}