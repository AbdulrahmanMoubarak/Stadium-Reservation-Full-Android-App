package com.training.ui.admin

import android.os.Bundle
import android.os.UserManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.training.R
import com.training.model.UserModel
import kotlinx.android.synthetic.main.fragment_user_view.*


class UserViewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var user: UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable("user") as UserModel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user_view_back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        if(user != null){
            user?.let { user_view_first_name.setText(it.first_name) }
            user?.let { user_view_last_name.setText(it.last_name) }
            user?.let { user_view_email.setText(it.email) }
            user?.let { user_view_phone.setText(it.phone) }
        }else{
            user_view_data.visibility = View.GONE
            user_view_error.visibility = View.VISIBLE
        }
    }
}