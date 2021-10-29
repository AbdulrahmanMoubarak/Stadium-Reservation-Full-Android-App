package com.training.ui.admin

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.training.R
import com.training.model.UserModel
import com.training.states.AppDataState
import com.training.util.constants.AccessPrivilege
import com.training.util.constants.DataError
import com.training.util.validation.ErrorFinder
import com.training.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_owner_registeration.*


@AndroidEntryPoint
class OwnerRegisterationFragment : Fragment() {
    private val viewModel: RegisterViewModel by viewModels()

    var isLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        arguments?.let { isLoaded = it.getBoolean("loaded") }
        return inflater.inflate(R.layout.fragment_owner_registeration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        subscribeLiveData()
        Admin_add_registerButton.setOnClickListener {
            if(isConnected()) {
                val email = Admin_add_reg_email.text.toString()
                val password = Admin_add_reg_pass.text.toString()
                val phone = Admin_add_reg_phone.text.toString()
                val fname = Admin_add_reg_fname.text.toString()
                val lname = Admin_add_reg_lname.text.toString()
                val user = UserModel(
                    email,
                    password,
                    fname,
                    lname,
                    phone,
                    AccessPrivilege.OWNER,
                    true
                )
                viewModel.addUser(user)
            }
            else{
                showErrorMsg(DataError.NETWORK_ERROR)
            }
        }
        viewModel.registerState.postValue(AppDataState.Filling)
    }

    private fun subscribeLiveData(){
        viewModel.registerState.observe(this, {data ->
            when(data::class){
                AppDataState.Loading::class ->{
                    displayProgressbar(true)
                    Log.d("Here", "subscribeLiveData: loading")
                }

                AppDataState.OperationSuccess::class ->{
                    Log.d("Here", "subscribeLiveData: Success")
                    displayProgressbar(false)
                    val state  = data as AppDataState.OperationSuccess
                    /////
                    if (view != null) {
                        Snackbar.make(
                            view!!,
                            "Successfully added owner",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    requireActivity().onBackPressed()
                    //////
                }

                AppDataState.Error::class ->{
                    Log.d("Here", "subscribeLiveData: Error")
                    val state = data as AppDataState.Error
                    displayProgressbar(false)
                    showErrorMsg(state.type)
                }
            }
        })
    }


    private fun showErrorMsg(error: Int){
        Admin_add_txt_error_msg.visibility = View.VISIBLE
        val error_msg = ErrorFinder.getErrorMsg(error)
        Admin_add_txt_error_msg.text = error_msg
    }

    private fun displayProgressbar(isDisplayed:Boolean){
        Admin_add_progress_bar_register.visibility = if(isDisplayed) View.VISIBLE else View.GONE
    }

    @SuppressLint("MissingPermission")
    private fun isConnected(): Boolean{
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

}