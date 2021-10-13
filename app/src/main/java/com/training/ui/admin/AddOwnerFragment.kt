package com.training.ui.admin

import android.content.Context
import android.os.Bundle
import android.os.UserManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.training.R
import com.training.model.UserModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_owner.*

@AndroidEntryPoint
class AddOwnerFragment : Fragment() {

    private lateinit var user :UserModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_owner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = getLoginData()
        textViewAdmin.text = textViewAdmin.text.toString() + " " + user.first_name

        admin_home_button.setOnClickListener {
            findNavController().navigate(R.id.action_addOwnerFragment_to_ownerRegisterationFragment)
        }
    }

    private fun getLoginData(): UserModel{
        var sp = requireActivity().getSharedPreferences("onLogged", Context.MODE_PRIVATE)
        val user = UserModel(
            sp.getString("email", "email").toString(),
            sp.getString("password", "pass").toString(),
            sp.getString("fname", "").toString(),
            sp.getString("lname", "").toString(),
            sp.getString("phone", "").toString(),
            sp.getInt("id", 0),
            sp.getString("user type", "customer").toString(),
            sp.getBoolean("first usage", false)
        )
        return user
    }
}