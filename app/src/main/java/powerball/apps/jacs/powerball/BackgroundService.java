/*
 * Author: John Rowan
 * Description: This is a service to check for the latest powerball numbers and send a notification
 * if the user has won or has not won
 * Anyone may use this file or anything contained in this project for their own personal use.
 */

package powerball.apps.jacs.powerball;

import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.os.*;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundService extends Service {

    public static final int notify = 5 * 60 * 1000;  //interval between two services(Here Service run every 5 minutes)
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;
    public  RequestQueue requestQueue;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        requestQueue = Volley.newRequestQueue(this);
        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else
            mTimer = new Timer();   //recreate new
        mTimer.scheduleAtFixedRate(new TimeDisplay(this), 0, notify);
        Log.d("timer","timer started");
    }



    @Override
    public void onDestroy() {
        mTimer.cancel();
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
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Powerball";
            String description = "Powerball Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("10101010101", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        Notification notification =
                new NotificationCompat.Builder(this, "10101010101")
                        .setContentTitle(getText(R.string.powerball))
                        .setContentText(getText(R.string.numbers))
                        .setSmallIcon(R.drawable.powerstar1)
                        .setContentIntent(pendingIntent)
                        .setTicker(getText(R.string.powerball))
                        .build();
        startForeground(Constants.POWER_FOREGROUND,notification);
        return START_STICKY;
    }


    class TimeDisplay extends TimerTask {
        public Context context;
        public TimeDisplay(Context context){
            this.context = context;
        }
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    String url = "https://www.powerball.com/api/v1/numbers/powerball/recent10?_format=json";
                    /**
                     * Description: this jsonArrayRequest requests the powerball numbers from the url above and
                     * gets the latest number and if it is either the day before or that present day then it will do
                     * the calculation based on your saved tickets and notify the user if their tickets are winners or not
                     */
                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                if (response.length() > 0) {
                                    //tickets.clear();
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        WinningTicket person = new WinningTicket();
                                        if (!jsonObject.isNull("field_draw_date")) {
                                            person.date = jsonObject.getString("field_draw_date");
                                        }
                                        if (!jsonObject.isNull("field_winning_numbers")) {
                                            person.winningNumber = jsonObject.getString("field_winning_numbers");
                                        }
                                        if (!jsonObject.isNull("field_multiplier")) {
                                            person.multiplier = jsonObject.getString("field_multiplier");
                                        }
                                        //tickets.add(i, person);
                                       // WinningTicket prev = SharedPrefHelper.getSharedOBJECT(context,"ticket");
                                        SimpleDateFormat jsonFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        Calendar theDay = Calendar.getInstance();
                                        Calendar yest = Calendar.getInstance();
                                        yest.add(Calendar.DAY_OF_YEAR,-1);
                                        //Log.d("timer","yest : " + yest.toString());
                                        Calendar now = Calendar.getInstance();
                                        try {
                                            Date yesterday = jsonFormat.parse(person.date);
                                            theDay.setTime(yesterday);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        if ((now.get(Calendar.YEAR) == theDay.get(Calendar.YEAR)
                                                && now.get(Calendar.DAY_OF_YEAR) == theDay.get(Calendar.DAY_OF_YEAR))||
                                                (yest.get(Calendar.YEAR) == theDay.get(Calendar.YEAR)
                                                        && yest.get(Calendar.DAY_OF_YEAR) == theDay.get(Calendar.DAY_OF_YEAR))) {
                                            //Log.d("timer","date is yesterday or today" + theDay.toString());

                                            //SharedPrefHelper.setSharedOBJECT(context,"ticket",person);
                                            Intent intent = new Intent(context, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                                            Log.d("timer",context.toString());
                                            RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification);
                                           RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.notification);
                                           boolean onewin = false;
                                            ArrayList<MyTicket> myPlays = SharedPrefHelper.getMyTickets(context,"powerball");
                                           for(int m = 0;m < myPlays.size();m++){
                                               MyTicket tick = myPlays.get(m);
                                               RemoteViews log = new RemoteViews(getPackageName(), R.layout.balls);
                                               //LayoutInflater inflater = (LayoutInflater)   context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                               //View view = inflater.inflate(R.layout.balls, null);
                                               int ball1 = R.id.ball1;
                                               int ball2 = R.id.ball2;
                                               int ball3 = R.id.ball3;
                                               int ball4 = R.id.ball4;
                                               int ball5 = R.id.ball5;
                                               int ball6 = R.id.ball6;
                                               int multi = R.id.win;
                                               //multi.setText("Multiplier: " + mDataset.get(position).multi);
                                               //TextView win = (TextView)view.findViewById(R.id.win);
                                               //win.setText(holder.context.getResources().getString(R.string.remove));

                                               int[] images = {ball1,ball2,ball3,ball4,ball5,ball6};
                                               //String num = mDataset.get(position).ticket;
                                               String num = "";
                                               String win = "";
                                               num = tick.ticket;
                                               win = person.calculateWin(num);

                                               //String num = person.ticket1;
                                               String[] split = num.split(" ");
                                               String[] winner = person.winningNumber.split(",");
                                               for(int j = 0;j < split.length;j++){
                                                   //images[j].setText(split[j]);
                                                   if(j == split.length - 1){
                                                       if(Integer.parseInt(split[j])==Integer.parseInt(winner[j])){
                                                           log.setInt(images[j], "setBackgroundResource", R.drawable.powerstar1);
                                                           log.setTextColor(images[j], Color.BLACK);
                                                           log.setTextViewText(images[j],split[j]);
                                                       }else{
                                                           log.setTextColor(images[j], Color.BLACK);
                                                           log.setTextViewText(images[j],split[j]);
                                                       }
                                                   }else{
                                                       for(int z = 0;z < split.length -1;z++){
                                                           if(Integer.parseInt(split[j])==Integer.parseInt(winner[z])){
                                                               log.setInt(images[j], "setBackgroundResource", R.drawable.ball1star);
                                                               log.setTextColor(images[j], Color.BLACK);
                                                               log.setTextViewText(images[j],split[j]);
                                                           }else{
                                                               log.setTextColor(images[j], Color.BLACK);
                                                               log.setTextViewText(images[j],split[j]);
                                                           }
                                                       }

                                                   }

                                               }
                                               log.setTextViewText(multi,win);
                                               //holder.linear.addView(view);
                                               if(!win.equals("Nothing :(")){
                                                   onewin = true;
                                                   notificationLayout.addView(R.id.lin,log);
                                                   notificationLayoutExpanded.addView(R.id.lin,log);
                                               }

                                           }
                                            NotificationCompat.Builder mBuilder;
                                           if(!onewin){
                                               notificationLayout = new RemoteViews(getPackageName(), R.layout.nowin);
                                               notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.nowin);
                                               mBuilder = new NotificationCompat.Builder(context, "10101010101")

                                                       .setSmallIcon(R.drawable.ic_stat_name)
                                                       .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                                                       .setCustomContentView(notificationLayout)
                                                       .setCustomBigContentView(notificationLayoutExpanded)
                                                       .setContentIntent(pendingIntent)
                                                       .setAutoCancel(true);
                                           }else{
                                               mBuilder = new NotificationCompat.Builder(context, "10101010101")

                                                       .setSmallIcon(R.drawable.ic_stat_name)
                                                       .setContentTitle("Powerball/Megamillions")
                                                       .setCustomContentView(notificationLayout)
                                                       .setCustomBigContentView(notificationLayoutExpanded)
                                                       .setContentIntent(pendingIntent)
                                                       .setAutoCancel(true);
                                           }



                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                CharSequence name = "Powerball";
                                                String description = "Powerball Notification";
                                                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                                                NotificationChannel channel = new NotificationChannel("10101010101", name, importance);
                                                channel.setDescription(description);
                                                // Register the channel with the system; you can't change the importance
                                                // or other notification behaviors after this
                                                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                                notificationManager.createNotificationChannel(channel);
                                            }
                                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                                            // notificationId is a unique int for each notification that you must define
                                            notificationManager.notify(new Random().nextInt(), mBuilder.build());
                                            stopSelf();
                                            break;
                                        }else{
                                            break;
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // do something
                        }
                    });

                    requestQueue.add(jsonArrayRequest);
                   // stopSelf();

                }
            });

        }

    }

}