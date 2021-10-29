package com.training.ui.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.training.R
import com.training.factory.UserActivityFactory
import com.training.model.UserModel
import com.training.states.AppDataState
import com.training.util.constants.AccessPrivilege
import com.training.util.constants.DataError
import com.training.util.validation.ErrorFinder
import com.training.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var myUser: UserModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeLiveData()
    }

    override fun onStart() {
        super.onStart()
        txt_nav_reg.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        registerButton.setOnClickListener {
            if(isConnected()) {
                val email = reg_email.text.toString()
                val password = reg_pass.text.toString()
                val phone = reg_phone.text.toString()
                val fname = reg_fname.text.toString()
                val lname = reg_lname.text.toString()
                val user = UserModel(
                    email,
                    password,
                    fname,
                    lname,
                    phone,
                    AccessPrivilege.CUSTOMER,
                    true
                )
                myUser = user
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
                    saveUserData(myUser)
                    switchActivity(myUser)
                    requireActivity().finish()
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


    private fun switchActivity(user: UserModel){
        Log.d("here", "switchActivity: ")
        val intent = Intent(
            requireActivity(),
            UserActivityFactory().getActivityClass(user.access_privilege)
        ).apply {
            putExtra("user", user)
        }
        startActivity(intent)
    }

    private fun showErrorMsg(error: Int){
        val error_msg = ErrorFinder.getErrorMsg(error)
        register_txt_error_msg.text = error_msg
        register_txt_error_msg.visibility = View.VISIBLE
    }

    private fun displayProgressbar(isDisplayed:Boolean){
        progress_bar_register.visibility = if(isDisplayed) View.VISIBLE else View.GONE
    }

    @SuppressLint("MissingPermission")
    private fun isConnected(): Boolean{
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    private fun saveUserData(user: UserModel){
        val sp = requireActivity().getSharedPreferences("onLogged", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.apply {
            putBoolean("logged", true)
            putString("user type", user.access_privilege)
            putString("email", user.email)
            putString("fname", user.first_name)
            putString("lname", user.last_name)
            putString("password", user.password)
            putString("phone", user.phone)
            putString("stadium_key", user.stadium_key)
            putBoolean("linked", user.linked)
            putBoolean("first usage", user.first_usage)
        }.apply()
    }
}