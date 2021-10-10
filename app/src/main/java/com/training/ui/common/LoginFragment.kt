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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.training.R
import com.training.factory.UserActivityFactory
import com.training.model.LoginModel
import com.training.model.UserModel
import com.training.ui.admin.AdminActivity
import com.training.ui.customer.CustomerActivity
import com.training.ui.owner.OwnerActivity
import com.training.util.validation.ErrorFinder
import com.training.util.encryption.ItemHasherSHA256
import com.training.states.SignInState
import com.training.states.SignInViewModelEventState
import com.training.viewmodels.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return  inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onStart() {
        super.onStart()

        subscribeObserver()

        loginButton.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = ItemHasherSHA256.hashItem("password")
            viewModel.scope.launch{
                viewModel.setStateEvent(
                    SignInViewModelEventState.ProceedLogin,
                    LoginModel(email, password)
                )
            }
        }

        txt_nav_log.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun subscribeObserver(){
        lifecycleScope.launchWhenStarted {
            viewModel.loginState.collect {
                when(it){
                    is SignInState.Success<UserModel> ->{
                        displayProgressbar(false)
                        Log.d("here", "success: ")
                        switchActivity(it.data)
                        //proceed to next activity depending on user type
                    }
                    is SignInState.Loading ->{
                        Log.d("here", "loading")
                        displayProgressbar(true)
                    }
                    is SignInState.Error ->{
                        displayProgressbar(false)
                        showErrorMsg(it.type)
                        Log.d("here", "Error ${it.type}")
                    }
                    is SignInState.Filling ->{
                        displayProgressbar(false)
                        Log.d("here", "filling")
                        //Nothing
                    }
                }
            }
        }
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
            UserActivityFactory().getActivityClass(user.getAccessPrivilege())
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
            putString("user type", user.getAccessPrivilege())
            putString("email", user.email)
            putString("fname", user.first_name)
            putString("lname", user.last_name)
            putString("password", user.password)
            putString("phone", user.phone)
            putInt("id", user.getId())
        }.apply()
    }
}