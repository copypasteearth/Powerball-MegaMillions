/*
 * Author: John Rowan
 * Description: Adapter for the recyclerview used to pick the users tickets
 * Anyone may use this file or anything contained in this project for their own personal use.
 */
package powerball.apps.jacs.powerball

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import powerball.apps.jacs.powerball.MyTicketAdapter.MyViewHolder
import java.util.*

class MyTicketAdapter     // Provide a suitable constructor (depends on the kind of dataset)
(var numbers: Fragment, var mDataset: ArrayList<MyTicket>, private val type: String) : RecyclerView.Adapter<MyViewHolder>() {
    public var listener: OnRemoveListener? = null
    /*fun setOnRemoveListener(listen: OnRemoveListener?) {
        listener = listen
    }*/

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class MyViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
        // each data item is just a string in this case
        //CardView cv;
        var linear: LinearLayout
        var context: Context

        init {
            //cv = (CardView) itemView.findViewById(R.id.cv);
            linear = itemView.findViewById<View>(R.id.linear) as LinearLayout
            this.context = context
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview, parent, false)
        return MyViewHolder(v, parent.context)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (type == "powerball") {
            val view: View
            val inflater = holder.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.balls, null)
            val ball1 = view.findViewById<View>(R.id.ball1) as TextView
            val ball2 = view.findViewById<View>(R.id.ball2) as TextView
            val ball3 = view.findViewById<View>(R.id.ball3) as TextView
            val ball4 = view.findViewById<View>(R.id.ball4) as TextView
            val ball5 = view.findViewById<View>(R.id.ball5) as TextView
            val ball6 = view.findViewById<View>(R.id.ball6) as TextView
            val multi = view.findViewById<View>(R.id.multi) as TextView
            multi.text = "Multiplier: " + mDataset[position].multi
            val win = view.findViewById<View>(R.id.win) as TextView
            win.text = holder.context.resources.getString(R.string.remove)
            val images = arrayOf(ball1, ball2, ball3, ball4, ball5, ball6)
            //String num = mDataset.get(position).ticket;
            val num = mDataset[position].ticket
            val split = num!!.split(" ").toTypedArray()
            for (j in split.indices) {
                images[j].text = split[j]
            }
            holder.linear.addView(view)
            win.setOnClickListener {
                //holder.linear.removeView(view);
                //mDataset.remove(holder.getAdapterPosition());
                //numbers.removeView("powerball",holder.getAdapterPosition());
                // mDataset.remove(holder.getAdapterPosition());
                // Log.d("adapter",mDataset.size()+"");

                // notifyItemRemoved(holder.getAdapterPosition());
                // notifyItemRangeChanged(holder.getAdapterPosition(), getItemCount());
                listener!!.onRemove(holder.adapterPosition)
                //notifyDataSetChanged();

                //SharedPrefHelper.setMyTickets(holder.context,mDataset,"powerball");
            }
        } else {
            val view: View
            val inflater = holder.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.balls1, null)
            val ball1 = view.findViewById<View>(R.id.ball1m) as TextView
            val ball2 = view.findViewById<View>(R.id.ball2m) as TextView
            val ball3 = view.findViewById<View>(R.id.ball3m) as TextView
            val ball4 = view.findViewById<View>(R.id.ball4m) as TextView
            val ball5 = view.findViewById<View>(R.id.ball5m) as TextView
            val ball6 = view.findViewById<View>(R.id.ball6m) as TextView
            val multi = view.findViewById<View>(R.id.multim) as TextView
            multi.text = "Multiplier: " + mDataset[position].multi
            val win = view.findViewById<View>(R.id.winm) as TextView
            win.text = holder.context.resources.getString(R.string.remove)
            val images = arrayOf(ball1, ball2, ball3, ball4, ball5, ball6)
            val num = mDataset[position].ticket
            val split = num!!.split(" ").toTypedArray()
            for (j in split.indices) {
                images[j].text = split[j]
            }
            holder.linear.addView(view)
            win.setOnClickListener {
                //holder.linear.removeView(view);
                // mDataset.remove(holder.getAdapterPosition());

                //notifyItemRemoved(holder.getAdapterPosition());
                //notifyItemRangeChanged(holder.getAdapterPosition(), 1);
                //notifyDataSetChanged();
                listener!!.onRemove(holder.adapterPosition)
                //SharedPrefHelper.setMyTickets(holder.context,mDataset,"megamillions");
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mDataset.size
    }
}

interface OnRemoveListener {
    fun onRemove(position: Int)
}