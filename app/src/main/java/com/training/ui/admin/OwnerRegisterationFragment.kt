package com.training.ui.admin

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.training.R
import com.training.factory.UserActivityFactory
import com.training.model.UserModel
import com.training.states.SignInState
import com.training.util.constants.AccessPrivilege
import com.training.util.constants.SignInDataError
import com.training.util.validation.ErrorFinder
import com.training.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_owner_registeration.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.txt_nav_reg

@AndroidEntryPoint
class OwnerRegisterationFragment : Fragment() {
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_owner_registeration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeLiveData()
    }

    override fun onStart() {
        super.onStart()
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
                    id = 0,
                    AccessPrivilege.OWNER,
                    true
                )
                viewModel.addUser(user)
            }
            else{
                showErrorMsg(SignInDataError.NETWORK_ERROR)
            }
        }
        viewModel.registerState.postValue(SignInState.Filling)
    }

    private fun subscribeLiveData(){
        viewModel.registerState.observe(this, {data ->
            when(data::class){
                SignInState.Loading::class ->{
                    displayProgressbar(true)
                    Log.d("Here", "subscribeLiveData: loading")
                }

                SignInState.OperationSuccess::class ->{
                    Log.d("Here", "subscribeLiveData: Success")
                    displayProgressbar(false)
                    val state  = data as SignInState.OperationSuccess
                    /////
                    findNavController().navigate(R.id.action_ownerRegisterationFragment_to_addOwnerFragment)
                    if (view != null) {
                        Snackbar.make(
                            view!!,
                            "Successfully added owner",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    //////
                }

                SignInState.Error::class ->{
                    Log.d("Here", "subscribeLiveData: Error")
                    val state = data as SignInState.Error
                    displayProgressbar(false)
                    showErrorMsg(state.type)
                }
            }
        })
    }


    private fun showErrorMsg(error: Int){
        val error_msg = ErrorFinder.getErrorMsg(error)
        register_txt_error_msg.text = error_msg
        register_txt_error_msg.visibility = View.VISIBLE
    }

    private fun displayProgressbar(isDisplayed:Boolean){
        Admin_add_progress_bar_register.visibility = if(isDisplayed) View.VISIBLE else View.GONE
    }

    private fun isConnected(): Boolean{
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

}