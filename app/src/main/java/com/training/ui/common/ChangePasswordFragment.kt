package com.training.ui.common

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
import com.training.R
import com.training.factory.UserActivityFactory
import com.training.model.UserModel
import com.training.states.SignInState
import com.training.util.constants.DataError
import com.training.util.validation.ErrorFinder
import com.training.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_change_password.*

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {

    private lateinit var user: UserModel
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        user = getLoginData()
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        change_pass_header_txt.text = change_pass_header_txt.text.toString() + " " + user.first_name
        subscribeLiveData()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        confirmButton.setOnClickListener {
            if(isConnected()) {
                val pass = editTextPasswordChange.text.toString()
                viewModel.updatePassword(user, pass)
            }
            else
                showErrorMsg(DataError.NETWORK_ERROR)
        }
    }

    private fun getLoginData(): UserModel {
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

    private fun subscribeLiveData(){
        viewModel.loginState.observe(this, {data ->
            when(data::class){
                SignInState.Loading::class ->{
                    displayProgressbar(true)
                    Log.d("Here", "subscribeLiveData: loading")
                }

                SignInState.OperationSuccess::class ->{
                    Log.d("Here", "subscribeLiveData: Success")
                    displayProgressbar(false)
                    user.first_usage = false
                    saveLoginData(user)
                    switchActivity(user)
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
        change_pass_txt_error_msg.text = error_msg
        change_pass_txt_error_msg.visibility = View.VISIBLE
    }

    private fun displayProgressbar(isDisplayed:Boolean){
        progress_bar_change_pass.visibility = if(isDisplayed) View.VISIBLE else View.GONE
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

    private fun saveLoginData(user: UserModel){
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
            putBoolean("first usage", user.first_usage)
        }.apply()
    }

    private fun isConnected(): Boolean{
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

}