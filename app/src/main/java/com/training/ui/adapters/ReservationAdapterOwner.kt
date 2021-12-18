package com.training.ui.adapters;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.training.R
import com.training.application.MainApplication
import com.training.model.ReservationModel
import com.training.util.constants.ReservationStatus
import java.util.*
import kotlin.collections.ArrayList

class ReservationAdapterOwner(
    var setReservationStatus: (ReservationModel) -> Unit
) :
    RecyclerView.Adapter<ReservationAdapterOwner.ReservationAdapterOwnerViewHolder>() {
    private var Item_List = ArrayList<ReservationModel>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReservationAdapterOwnerViewHolder {
        return ReservationAdapterOwnerViewHolder(
            LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reservation_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ReservationAdapterOwnerViewHolder, position: Int) {
        val reservation = Item_List.get(position)
        holder.removeButton.visibility = View.GONE
        holder.location.text = reservation.location
        holder.price.text = "${reservation.price} ${MainApplication.getApplication().getString(R.string.egp)}"
        holder.game.text = when {
            reservation.game.equals("football") -> MainApplication.getApplication().getString(R.string.football)
            reservation.game.equals("basket") -> MainApplication.getApplication().getString(R.string.basket)
            reservation.game.equals("tennis") -> MainApplication.getApplication().getString(R.string.tennis)
            reservation.game.equals("swimming") -> MainApplication.getApplication().getString(R.string.swimming)
            else -> ""
        }
        if (reservation.status == ReservationStatus.ACCEPTED) {
            holder.status.setImageResource(R.drawable.ic_green_circle_svgrepo_com)
        } else if (reservation.status == ReservationStatus.PENDING) {
            holder.status.setImageResource(R.drawable.ic_pending_svgrepo_com)
            holder.acceptBtn.apply{
                visibility = View.VISIBLE
                setOnClickListener {
                    Item_List.get(position).status = ReservationStatus.ACCEPTED
                    setReservationStatus(Item_List.get(position))
                    holder.acceptBtn.visibility = View.GONE
                    holder.rejectBtn.visibility = View.GONE
                }
            }
            holder.rejectBtn.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    Item_List.get(position).status = ReservationStatus.REJECTED
                    setReservationStatus(Item_List.get(position))
                    holder.acceptBtn.visibility = View.GONE
                    holder.rejectBtn.visibility = View.GONE
                }
            }
        } else if (reservation.status == ReservationStatus.REJECTED) {
            holder.status.setImageResource(R.drawable.ic_red_circle_svgrepo_com)
        }

        val cal = Calendar.getInstance()

        var timeStr1 = ""
        cal.timeInMillis = reservation.start_time.seconds * 1000
        holder.date.text = "${cal.get(Calendar.DAY_OF_MONTH)}-${cal.get(Calendar.MONTH)+1}-${cal.get(
            Calendar.YEAR)}"
        var format = if(cal.get(Calendar.HOUR_OF_DAY) < 12) " ${MainApplication.getApplication().getString(R.string.am)}" else " ${MainApplication.getApplication().getString(R.string.pm)}"
        var hour = if(cal.get(Calendar.HOUR_OF_DAY) < 12) cal.get(Calendar.HOUR_OF_DAY).toString() else (cal.get(
            Calendar.HOUR_OF_DAY) - 12).toString()
        timeStr1 = hour + ":" + cal.get(Calendar.MINUTE).toString()
        if(cal.get(Calendar.MINUTE).toString().length == 1)
            timeStr1 += "0"
        holder.time.text = holder.time.text.toString().replace("@", timeStr1+format)

        var timeStr2 = ""
        cal.timeInMillis = reservation.end_time.seconds * 1000
        hour = if(cal.get(Calendar.HOUR_OF_DAY) < 12) cal.get(Calendar.HOUR_OF_DAY).toString() else (cal.get(
            Calendar.HOUR_OF_DAY) - 12).toString()
        timeStr2 = hour + ":" + cal.get(Calendar.MINUTE).toString()
        if(cal.get(Calendar.MINUTE).toString().length == 1)
            timeStr2 += "0"
        format = if(cal.get(Calendar.HOUR_OF_DAY) < 12) " ${MainApplication.getApplication().getString(R.string.am)}" else " ${MainApplication.getApplication().getString(R.string.pm)}"
        holder.time.text = holder.time.text.toString().replace("#", timeStr2+format)

        holder.reservationNav.visibility = View.GONE
        holder.reservationNav.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return Item_List.size
        notifyDataSetChanged()
    }

    fun setItem_List(list: List<ReservationModel>) {
        Item_List = list as ArrayList<ReservationModel>
    }

    class ReservationAdapterOwnerViewHolder : RecyclerView.ViewHolder {
        val time: TextView
        val location: TextView
        val game: TextView
        val price: TextView
        val status: ImageView
        val date: TextView
        val removeButton: ImageButton
        val reservationNav: ImageView
        val acceptBtn: ImageButton
        val rejectBtn: ImageButton
        constructor(itemView: View) : super(itemView) {
            time = itemView.findViewById(R.id.res_time)
            location = itemView.findViewById(R.id.res_stadium_loc)
            game = itemView.findViewById(R.id.res_stadium_game)
            price = itemView.findViewById(R.id.res_price)
            status = itemView.findViewById(R.id.res_status)
            date = itemView.findViewById(R.id.res_date)
            removeButton = itemView.findViewById(R.id.res_removeBtn)
            reservationNav = itemView.findViewById(R.id.res_imgNav)
            acceptBtn = itemView.findViewById(R.id.btn_accept)
            rejectBtn = itemView.findViewById(R.id.btn_reject)
        }
    }
}