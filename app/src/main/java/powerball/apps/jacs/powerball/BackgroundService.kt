/*
 * Author: John Rowan
 * Description: This is a service to check for the latest powerball numbers and send a notification
 * if the user has won or has not won
 * Anyone may use this file or anything contained in this project for their own personal use.
 */
package powerball.apps.jacs.powerball

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import android.widget.RemoteViews
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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class BackgroundService : Service() {
    private val mHandler = Handler() //run on another Thread to avoid crash
    private var mTimer: Timer? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        if (mTimer != null) // Cancel if already existed
            mTimer!!.cancel() else mTimer = Timer() //recreate new
        mTimer!!.scheduleAtFixedRate(TimeDisplay(this), 0, notify.toLong())
        Log.d("timer", "timer started")
    }

    override fun onDestroy() {
        mTimer!!.cancel()
    }

    /**
     *
     * @param intent
     * @param flags
     * @param startId
     * @return START_STICKY
     * Description: this method makes a notification and displays the notification
     * as well as calls startForeground as required by oreo and up
     */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Powerball"
            val description = "Powerball Notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("10101010101", name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            Log.e("notification", notificationManager.toString())
            notificationManager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(this, "10101010101")
                .setContentTitle(getText(R.string.powerball))
                .setContentText(getText(R.string.numbers))
                .setSmallIcon(R.drawable.powerstar1)
                .setContentIntent(pendingIntent)
                .setTicker(getText(R.string.powerball))
                .build()
        startForeground(Constants.POWER_FOREGROUND, notification)
        return START_STICKY
    }

    internal inner class TimeDisplay(var context: Context) : TimerTask() {
        override fun run() {
            // run on another thread
            mHandler.post {
                GlobalScope.launch{
                    withContext(Dispatchers.IO){
                        val url = "https://data.ny.gov/resource/d6yy-54nr.json"
                        val json = URL(url).readText()
                        val gson = Gson()
                        val powerballData = gson.fromJson(json, PowerballData::class.java)
                        for (ticket in powerballData) {

                            val person = WinningTicket()
                            person.date = ticket.draw_date//jsonObject.getString("field_draw_date")
                            person.winningNumber = ticket.winning_numbers
                            person.multiplier = ticket.multiplier
                            // WinningTicket prev = SharedPrefHelper.getSharedOBJECT(context,"ticket");
                            val jsonFormat = SimpleDateFormat("yyyy-MM-dd'T'00:00:00:000")
                            val theDay = Calendar.getInstance()
                            val yest = Calendar.getInstance()
                            yest.add(Calendar.DAY_OF_YEAR, -1)
                            //Log.d("timer","yest : " + yest.toString());
                            val now = Calendar.getInstance()
                            try {
                                val yesterday = jsonFormat.parse(person.date)
                                theDay.time = yesterday
                            } catch (e: ParseException) {
                                e.printStackTrace()
                            }
                            if ((now[Calendar.YEAR] == theDay[Calendar.YEAR]
                                            && now[Calendar.DAY_OF_YEAR] == theDay[Calendar.DAY_OF_YEAR]) ||
                                    (yest[Calendar.YEAR] == theDay[Calendar.YEAR]
                                            && yest[Calendar.DAY_OF_YEAR] == theDay[Calendar.DAY_OF_YEAR])) {
                                //Log.d("timer","date is yesterday or today" + theDay.toString());

                                //SharedPrefHelper.setSharedOBJECT(context,"ticket",person);
                                val intent = Intent(context, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
                                Log.d("timer", context.toString())
                                var notificationLayout = RemoteViews(packageName, R.layout.notification)
                                var notificationLayoutExpanded = RemoteViews(packageName, R.layout.notification)
                                var onewin = false
                                val myPlays = SharedPrefHelper.getMyTickets(context, "powerball")
                                for (m in myPlays.indices) {
                                    val tick = myPlays[m]
                                    val log = RemoteViews(packageName, R.layout.balls)
                                    //LayoutInflater inflater = (LayoutInflater)   context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    //View view = inflater.inflate(R.layout.balls, null);
                                    val ball1 = R.id.ball1
                                    val ball2 = R.id.ball2
                                    val ball3 = R.id.ball3
                                    val ball4 = R.id.ball4
                                    val ball5 = R.id.ball5
                                    val ball6 = R.id.ball6
                                    val multi = R.id.win
                                    //multi.setText("Multiplier: " + mDataset.get(position).multi);
                                    //TextView win = (TextView)view.findViewById(R.id.win);
                                    //win.setText(holder.context.getResources().getString(R.string.remove));
                                    val images = intArrayOf(ball1, ball2, ball3, ball4, ball5, ball6)
                                    //String num = mDataset.get(position).ticket;
                                    var num = ""
                                    var win = ""
                                    num = tick.ticket.toString()
                                    win = person.calculateWin(num)

                                    //String num = person.ticket1;
                                    val split = num.split(" ").toTypedArray()
                                    val winner = person.winningNumber?.split(" ")!!.toTypedArray()
                                    for (j in split.indices) {
                                        //images[j].setText(split[j]);
                                        if (j == split.size - 1) {
                                            if (split[j].toInt() == winner[j].toInt()) {
                                                log.setInt(images[j], "setBackgroundResource", R.drawable.powerstar1)
                                                log.setTextColor(images[j], Color.BLACK)
                                                log.setTextViewText(images[j], split[j])
                                            } else {
                                                log.setTextColor(images[j], Color.BLACK)
                                                log.setTextViewText(images[j], split[j])
                                            }
                                        } else {
                                            for (z in 0 until split.size - 1) {
                                                if (split[j].toInt() == winner[z].toInt()) {
                                                    log.setInt(images[j], "setBackgroundResource", R.drawable.ball1star)
                                                    log.setTextColor(images[j], Color.BLACK)
                                                    log.setTextViewText(images[j], split[j])
                                                } else {
                                                    log.setTextColor(images[j], Color.BLACK)
                                                    log.setTextViewText(images[j], split[j])
                                                }
                                            }
                                        }
                                    }
                                    log.setTextViewText(multi, win)
                                    //holder.linear.addView(view);
                                    if (win != "Nothing :(") {
                                        onewin = true
                                        notificationLayout.addView(R.id.lin, log)
                                        notificationLayoutExpanded.addView(R.id.lin, log)
                                    }
                                }
                                val mBuilder: NotificationCompat.Builder
                                if (!onewin) {
                                    notificationLayout = RemoteViews(packageName, R.layout.nowin)
                                    notificationLayoutExpanded = RemoteViews(packageName, R.layout.nowin)
                                    mBuilder = NotificationCompat.Builder(context, "10101010101")
                                            .setSmallIcon(R.drawable.ic_stat_name)
                                            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                                            .setCustomContentView(notificationLayout)
                                            .setCustomBigContentView(notificationLayoutExpanded)
                                            .setContentIntent(pendingIntent)
                                            .setAutoCancel(true)
                                } else {
                                    mBuilder = NotificationCompat.Builder(context, "10101010101")
                                            .setSmallIcon(R.drawable.ic_stat_name)
                                            .setContentTitle("Powerball/Megamillions")
                                            .setCustomContentView(notificationLayout)
                                            .setCustomBigContentView(notificationLayoutExpanded)
                                            .setContentIntent(pendingIntent)
                                            .setAutoCancel(true)
                                }
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
                                val notificationManager = NotificationManagerCompat.from(context)

                                // notificationId is a unique int for each notification that you must define
                                notificationManager.notify(Random().nextInt(), mBuilder.build())
                                stopSelf()
                                break
                            } else {
                                break
                            }
                        }
                    }
                }

                // stopSelf();
            }
        }
    }

    companion object {
        const val notify = 5 * 60 * 1000 //interval between two services(Here Service run every 5 minutes)
    }
}