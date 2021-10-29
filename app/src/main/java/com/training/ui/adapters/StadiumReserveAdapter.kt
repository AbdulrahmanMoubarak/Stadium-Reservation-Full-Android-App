package com.training.ui.adapters;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.training.R
import com.training.application.MainApplication
import com.training.model.StadiumModel
import kotlinx.android.synthetic.main.stadium_reservation_item.view.*

class StadiumReserveAdapter(var getUser: () -> Bundle, val locationListener: (String) -> Unit =  {}) :
    RecyclerView.Adapter<StadiumReserveAdapter.StadiumReserveViewHolder>() {
    var Item_List = ArrayList<StadiumModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StadiumReserveViewHolder {
        return StadiumReserveViewHolder(
            LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stadium_reservation_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StadiumReserveViewHolder, position: Int) {
        holder.stadiumName.setText(Item_List.get(position).name)
        holder.stadiumLoc.setText(
            Item_List.get(position).location_str.split(',')[0]
        )

        val active = Item_List.get(position).active

        if(!active){
            holder.stadiumActive.setImageResource(R.drawable.ic_red_circle_svgrepo_com)
        } else{
            holder.stadiumActive.setImageResource(R.drawable.ic_green_circle_svgrepo_com)
        }

        holder.itemView.buttonFindLocation.setOnClickListener {
            locationListener(Item_List.get(position).id)
        }

        holder.itemView.setOnClickListener {
            if (active) {
                val bundle = getUser()
                bundle.putSerializable("stadium", Item_List.get(position))
                it.findNavController().navigate(
                    R.id.action_customerSelectStadiumFragment_to_customerSelectGameFragment,
                    bundle
                )
            }else{
                Toast.makeText(MainApplication.getAppContext(), MainApplication.getApplication().getString(R.string.stadium_notActive), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return Item_List.size
    }

    fun setItem_List(list: List<StadiumModel>) {
        Item_List = list as ArrayList<StadiumModel>
    }

    class StadiumReserveViewHolder : RecyclerView.ViewHolder {
        var stadiumName: TextView
        var stadiumLoc: TextView
        var stadiumActive: ImageView
        constructor(itemView: View) : super(itemView) {
            stadiumName = itemView.findViewById(R.id.customer_stadium_name)
            stadiumLoc = itemView.findViewById(R.id.customer_stadium_loc)
            stadiumActive = itemView.imgStadiumAvailable
        }
    }
}