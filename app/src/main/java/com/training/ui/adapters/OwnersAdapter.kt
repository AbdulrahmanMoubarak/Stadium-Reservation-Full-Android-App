package com.training.ui.adapters;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.training.R
import com.training.model.UserModel

class OwnersAdapter() : RecyclerView.Adapter<OwnersAdapter.OwnerViewHolder>() {
     var Item_List = ArrayList<UserModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OwnerViewHolder {
        return OwnerViewHolder(
            LayoutInflater.from(parent.getContext())
                .inflate(R.layout.owner_recycler_view, parent, false)
        )
    }



    override fun onBindViewHolder(holder: OwnerViewHolder, position: Int) {
        holder.userName.setText(Item_List.get(position).first_name + " " + Item_List.get(position).last_name)
        holder.phone.setText(Item_List.get(position).phone)
        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("user",Item_List.get(position))
            }
            it.findNavController().navigate(R.id.action_usersFragment_to_userViewFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return Item_List.size
    }

    fun setItem_List(list: List<UserModel>) {
        Item_List = list as ArrayList<UserModel>
    }

    class OwnerViewHolder : RecyclerView.ViewHolder {

        var userName: TextView
        var phone: TextView

        constructor(itemView: View) : super(itemView) {
            userName = itemView.findViewById(R.id.owner_name)
            phone = itemView.findViewById(R.id.owner_phone)
        }
    }
}