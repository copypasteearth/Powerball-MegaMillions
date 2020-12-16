package powerball.apps.jacs.powerball

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import android.widget.RemoteViews
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import powerball.apps.jacs.powerball.data.mega.MegamillionsData
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Implementation of App Widget functionality.
 */
class MegaMillionsWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.mega_millions_widget)
    views.removeAllViews(R.id.widget_layout)
    GlobalScope.launch {
        withContext(Dispatchers.IO){
            val url = "https://data.ny.gov/resource/5xaw-6ayf.json"
            val json = URL(url).readText()
            val gson = Gson()
            val megamillionsData = gson.fromJson(json, MegamillionsData::class.java)
            val ticket = megamillionsData[0]
                val person = WinningTicket()
                person.date = ticket.draw_date//jsonObject.getString("field_draw_date")
                person.winningNumber = ticket.winning_numbers + " " + ticket.mega_ball
                person.multiplier = ticket.multiplier


                    //Log.d("timer","date is yesterday or today" + theDay.toString());

                    // SharedPrefHelper.setSharedOBJECT(context,"ticket",person);
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
                    Log.d("timer", context.toString())
                    var onewin = false
                    val myPlays = SharedPrefHelper.getMyTickets(context, "megamillions")
                    for (m in myPlays.indices) {
                        val tick = myPlays[m]
                        val log = RemoteViews(context.packageName, R.layout.remote_balls)
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
                        win = person.calculateWin(num, tick.multi)

                        //String num = person.ticket1;
                        val split = num.split(" ").toTypedArray()
                        val winner = person.winningNumber!!.split(" ").toTypedArray()
                        for (j in split.indices) {
                            //images[j].setText(split[j]);
                            if (j == split.size - 1) {
                                if (split[j].toInt() == winner[j].toInt()) {
                                    log.setInt(images[j], "setBackgroundResource", R.drawable.megastar1)
                                    log.setTextColor(images[j], Color.BLACK)
                                    log.setTextViewText(images[j], split[j])
                                } else {
                                    log.setInt(images[j], "setBackgroundResource", R.drawable.mega1)
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
                        views.setTextViewText(R.id.widgettextview, ticket.draw_date)
                        views.addView(R.id.widget_layout,log)
                    }




            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }



}