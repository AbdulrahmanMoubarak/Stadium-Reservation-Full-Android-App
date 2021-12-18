package com.training.ui.adapters;

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.training.R
import com.training.application.MainApplication
import com.training.model.FieldModel
import kotlinx.android.synthetic.main.stadium_field_item.view.*
import android.content.DialogInterface

class FieldAdapter(
    var onRemove: (FieldModel) -> Unit = {},
    var onpressActive: (FieldModel) -> Unit = {},
    var openDialog: () -> AlertDialog.Builder
) : RecyclerView.Adapter<FieldAdapter.FieldViewHolder>() {

    var Item_List = ArrayList<FieldModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FieldViewHolder {
        return FieldViewHolder(
            LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stadium_field_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FieldViewHolder, position: Int) {

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

        holder.remove.setOnClickListener {
            openDialog().apply {
                setPositiveButton(MainApplication.getApplication().getString(R.string.confirm)) { dialog, id ->
                    onRemove(current_field)

                }
                setNegativeButton(MainApplication.getApplication().getString(R.string.cancel)) { dialog, id ->

                }
            }.create().show()
        }

        holder.imgAvail.setOnClickListener {
            onpressActive(current_field)
            Item_List.get(position).available = !Item_List.get(position).available
        }
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
    }

    class FieldViewHolder : RecyclerView.ViewHolder {
        var img: ImageView
        var imgAvail: ImageView
        var name: TextView
        var capacity: TextView
        var remove: ImageView
        constructor(itemView: View) : super(itemView) {
            img = itemView.imageViewField
            imgAvail = itemView.imageViewAvailable
            name = itemView.textViewFieldName
            capacity = itemView.textViewCapacity
            remove = itemView.stadium_field_remove
        }
    }
}