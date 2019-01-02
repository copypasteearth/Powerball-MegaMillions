/*
 * Author: John Rowan
 * Description: Adapter for recyclerviews from PowerballFragment and MegaMillionsFragment it does comparason
 * of users tickets with winning tickets and adds results to a view.
 * Anyone may use this file or anything contained in this project for their own personal use.
 */

package powerball.apps.jacs.powerball;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.*;

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<WinningTicket> tickets;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;
    private String type;

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }
    @Override
    public int getItemViewType(int position) {
        return tickets.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    RVAdapter(List<WinningTicket> tickets,RecyclerView recyclerView,String type) {
        //setHasStableIds(true);
        this.tickets = tickets;
        this.type = type;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
            PersonViewHolder pvh = new PersonViewHolder(v,parent.getContext());
            return pvh;
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view,parent.getContext());
        }

return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder1, int position) {

        if (holder1 instanceof PersonViewHolder) {
            PersonViewHolder holder = (PersonViewHolder) holder1;
            View view;
            LayoutInflater inflater = (LayoutInflater)   holder.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TextView ball1,ball2,ball3,ball4,ball5,ball6,win;
            TextView date = new TextView(holder.context);
            date.setText("Date: " + tickets.get(holder1.getAdapterPosition()).date);
            TextView winning = new TextView(holder.context);
            winning.setText("Winning Number");
            if(type.equals(Constants.POWERBALL)){
                view = inflater.inflate(R.layout.balls, null);

                ball1 = (TextView) view.findViewById(R.id.ball1);
                ball2 = (TextView) view.findViewById(R.id.ball2);
                ball3 = (TextView) view.findViewById(R.id.ball3);
                ball4 = (TextView) view.findViewById(R.id.ball4);
                ball5 = (TextView) view.findViewById(R.id.ball5);
                ball6 = (TextView) view.findViewById(R.id.ball6);
                win = (TextView)view.findViewById(R.id.win);
            }else{
                view = inflater.inflate(R.layout.balls1, null);

                ball1 = (TextView) view.findViewById(R.id.ball1m);
                ball2 = (TextView) view.findViewById(R.id.ball2m);
                ball3 = (TextView) view.findViewById(R.id.ball3m);
                ball4 = (TextView) view.findViewById(R.id.ball4m);
                ball5 = (TextView) view.findViewById(R.id.ball5m);
                ball6 = (TextView) view.findViewById(R.id.ball6m);
                win = (TextView)view.findViewById(R.id.winm);
            }

            win.setText("");
            TextView[] images = {ball1,ball2,ball3,ball4,ball5,ball6};
            String num = tickets.get(position).winningNumber;
            String[] split = num.split(",");
            for(int j = 0;j < split.length;j++){
                images[j].setText(split[j]);
            }
            holder.linear.addView(date);
            holder.linear.addView(winning);
            holder.linear.addView(view);

            ArrayList<MyTicket> myPick;
            if(type.equals(Constants.POWERBALL)){
                myPick = SharedPrefHelper.getMyTickets(holder.context,"powerball");
            }else{
                myPick = SharedPrefHelper.getMyTickets(holder.context,"megamillions");
            }
            for(int i = 0;i < myPick.size();i++){
                MyTicket tick1 = myPick.get(i);
                TextView ticknum = new TextView(holder.context);
                ticknum.setText("Ticket " + (i + 1));
                View view1;
                LayoutInflater inflater1 = (LayoutInflater)   holder.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                TextView ball11,ball21,ball31,ball41,ball51,ball61,end;
                if(type.equals(Constants.POWERBALL)){
                    view1 = inflater.inflate(R.layout.balls, null);

                    ball11 = (TextView) view1.findViewById(R.id.ball1);
                    ball21 = (TextView) view1.findViewById(R.id.ball2);
                    ball31 = (TextView) view1.findViewById(R.id.ball3);
                    ball41 = (TextView) view1.findViewById(R.id.ball4);
                    ball51 = (TextView) view1.findViewById(R.id.ball5);
                    ball61 = (TextView) view1.findViewById(R.id.ball6);
                    end = (TextView)view1.findViewById(R.id.win);
                }else{
                    view1 = inflater.inflate(R.layout.balls1, null);

                    ball11 = (TextView) view1.findViewById(R.id.ball1m);
                    ball21 = (TextView) view1.findViewById(R.id.ball2m);
                    ball31 = (TextView) view1.findViewById(R.id.ball3m);
                    ball41 = (TextView) view1.findViewById(R.id.ball4m);
                    ball51 = (TextView) view1.findViewById(R.id.ball5m);
                    ball61 = (TextView) view1.findViewById(R.id.ball6m);
                    end = (TextView)view1.findViewById(R.id.winm);
                }
                TextView[] images1 = {ball11,ball21,ball31,ball41,ball51,ball61};
                String num1 = tickets.get(position).winningNumber;
                String tick = "";
                tick = tick1.ticket;

                String[] split1 = num1.split(",");
                String[] splittick = tick.split(" ");
                if(type.equals(Constants.POWERBALL)){
                    for(int j = 0;j < split1.length;j++){
                        if(j == split1.length - 1){
                            if(split1[j].equals(splittick[j])){
                                images1[j].setBackground(ResourcesCompat.getDrawable(holder.context.getResources(), R.drawable.powerstar1, null));
                                images1[j].setText(splittick[j]);
                            }else{
                                images1[j].setText(splittick[j]);
                            }
                        }else{
                            for(int z = 0;z < split1.length - 1;z++){
                                if(split1[z].equals(splittick[j])){
                                    images1[j].setBackground(ResourcesCompat.getDrawable(holder.context.getResources(), R.drawable.ball1star, null));
                                    images1[j].setText(splittick[j]);
                                }else{
                                    images1[j].setText(splittick[j]);
                                }
                            }

                        }

                    }
                }else{
                    for(int j = 0;j < split1.length;j++){
                        if(j == split1.length - 1){
                            if(split1[j].equals(splittick[j])){
                                images1[j].setBackground(ResourcesCompat.getDrawable(holder.context.getResources(), R.drawable.megastar1, null));
                                images1[j].setText(splittick[j]);
                            }else{
                                images1[j].setText(splittick[j]);
                            }
                        }else{
                            for(int z = 0;z < split1.length - 1;z++){
                                if(split1[z].equals(splittick[j])){
                                    images1[j].setBackground(ResourcesCompat.getDrawable(holder.context.getResources(), R.drawable.ball1star, null));
                                    images1[j].setText(splittick[j]);
                                }else{
                                    images1[j].setText(splittick[j]);
                                }
                            }

                        }

                    }
                }

                //TextView end = (TextView) view1.findViewById(R.id.win);
                end.setText(tickets.get(position).calculateWin(tick));
                holder.linear.addView(ticknum);

                holder.linear.addView(view1);

            }
        } else if (holder1 instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder1;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }




        //holder.date.setText("Date: " + tickets.get(position).date);
        //holder.ticket.setText("Winning Numbers: " + tickets.get(position).winningNumber);
       // holder.multi.setText("Multiplier: " + tickets.get(position).multiplier);
        //holder.yourtickets.setText("ticket1: " + tickets.get(position).ticket1 + " := " + tickets.get(position).calculateWin(tickets.get(position).ticket1) + "\n"
        //+ "ticket2: " + tickets.get(position).ticket2 + " := " + tickets.get(position).calculateWin(tickets.get(position).ticket2) + "\n"
       // + "ticket3: " + tickets.get(position).ticket3 + " := " + tickets.get(position).calculateWin(tickets.get(position).ticket3) + "\n");
    }
    public void setLoaded() {
        isLoading = false;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (tickets != null) {
            return tickets.size();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class PersonViewHolder extends ViewHolder {

       public LinearLayout linear;
       public Context context;

        PersonViewHolder(View itemView,Context context) {
            super(itemView);
            itemView.setHasTransientState(true);

            linear = (LinearLayout) itemView.findViewById(R.id.linear);
            this.context = context;

        }
    }
    public class LoadingViewHolder extends ViewHolder {
        public ProgressBar progressBar;
        public Context context;

        public LoadingViewHolder(View view,Context con) {
            super(view);
            context = con;
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

}
