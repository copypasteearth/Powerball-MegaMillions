package powerball.apps.jacs.powerball;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MyTicketAdapter extends RecyclerView.Adapter<MyTicketAdapter.MyViewHolder> {
    public ArrayList<MyTicket> mDataset;
    private String type;
    public Fragment numbers;
    private OnRemoveListener listener;

    public void setOnRemoveListener(OnRemoveListener listen){
        listener = listen;
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //CardView cv;
        LinearLayout linear;
        Context context;

        public MyViewHolder(View itemView, Context context) {
            super(itemView);
            //cv = (CardView) itemView.findViewById(R.id.cv);
            linear = (LinearLayout) itemView.findViewById(R.id.linear);
            this.context = context;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyTicketAdapter(Fragment num,ArrayList<MyTicket> myDataset, String type) {
        mDataset = myDataset;
        this.type = type;
        this.numbers = num;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyTicketAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        MyViewHolder vh = new MyViewHolder(v,parent.getContext());
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if(type.equals("powerball")){
            final View view;
            LayoutInflater inflater = (LayoutInflater)   holder.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.balls, null);
            TextView ball1 = (TextView) view.findViewById(R.id.ball1);
            TextView ball2 = (TextView) view.findViewById(R.id.ball2);
            TextView ball3 = (TextView) view.findViewById(R.id.ball3);
            TextView ball4 = (TextView) view.findViewById(R.id.ball4);
            TextView ball5 = (TextView) view.findViewById(R.id.ball5);
            TextView ball6 = (TextView) view.findViewById(R.id.ball6);
            TextView multi = (TextView) view.findViewById(R.id.multi);
            multi.setText("Multiplier: " + mDataset.get(position).multi);
            TextView win = (TextView)view.findViewById(R.id.win);
            win.setText(holder.context.getResources().getString(R.string.remove));

            TextView[] images = {ball1,ball2,ball3,ball4,ball5,ball6};
            //String num = mDataset.get(position).ticket;
            String num = mDataset.get(position).ticket;
            String[] split = num.split(" ");
            for(int j = 0;j < split.length;j++){
                images[j].setText(split[j]);
            }
            holder.linear.addView(view);
            win.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //holder.linear.removeView(view);
                    //mDataset.remove(holder.getAdapterPosition());
                    //numbers.removeView("powerball",holder.getAdapterPosition());
                   // mDataset.remove(holder.getAdapterPosition());
                   // Log.d("adapter",mDataset.size()+"");

                   // notifyItemRemoved(holder.getAdapterPosition());
                   // notifyItemRangeChanged(holder.getAdapterPosition(), getItemCount());
                    listener.onRemove(holder.getAdapterPosition());
                    //notifyDataSetChanged();

                    //SharedPrefHelper.setMyTickets(holder.context,mDataset,"powerball");
                }
            });
        }else{
            View view;
            LayoutInflater inflater = (LayoutInflater)   holder.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.balls1, null);
            TextView ball1 = (TextView) view.findViewById(R.id.ball1m);
            TextView ball2 = (TextView) view.findViewById(R.id.ball2m);
            TextView ball3 = (TextView) view.findViewById(R.id.ball3m);
            TextView ball4 = (TextView) view.findViewById(R.id.ball4m);
            TextView ball5 = (TextView) view.findViewById(R.id.ball5m);
            TextView ball6 = (TextView) view.findViewById(R.id.ball6m);
            TextView multi = (TextView) view.findViewById(R.id.multim);
            multi.setText("Multiplier: " + mDataset.get(position).multi);
            TextView win = (TextView)view.findViewById(R.id.winm);
            win.setText(holder.context.getResources().getString(R.string.remove));
            TextView[] images = {ball1,ball2,ball3,ball4,ball5,ball6};
            String num = mDataset.get(position).ticket;
            String[] split = num.split(" ");
            for(int j = 0;j < split.length;j++){
                images[j].setText(split[j]);
            }
            holder.linear.addView(view);
            win.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //holder.linear.removeView(view);
                   // mDataset.remove(holder.getAdapterPosition());

                    //notifyItemRemoved(holder.getAdapterPosition());
                    //notifyItemRangeChanged(holder.getAdapterPosition(), 1);
                    //notifyDataSetChanged();
                    listener.onRemove(holder.getAdapterPosition());
                    //SharedPrefHelper.setMyTickets(holder.context,mDataset,"megamillions");
                }
            });
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
interface OnRemoveListener{
    void onRemove(int position);
}