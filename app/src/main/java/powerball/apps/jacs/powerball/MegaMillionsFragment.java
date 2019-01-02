package powerball.apps.jacs.powerball;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class MegaMillionsFragment extends Fragment {
    public ArrayList<WinningTicket> tickets = new ArrayList<>();
    public SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
    public Calendar now,past;
    public RequestQueue requestQueue;
    public int counter = 0;

    public MegaMillionsFragment() {
        // Required empty public constructor
    }


    public static MegaMillionsFragment newInstance(String param1, String param2) {
        MegaMillionsFragment fragment = new MegaMillionsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mega_millions, container, false);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRecyclerView(getActivity());
        Log.d("timer","onActivityCreated");
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d("timer","onstart");
        //setRecyclerView(getActivity());
    }
    public void setRecyclerView(Context mContext) {
        counter = 0;
        tickets = new ArrayList<>();
        RecyclerView rv = (RecyclerView) getView().findViewById(R.id.recycle1);
        rv.removeAllViews();
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        rv.setLayoutManager(llm);

        final RVAdapter rvAdapter = new RVAdapter(tickets, rv, Constants.MEGA_MILLIONS);
        rv.setAdapter(rvAdapter);

        requestQueue = Volley.newRequestQueue(mContext);
        now = Calendar.getInstance();
        past = Calendar.getInstance();
        past.add(Calendar.MONTH, -1);
        //String url = "http://data.ny.gov/resource/d6yy-54nr.json";
        String url = "https://www.megamillions.com/cmspages/utilservice.asmx/GetDrawingPagingData?pageNumber=1&pageSize=21&startDate=" + inputFormat.format(past.getTime()) + "&endDate=" + inputFormat.format(now.getTime());
        Log.d("timer", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {

                            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                            factory.setNamespaceAware(true);
                            XmlPullParser parser = factory.newPullParser();
                            parser.setInput(new StringReader(response));
                            int eventType = parser.getEventType();

                            while (eventType != XmlPullParser.END_DOCUMENT){

                                if(eventType== XmlPullParser.START_TAG){

                                    String name = parser.getName();
                                    if(name.equals("string")){
                                        if(parser.next() == XmlPullParser.TEXT) {
                                            String json = parser.getText();
                                            JSONObject object = new JSONObject(json);
                                            JSONArray array = object.getJSONArray("DrawingData");
                                            for(int i = 0;i < array.length();i++){
                                                JSONObject jsonObject = array.getJSONObject(i);
                                                WinningTicket person = new WinningTicket();
                                                int ball1 = 0,ball2=0,ball3=0,ball4=0,ball5=0,ball6=0,mega;
                                                if (!jsonObject.isNull("PlayDate")) {
                                                    person.date = jsonObject.getString("PlayDate");
                                                }
                                                if (!jsonObject.isNull("N1")) {
                                                    ball1 = jsonObject.getInt("N1");
                                                }
                                                if (!jsonObject.isNull("N2")) {
                                                    ball2 = jsonObject.getInt("N2");
                                                }
                                                if (!jsonObject.isNull("N3")) {
                                                    ball3 = jsonObject.getInt("N3");
                                                }
                                                if (!jsonObject.isNull("N4")) {
                                                    ball4 = jsonObject.getInt("N4");
                                                }
                                                if (!jsonObject.isNull("N5")) {
                                                    ball5 = jsonObject.getInt("N5");
                                                }
                                                if (!jsonObject.isNull("MBall")) {
                                                    ball6 = jsonObject.getInt("MBall");
                                                }
                                                if (!jsonObject.isNull("Megaplier")) {
                                                    person.multiplier = jsonObject.getString("Megaplier");
                                                }
                                                person.winningNumber = ball1 + "," + ball2 + "," + ball3 + ","+ball4+","+ball5+","+ball6;
                                                //if(!first){
                                                //    WinningTicket tick = SharedPrefHelper.getSharedOBJECT(getApplicationContext(),"ticket");
                                                //    if(tick == null || !tick.equals(person)){
                                                //        SharedPrefHelper.setSharedOBJECT(getApplicationContext(),"ticket",person);
                                                //    }
                                                //    first = true;
                                                //}
                                                tickets.add(counter, person);
                                                rvAdapter.notifyItemInserted(counter);
                                                counter++;
                                            }
                                            Log.d("timer","json:" + json);
                                        }


                                    }


                                }else if(eventType== XmlPullParser.END_TAG){


                                }
                                eventType = parser.next();

                            }


                        }catch (Exception e){
                            Log.d("timer","Error in ParseXML()",e);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // handle error response
                    }
                }
        );
      /*  StringRequest jsonArrayRequest = new StringRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if (response.length() > 0) {
                        Log.d("timer","response"+response.toString());
                        tickets.clear();
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
                            //if(!first){
                            //    WinningTicket tick = SharedPrefHelper.getSharedOBJECT(getApplicationContext(),"ticket");
                            //    if(tick == null || !tick.equals(person)){
                            //        SharedPrefHelper.setSharedOBJECT(getApplicationContext(),"ticket",person);
                            //    }
                            //    first = true;
                            //}
                            tickets.add(counter, person);
                            rvAdapter.notifyItemInserted(counter);
                            counter++;
                        }
                        // rvAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // do something
                Log.d("timer","error : "+error.getLocalizedMessage() + error.networkResponse.statusCode);
            }
        });
        */

        requestQueue.add(stringRequest);

        rvAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                Log.d("timer","loading more");
                tickets.add(null);
                rvAdapter.notifyItemInserted(tickets.size() - 1);

                now = (Calendar)past.clone();
                past.add(Calendar.MONTH,-1);
                Log.d("timer","dates:::::: "+ now.getTime().toString() + "  :  " + past.getTime().toString());
                String url = "https://www.megamillions.com/cmspages/utilservice.asmx/GetDrawingPagingData?pageNumber=1&pageSize=21&startDate=" + inputFormat.format(past.getTime()) + "&endDate=" + inputFormat.format(now.getTime());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    //TODO remove the loading null element from list and notify the adapter
                                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                                    factory.setNamespaceAware(true);
                                    XmlPullParser parser = factory.newPullParser();
                                    parser.setInput(new StringReader(response));
                                    int eventType = parser.getEventType();

                                    while (eventType != XmlPullParser.END_DOCUMENT){

                                        if(eventType== XmlPullParser.START_TAG){

                                            String name = parser.getName();
                                            if(name.equals("string")){
                                                if(parser.next() == XmlPullParser.TEXT) {
                                                    String json = parser.getText();
                                                    JSONObject object = new JSONObject(json);
                                                    JSONArray array = object.getJSONArray("DrawingData");
                                                    for(int i = 0;i < array.length();i++){
                                                        JSONObject jsonObject = array.getJSONObject(i);
                                                        WinningTicket person = new WinningTicket();
                                                        int ball1 = 0,ball2=0,ball3=0,ball4=0,ball5=0,ball6=0,mega;
                                                        if (!jsonObject.isNull("PlayDate")) {
                                                            person.date = jsonObject.getString("PlayDate");
                                                        }
                                                        if (!jsonObject.isNull("N1")) {
                                                            ball1 = jsonObject.getInt("N1");
                                                        }
                                                        if (!jsonObject.isNull("N2")) {
                                                            ball2 = jsonObject.getInt("N2");
                                                        }
                                                        if (!jsonObject.isNull("N3")) {
                                                            ball3 = jsonObject.getInt("N3");
                                                        }
                                                        if (!jsonObject.isNull("N4")) {
                                                            ball4 = jsonObject.getInt("N4");
                                                        }
                                                        if (!jsonObject.isNull("N5")) {
                                                            ball5 = jsonObject.getInt("N5");
                                                        }
                                                        if (!jsonObject.isNull("MBall")) {
                                                            ball6 = jsonObject.getInt("MBall");
                                                        }
                                                        if (!jsonObject.isNull("Megaplier")) {
                                                            person.multiplier = jsonObject.getString("Megaplier");
                                                        }
                                                        person.winningNumber = ball1 + "," + ball2 + "," + ball3 + ","+ball4+","+ball5+","+ball6;
                                                        //if(!first){
                                                        //    WinningTicket tick = SharedPrefHelper.getSharedOBJECT(getApplicationContext(),"ticket");
                                                        //    if(tick == null || !tick.equals(person)){
                                                        //        SharedPrefHelper.setSharedOBJECT(getApplicationContext(),"ticket",person);
                                                        //    }
                                                        //    first = true;
                                                        //}
                                                        if(!tickets.contains(person)) {
                                                            tickets.add(counter, person);
                                                            rvAdapter.notifyItemInserted(counter);
                                                            counter++;
                                                        }
                                                    }
                                                    Log.d("timer","json:" + json);
                                                    rvAdapter.setLoaded();
                                                }


                                            }


                                        }else if(eventType== XmlPullParser.END_TAG){


                                        }
                                        eventType = parser.next();

                                    }


                                }catch (Exception e){
                                    Log.d("timer","Error in ParseXML()",e);
                                }
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // handle error response
                            }
                        });
                requestQueue.add(stringRequest);
            }
        });
    }
}



