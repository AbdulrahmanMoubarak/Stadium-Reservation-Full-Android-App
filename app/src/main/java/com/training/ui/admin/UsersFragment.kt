package com.training.ui.admin

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.R
import com.training.model.UserModel
import com.training.ui.adapters.OwnersAdapter
import com.training.states.AppDataState
import com.training.util.constants.DataError.Companion.NETWORK_ERROR
import com.training.util.validation.ErrorFinder
import com.training.viewmodels.DataRetrieveViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_users.*

@AndroidEntryPoint
class UsersFragment : Fragment() {

    private val viewModel: DataRetrieveViewModel by viewModels()
    private lateinit var user :UserModel
    val adapter = OwnersAdapter()

    var isLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        arguments?.let { isLoaded = it.getBoolean("loaded") }
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = getLoginData()
        owner_recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setAdapter(adapter)
        }
        observeLiveData()
        admin_home_button.setOnClickListener {
            var bundle = Bundle().apply {
                putBoolean("loaded", isLoaded)
            }
            findNavController().navigate(R.id.action_usersFragment_to_ownerRegisterationFragment, bundle)
        }

        admin_refresh_user.setOnClickListener {
            viewModel.getAllUsers()
        }
    }

    override fun onStart() {
        super.onStart()

        if(isConnected()){
            if(!isLoaded)
                viewModel.getAllUsers()
        }else{
            showErrorMsg(NETWORK_ERROR)
        }
    }

    private fun observeLiveData(){
        viewModel.retrieveState.observe(this, {data ->
            when(data::class){
                AppDataState.Loading::class ->{
                    displayProgressbar(true)
                }

                AppDataState.Success::class ->{
                    displayProgressbar(false)
                    val state  = data as AppDataState.Success
                    adapter.setItem_List(state.data)
                    owner_recyclerView.adapter = adapter
                    isLoaded = true
                    txt_err_userList.visibility = View.GONE
                }

                AppDataState.Error::class ->{
                    adapter.Item_List.clear()
                    owner_recyclerView.adapter = adapter
                    val state = data as AppDataState.Error
                    displayProgressbar(false)
                    showErrorMsg(state.type)
                }
            }
        })
    }

    private fun getLoginData(): UserModel{
        var sp = requireActivity().getSharedPreferences("onLogged", Context.MODE_PRIVATE)
        val user = UserModel(
            sp.getString("email", "email").toString(),
            sp.getString("password", "pass").toString(),
            sp.getString("fname", "").toString(),
            sp.getString("lname", "").toString(),
            sp.getString("phone", "").toString(),
            sp.getString("user type", "customer").toString(),
            sp.getBoolean("first usage", false)
        )
        return user
    }

    private fun showErrorMsg(error: Int){
        val error_msg = ErrorFinder.getErrorMsg(error)
        txt_err_userList.text = error_msg
        txt_err_userList.visibility = View.VISIBLE
    }

    private fun displayProgressbar(isDisplayed:Boolean){
        progress_bar_userList.visibility = if(isDisplayed) View.VISIBLE else View.GONE
    }

    @SuppressLint("MissingPermission")
    private fun isConnected(): Boolean{
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}