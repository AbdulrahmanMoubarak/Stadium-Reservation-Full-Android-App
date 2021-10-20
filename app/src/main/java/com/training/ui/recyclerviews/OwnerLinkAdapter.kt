package com.training.ui.recyclerviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.training.R
import com.training.model.UserModel

class OwnerLinkAdapter: RecyclerView.Adapter<OwnerLinkAdapter.OwnerLinkViewHolder>() {
    var Item_List = ArrayList<UserModel>()

    val selectedUser = MutableLiveData<UserModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OwnerLinkViewHolder {
        return OwnerLinkViewHolder(
            LayoutInflater.from(parent.getContext())
                .inflate(R.layout.owner_recycler_view, parent, false)
        )
    }



    override fun onBindViewHolder(holder: OwnerLinkViewHolder, position: Int) {
        holder.userName.setText(Item_List.get(position).first_name + " " + Item_List.get(position).last_name)
        holder.phone.setText(Item_List.get(position).phone)
        holder.itemView.setOnClickListener {
            selectedUser.postValue(Item_List.get(position))
        }
    }

    override fun getItemCount(): Int {
        return Item_List.size
    }

    fun setItem_List(list: List<UserModel>) {
        Item_List = list as ArrayList<UserModel>
    }

    class OwnerLinkViewHolder : RecyclerView.ViewHolder {

        var userName: TextView
        var phone: TextView

        constructor(itemView: View) : super(itemView) {
            userName = itemView.findViewById(R.id.owner_name)
            phone = itemView.findViewById(R.id.owner_phone)
        }
    }
}