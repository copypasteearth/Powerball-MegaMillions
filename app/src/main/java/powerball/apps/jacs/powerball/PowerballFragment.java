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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class PowerballFragment extends Fragment {
    public ArrayList<WinningTicket> tickets = new ArrayList<>();
    public SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public Calendar now,past;
    public RequestQueue requestQueue;
    public int counter = 0;
    public PowerballFragment() {
        // Required empty public constructor
    }

    public static PowerballFragment newInstance() {
        PowerballFragment fragment = new PowerballFragment();

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
        return inflater.inflate(R.layout.fragment_powerball, container, false);
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

    public void setRecyclerView(Context mContext){
        counter = 0;
        tickets = new ArrayList<>();
        RecyclerView rv = (RecyclerView)getView().findViewById(R.id.recycle);
        rv.removeAllViews();
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        rv.setLayoutManager(llm);

        final RVAdapter rvAdapter = new RVAdapter(tickets,rv,Constants.POWERBALL);
        rv.setAdapter(rvAdapter);

        requestQueue = Volley.newRequestQueue(mContext);
        now = Calendar.getInstance();
        past = Calendar.getInstance();
        past.add(Calendar.MONTH,-1);
        //String url = "http://data.ny.gov/resource/d6yy-54nr.json";
        String url = "https://www.powerball.com/api/v1/numbers/powerball?_format=json&min="+inputFormat.format(past.getTime()).replace(" ","%20")+"&max="+inputFormat.format(now.getTime()).replace(" ","%20");
        Log.d("timer",url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
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

        requestQueue.add(jsonArrayRequest);
        rvAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                Log.d("timer","loading more");
                tickets.add(null);
                rvAdapter.notifyItemInserted(tickets.size() - 1);

                now = (Calendar)past.clone();
                past.add(Calendar.MONTH,-1);
                Log.d("timer","dates:::::: "+ now.getTime().toString() + "  :  " + past.getTime().toString());
                String url = "https://www.powerball.com/api/v1/numbers/powerball?_format=json&min="+inputFormat.format(past.getTime()).replace(" ","%20")+"&max="+inputFormat.format(now.getTime()).replace(" ","%20");
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {
                                Log.d("timer","response"+response.toString());
                                //tickets.clear();
                                tickets.remove(tickets.size() - 1);
                                rvAdapter.notifyItemRemoved(tickets.size());

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
                                    //tickets.add( person);
                                    if(!tickets.contains(person)) {
                                        tickets.add(counter, person);
                                        rvAdapter.notifyItemInserted(counter);
                                        counter++;
                                    }
                                }
                               // rvAdapter.notifyDataSetChanged();
                                //contactAdapter.notifyDataSetChanged();
                                rvAdapter.setLoaded();

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
                requestQueue.add(jsonArrayRequest);
            }
        });
    }
}
