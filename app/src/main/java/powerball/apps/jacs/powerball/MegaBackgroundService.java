/*
 * Author: John Rowan
 * Description: background service to get latest mega millions winning numbers and compare them with
 * the users tickets and send a notification with the results.
 * Anyone may use this file or anything contained in this project for their own personal use.
 */

package powerball.apps.jacs.powerball;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MegaBackgroundService extends Service {
    public static final int notify = 5 * 60 * 1000;  //interval between two services(Here Service run every 5 minutes)
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;
    public RequestQueue requestQueue;

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
     * Description: makes a notification and calls startForeground as required from oreo and up
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
                        .setContentTitle(getText(R.string.megamillions))
                        .setContentText(getText(R.string.numbers))
                        .setSmallIcon(R.drawable.powerstar1)
                        .setContentIntent(pendingIntent)
                        .setTicker(getText(R.string.megamillions))
                        .build();
        startForeground(Constants.MEGA_FOREGROUND,notification);
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

                    String url = "https://www.megamillions.com/cmspages/utilservice.asmx/GetDrawingPagingData?pageNumber=1&pageSize=1&startDate=&endDate=";
                    /**
                     * Description: jsonArrayRequest which is really a string request and it gets the latest
                     * mega millions numbers in xml format and then it compares if the number is the current day or
                     * the day before then checks if any of the users numbers are winners and sends notification.
                     */
                    StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                    //tickets.clear();
                                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                                factory.setNamespaceAware(true);
                                XmlPullParser parser = factory.newPullParser();
                                parser.setInput(new StringReader(response));
                                int eventType = parser.getEventType();

                                while (eventType != XmlPullParser.END_DOCUMENT){

                                    if(eventType== XmlPullParser.START_TAG){

                                        String name1 = parser.getName();
                                        if(name1.equals("string")){
                                            if(parser.next() == XmlPullParser.TEXT) {
                                                String json = parser.getText();
                                                JSONObject object = new JSONObject(json);
                                                JSONArray array = object.getJSONArray("DrawingData");
                                                for(int i = 0;i < array.length();i++){
                                                    JSONObject jsonObject = array.getJSONObject(i);
                                                    WinningTicket person = new WinningTicket();
                                                    int ball1m = 0,ball2m=0,ball3m=0,ball4m=0,ball5m=0,ball6m=0,mega;
                                                    if (!jsonObject.isNull("PlayDate")) {
                                                        person.date = jsonObject.getString("PlayDate");
                                                    }
                                                    if (!jsonObject.isNull("N1")) {
                                                        ball1m = jsonObject.getInt("N1");
                                                    }
                                                    if (!jsonObject.isNull("N2")) {
                                                        ball2m = jsonObject.getInt("N2");
                                                    }
                                                    if (!jsonObject.isNull("N3")) {
                                                        ball3m = jsonObject.getInt("N3");
                                                    }
                                                    if (!jsonObject.isNull("N4")) {
                                                        ball4m = jsonObject.getInt("N4");
                                                    }
                                                    if (!jsonObject.isNull("N5")) {
                                                        ball5m = jsonObject.getInt("N5");
                                                    }
                                                    if (!jsonObject.isNull("MBall")) {
                                                        ball6m = jsonObject.getInt("MBall");
                                                    }
                                                    if (!jsonObject.isNull("Megaplier")) {
                                                        person.multiplier = jsonObject.getString("Megaplier");
                                                    }
                                                    person.winningNumber = ball1m + "," + ball2m + "," + ball3m + ","+ball4m+","+ball5m+","+ball6m;
                                                    SimpleDateFormat jsonFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
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

                                                       // SharedPrefHelper.setSharedOBJECT(context,"ticket",person);
                                                        Intent intent = new Intent(context, MainActivity.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                                                        Log.d("timer",context.toString());
                                                        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification);
                                                        RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.notification);
                                                        boolean onewin = false;
                                                        ArrayList<MyTicket> myPlays = SharedPrefHelper.getMyTickets(context,"megamillions");
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
                                                                        log.setInt(images[j], "setBackgroundResource", R.drawable.megastar1);
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
                                                                    //.setSmallIcon(R.drawable.ic_launcher_background)
                                                                    //.setContentTitle("my Powerball")
                                                                    //.setContentText("ticket1: " + person.ticket1 + " := " + person.calculateWin(person.ticket1) + "\n"
                                                                    // + "ticket2: " + person.ticket2 + " := " + person.calculateWin(person.ticket2) + "\n"
                                                                    // + "ticket3: " + person.ticket3 + " := " + person.calculateWin(person.ticket3) + "\n")
                                                                    //.setStyle(new NotificationCompat.BigTextStyle()
                                                                    //.bigText("ticket1: " + person.ticket1 + " := " + person.calculateWin(person.ticket1) + "\n"
                                                                    //        + "ticket2: " + person.ticket2 + " := " + person.calculateWin(person.ticket2) + "\n"
                                                                    //       + "ticket3: " + person.ticket3 + " := " + person.calculateWin(person.ticket3) + "\n"))
                                                                    // .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                                    .setContentIntent(pendingIntent)
                                                                    .setAutoCancel(true);
                                                        }else{
                                                            mBuilder = new NotificationCompat.Builder(context, "10101010101")

                                                                    .setSmallIcon(R.drawable.ic_stat_name)
                                                                    .setContentTitle("Powerball/Megamillions")
                                                                    //.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                                                                    .setCustomContentView(notificationLayout)
                                                                    .setCustomBigContentView(notificationLayoutExpanded)
                                                                    //.setSmallIcon(R.drawable.ic_launcher_background)
                                                                    //.setContentTitle("my Powerball")
                                                                    //.setContentText("ticket1: " + person.ticket1 + " := " + person.calculateWin(person.ticket1) + "\n"
                                                                    // + "ticket2: " + person.ticket2 + " := " + person.calculateWin(person.ticket2) + "\n"
                                                                    // + "ticket3: " + person.ticket3 + " := " + person.calculateWin(person.ticket3) + "\n")
                                                                    //.setStyle(new NotificationCompat.BigTextStyle()
                                                                    //.bigText("ticket1: " + person.ticket1 + " := " + person.calculateWin(person.ticket1) + "\n"
                                                                    //        + "ticket2: " + person.ticket2 + " := " + person.calculateWin(person.ticket2) + "\n"
                                                                    //       + "ticket3: " + person.ticket3 + " := " + person.calculateWin(person.ticket3) + "\n"))
                                                                    // .setPriority(NotificationCompat.PRIORITY_DEFAULT)
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


                                        }


                                    }else if(eventType== XmlPullParser.END_TAG){


                                    }
                                    eventType = parser.next();

                                }

                                    //rvAdapter.notifyDataSetChanged();

                            } catch (Exception e) {
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
