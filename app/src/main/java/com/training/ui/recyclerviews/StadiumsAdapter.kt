package com.training.ui.recyclerviews;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.training.R
import com.training.model.StadiumModel

class StadiumsAdapter() : RecyclerView.Adapter<StadiumsAdapter.StadiumsViewHolder>() {
     var Item_List = ArrayList<StadiumModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StadiumsViewHolder {
        return StadiumsViewHolder(
            LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stadiums_item_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StadiumsViewHolder, position: Int) {
        holder.stadiumName.setText(Item_List.get(position).name)
        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("stadium",Item_List.get(position))
                it.findNavController().navigate(R.id.action_stadiumsFragment_to_stadiumViewFragment, this)
            }
        }
    }

    override fun getItemCount(): Int {
        return Item_List.size
    }

    fun setItem_List(list: List<StadiumModel>) {
        Item_List = list as ArrayList<StadiumModel>
    }

    class StadiumsViewHolder : RecyclerView.ViewHolder {
        var stadiumName: TextView

        constructor(itemView: View) : super(itemView) {
            stadiumName = itemView.findViewById(R.id.stadium_name)
        }
    }
}