package com.training.ui.common

import android.content.Context
import android.content.Intent
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
import com.training.model.LoginModel
import com.training.model.UserModel
import com.training.states.SignInState
import com.training.util.validation.ErrorFinder
import com.training.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return  inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeLiveData()
    }

    override fun onStart() {
        super.onStart()


        loginButton.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            viewModel.validateLogin(LoginModel(email, password))
        }

        txt_nav_log.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        viewModel.loginState.postValue(SignInState.Filling)
    }

    private fun subscribeLiveData(){
        viewModel.loginState.observe(this, {data ->
            when(data::class){
                SignInState.Loading::class ->{
                    displayProgressbar(true)
                    Log.d("Here", "subscribeLiveData: loading")
                }

                SignInState.Success::class ->{
                    Log.d("Here", "subscribeLiveData: Success")
                    displayProgressbar(false)
                    val state  = data as SignInState.Success
                    switchActivity(state.data)
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
        login_txt_error_msg.text = error_msg
        login_txt_error_msg.visibility = View.VISIBLE
    }

    private fun displayProgressbar(isDisplayed:Boolean){
        progress_bar_login.visibility = if(isDisplayed) View.VISIBLE else View.GONE
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
            putInt("id", user.id)
            putBoolean("first usage", true)
        }.apply()
    }
}