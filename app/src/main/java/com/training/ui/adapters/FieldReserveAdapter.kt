package com.training.ui.adapters;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.training.R
import com.training.application.MainApplication
import com.training.model.FieldModel
import kotlinx.android.synthetic.main.field_reservation_item.view.*

class FieldReserveAdapter(
    val openDialog: (field: FieldModel) -> Unit,
) : RecyclerView.Adapter<FieldReserveAdapter.FieldReserveViewHolder>() {

    private var Item_List = ArrayList<FieldModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FieldReserveViewHolder {
        return FieldReserveViewHolder(
            LayoutInflater.from(parent.getContext())
                .inflate(R.layout.field_reservation_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FieldReserveViewHolder, position: Int) {
        val current_field = Item_List.get(position)
        var gameName = ""

        if(current_field.game.equals("football")){
            holder.img.setImageResource(R.drawable.ic_football_svgrepo_com)
            gameName = MainApplication.getApplication().getString(R.string.football)
        } else if(current_field.game.equals("basket")){
            holder.img.setImageResource(R.drawable.ic_basketball_svgrepo_com)
            gameName = MainApplication.getApplication().getString(R.string.basket)
        } else if(current_field.game.equals("tennis")){
            holder.img.setImageResource(R.drawable.ic_tennis_svgrepo_com)
            gameName = MainApplication.getApplication().getString(R.string.tennis)
        } else if(current_field.game.equals("swimming")){
            holder.img.setImageResource(R.drawable.ic_swimming_svgrepo_com)
            gameName = MainApplication.getApplication().getString(R.string.swimming)
        }

        if(current_field.available == false){
            holder.imgAvail.setImageResource(R.drawable.ic_red_circle_svgrepo_com)
        } else{
            holder.imgAvail.setImageResource(R.drawable.ic_green_circle_svgrepo_com)
        }

        holder.name.setText(gameName)
        holder.capacity.setText(holder.capacity.text.toString() + ": " + current_field.capacity.toString())
        holder.price.setText("${holder.price.text}: ${current_field.price} ${MainApplication.getApplication().getString(R.string.egp)}")

        holder.itemView.setOnClickListener {
            if(current_field.available) {
                openDialog(current_field)
            }else{
                Toast.makeText(MainApplication.getAppContext(), MainApplication.getApplication().getString(R.string.fieldNotAvailable), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return Item_List.size
    }

    fun setItem_List(list: List<FieldModel>) {
        Item_List = list as ArrayList<FieldModel>
    }

    class FieldReserveViewHolder : RecyclerView.ViewHolder {
        var img: ImageView
        var imgAvail: ImageView
        var name: TextView
        var capacity: TextView
        var price: TextView
        constructor(itemView: View) : super(itemView) {
            img = itemView.reserve_imageViewField
            imgAvail = itemView.reserve_imageViewAvailable
            name = itemView.reserve_textViewFieldName
            capacity = itemView.reserve_textViewCapacity
            price = itemView.reserve_textViewPrice
        }
    }
}