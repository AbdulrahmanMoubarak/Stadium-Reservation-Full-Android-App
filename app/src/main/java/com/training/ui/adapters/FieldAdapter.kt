package com.training.ui.adapters;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.training.R
import com.training.model.FieldModel
import kotlinx.android.synthetic.main.stadium_field_item.view.*

class FieldAdapter() : RecyclerView.Adapter<FieldAdapter.FieldViewHolder>() {
    var Item_List = ArrayList<FieldModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FieldViewHolder {
        return FieldViewHolder(
            LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stadium_field_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FieldViewHolder, position: Int) {

        val current_field = Item_List.get(position)

        if(current_field.game.equals("football")){
            holder.img.setImageResource(R.drawable.ic_football_svgrepo_com)
        } else if(current_field.game.equals("basket")){
            holder.img.setImageResource(R.drawable.ic_basketball_svgrepo_com)
        } else if(current_field.game.equals("tennis")){
            holder.img.setImageResource(R.drawable.ic_tennis_svgrepo_com)
        } else if(current_field.game.equals("swimming")){
            holder.img.setImageResource(R.drawable.ic_swimming_svgrepo_com)
        }

        if(current_field.available == false){
            holder.imgAvail.setImageResource(R.drawable.ic_red_circle_svgrepo_com)
        } else{
            holder.imgAvail.setImageResource(R.drawable.ic_green_circle_svgrepo_com)
        }

        holder.name.setText(current_field.game)
        holder.capacity.setText(holder.capacity.text.toString() + ": " + current_field.capacity.toString())
    }

    override fun getItemCount(): Int {
        return Item_List.size
    }

    fun addItem(field: FieldModel){
        Item_List.add(field)
        notifyItemInserted(Item_List.size-1)
    }

    fun setItem_List(list: List<FieldModel>) {
        Item_List = list as ArrayList<FieldModel>
        notifyDataSetChanged()
    }

    class FieldViewHolder : RecyclerView.ViewHolder {
        var img: ImageView
        var imgAvail: ImageView
        var name: TextView
        var capacity: TextView
        constructor(itemView: View) : super(itemView) {
            img = itemView.imageViewField
            imgAvail = itemView.imageViewAvailable
            name = itemView.textViewFieldName
            capacity = itemView.textViewCapacity
        }
    }
}