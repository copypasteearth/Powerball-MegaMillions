/*
 * Author: John Rowan
 * Description: Adapter for recyclerviews from PowerballFragment and MegaMillionsFragment it does comparason
 * of users tickets with winning tickets and adds results to a view.
 * Anyone may use this file or anything contained in this project for their own personal use.
 */
package powerball.apps.jacs.powerball

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import java.util.*

class RVAdapter internal constructor(var tickets: List<WinningTicket?>?, recyclerView: RecyclerView, private val type: String) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private var onLoadMoreListener: OnLoadMoreListener? = null
    private val visibleThreshold = 1
    private var lastVisibleItem = 0
    private var totalItemCount = 0
    private var isLoading = false
    fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener?) {
        onLoadMoreListener = mOnLoadMoreListener
    }

    override fun getItemViewType(position: Int): Int {
        return if (tickets!![position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview, parent, false)
            return PersonViewHolder(v, parent.context)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            return LoadingViewHolder(view, parent.context)
        }

    }

    override fun onBindViewHolder(holder1: RecyclerView.ViewHolder, position: Int) {
        if (holder1 is PersonViewHolder) {
            val holder = holder1
            val view: View
            val inflater = holder.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val ball1: TextView
            val ball2: TextView
            val ball3: TextView
            val ball4: TextView
            val ball5: TextView
            val ball6: TextView
            val win: TextView
            val date = TextView(holder.context)
            date.textSize = 20f
            date.text = "Date: " + tickets!![holder1.getAdapterPosition()]!!.date
            val winning = TextView(holder.context)
            winning.text = "Winning Number"
            winning.textSize = 20f
            if (type == Constants.POWERBALL) {
                view = inflater.inflate(R.layout.balls, null)
                ball1 = view.findViewById<View>(R.id.ball1) as TextView
                ball2 = view.findViewById<View>(R.id.ball2) as TextView
                ball3 = view.findViewById<View>(R.id.ball3) as TextView
                ball4 = view.findViewById<View>(R.id.ball4) as TextView
                ball5 = view.findViewById<View>(R.id.ball5) as TextView
                ball6 = view.findViewById<View>(R.id.ball6) as TextView
                win = view.findViewById<View>(R.id.win) as TextView
            } else {
                view = inflater.inflate(R.layout.balls1, null)
                ball1 = view.findViewById<View>(R.id.ball1m) as TextView
                ball2 = view.findViewById<View>(R.id.ball2m) as TextView
                ball3 = view.findViewById<View>(R.id.ball3m) as TextView
                ball4 = view.findViewById<View>(R.id.ball4m) as TextView
                ball5 = view.findViewById<View>(R.id.ball5m) as TextView
                ball6 = view.findViewById<View>(R.id.ball6m) as TextView
                win = view.findViewById<View>(R.id.winm) as TextView
            }
            win.text = ""
            val images = arrayOf(ball1, ball2, ball3, ball4, ball5, ball6)
            val num = tickets!![position]!!.winningNumber
            val split = num?.split(" ")?.toTypedArray()
            for (j in split?.indices!!) {
                images[j].text = split?.get(j)
            }
            holder.linear.addView(date)
            holder.linear.addView(winning)
            holder.linear.addView(view)
            val myPick: ArrayList<MyTicket>
            myPick = if (type == Constants.POWERBALL) {
                SharedPrefHelper.getMyTickets(holder.context, "powerball")
            } else {
                SharedPrefHelper.getMyTickets(holder.context, "megamillions")
            }
            for (i in myPick.indices) {
                val tick1 = myPick[i]
                val ticknum = TextView(holder.context)
                ticknum.textSize = 20f
                ticknum.text = "Ticket " + (i + 1)
                var view1: View
                val inflater1 = holder.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                var ball11: TextView
                var ball21: TextView
                var ball31: TextView
                var ball41: TextView
                var ball51: TextView
                var ball61: TextView
                var end: TextView
                if (type == Constants.POWERBALL) {
                    view1 = inflater.inflate(R.layout.balls, null)
                    ball11 = view1.findViewById<View>(R.id.ball1) as TextView
                    ball21 = view1.findViewById<View>(R.id.ball2) as TextView
                    ball31 = view1.findViewById<View>(R.id.ball3) as TextView
                    ball41 = view1.findViewById<View>(R.id.ball4) as TextView
                    ball51 = view1.findViewById<View>(R.id.ball5) as TextView
                    ball61 = view1.findViewById<View>(R.id.ball6) as TextView
                    end = view1.findViewById<View>(R.id.win) as TextView
                } else {
                    view1 = inflater.inflate(R.layout.balls1, null)
                    ball11 = view1.findViewById<View>(R.id.ball1m) as TextView
                    ball21 = view1.findViewById<View>(R.id.ball2m) as TextView
                    ball31 = view1.findViewById<View>(R.id.ball3m) as TextView
                    ball41 = view1.findViewById<View>(R.id.ball4m) as TextView
                    ball51 = view1.findViewById<View>(R.id.ball5m) as TextView
                    ball61 = view1.findViewById<View>(R.id.ball6m) as TextView
                    end = view1.findViewById<View>(R.id.winm) as TextView
                }
                val images1 = arrayOf(ball11, ball21, ball31, ball41, ball51, ball61)
                val num1 = tickets!![position]!!.winningNumber
                var tick: String? = ""
                tick = tick1.ticket
                val split1 = num1?.split(" ")?.toTypedArray()
                val splittick = tick!!.split(" ").toTypedArray()
                if (type == Constants.POWERBALL) {
                    for (j in split1!!.indices) {
                        if (j == split1.size - 1) {
                            if (split1.get(j).toInt() == splittick[j].toInt()) {
                                images1[j].background = ResourcesCompat.getDrawable(holder.context.resources, R.drawable.powerstar1, null)
                                images1[j].text = splittick[j]
                            } else {
                                images1[j].text = splittick[j]
                            }
                        } else {
                            for (z in 0 until split1.size - 1) {
                                if (split1[z].toInt() == splittick[j].toInt()) {
                                    images1[j].background = ResourcesCompat.getDrawable(holder.context.resources, R.drawable.ball1star, null)
                                    images1[j].text = splittick[j]
                                } else {
                                    images1[j].text = splittick[j]
                                }
                            }
                        }
                    }
                } else {
                    for (j in split1!!.indices) {
                        if (j == split1.size - 1) {
                            if (split1[j].toInt() == splittick[j].toInt()) {
                                images1[j].background = ResourcesCompat.getDrawable(holder.context.resources, R.drawable.megastar1, null)
                                images1[j].text = splittick[j]
                            } else {
                                images1[j].text = splittick[j]
                            }
                        } else {
                            for (z in 0 until split1.size - 1) {
                                if (split1[z].toInt() == splittick[j].toInt()) {
                                    images1[j].background = ResourcesCompat.getDrawable(holder.context.resources, R.drawable.ball1star, null)
                                    images1[j].text = splittick[j]
                                } else {
                                    images1[j].text = splittick[j]
                                }
                            }
                        }
                    }
                }

                //TextView end = (TextView) view1.findViewById(R.id.win);
                end.text = tickets!![position]!!.calculateWin(tick, tick1.multi)
                holder.linear.addView(ticknum)
                holder.linear.addView(view1)
            }
        } else if (holder1 is LoadingViewHolder) {
            holder1.progressBar.isIndeterminate = true
        }


        //holder.date.setText("Date: " + tickets.get(position).date);
        //holder.ticket.setText("Winning Numbers: " + tickets.get(position).winningNumber);
        // holder.multi.setText("Multiplier: " + tickets.get(position).multiplier);
        //holder.yourtickets.setText("ticket1: " + tickets.get(position).ticket1 + " := " + tickets.get(position).calculateWin(tickets.get(position).ticket1) + "\n"
        //+ "ticket2: " + tickets.get(position).ticket2 + " := " + tickets.get(position).calculateWin(tickets.get(position).ticket2) + "\n"
        // + "ticket3: " + tickets.get(position).ticket3 + " := " + tickets.get(position).calculateWin(tickets.get(position).ticket3) + "\n");
    }

    fun setLoaded() {
        isLoading = false
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (tickets != null) {
            tickets!!.size
        } else 0
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    inner class PersonViewHolder internal constructor(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
        var linear: LinearLayout
        var context: Context

        init {
            itemView.setHasTransientState(true)
            linear = itemView.findViewById<View>(R.id.linear) as LinearLayout
            this.context = context
        }
    }

    inner class LoadingViewHolder(view: View, var context: Context) : RecyclerView.ViewHolder(view) {
        var progressBar: ProgressBar

        init {
            progressBar = view.findViewById<View>(R.id.progressBar1) as ProgressBar
        }
    }

    init {
        //setHasStableIds(true);
        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayoutManager!!.itemCount
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener!!.onLoadMore()
                    }
                    isLoading = true
                }
            }
        })
    }
}