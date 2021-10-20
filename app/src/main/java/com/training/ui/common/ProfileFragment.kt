package com.training.ui.common

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.training.R
import com.training.model.UserModel
import com.training.states.SignInState
import com.training.util.constants.DataError
import com.training.util.validation.ErrorFinder
import com.training.viewmodels.EditViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var user: UserModel
    private val viewmodel: EditViewModel by viewModels()
    private var passEdited = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscribeLiveData()
        super.onViewCreated(view, savedInstanceState)
        user = getLoginData()
        setupView()
        profile_edit.setOnClickListener {
            profile_viewmode.visibility = View.GONE
            profile_editmode.visibility = View.VISIBLE
        }

        profile_cancel_edit.setOnClickListener {
            profile_editmode.visibility = View.GONE
            profile_viewmode.visibility = View.VISIBLE
        }

        profile_confirm_edit.setOnClickListener {
            if (isConnected()) {
                val edit_user = getEditData()
                if (edit_user.password.equals(user.password)) {
                    passEdited = false
                    viewmodel.updateUserData(edit_user, false)
                } else {
                    passEdited = true
                    viewmodel.updateUserData(edit_user, true)
                }
            }
            else
                showErrorMsg(DataError.NETWORK_ERROR)
        }
    }

    private fun setupView(){
        profile_fname.setText(user.first_name)
        profile_fname_edit.setText(user.first_name)
        profile_lname.setText(user.last_name)
        profile_lname_edit.setText(user.last_name)
        profile_mobile.setText(user.phone)
        profile_mobile_edit.setText(user.phone)
        profile_email.setText(user.email)
        profile_email_edit.setText(user.email)
        profile_password_edit.setText(user.password)
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

    private fun getEditData():UserModel{
        return UserModel(
            profile_email_edit.text.toString(),
            profile_password_edit.text.toString(),
            profile_fname_edit.text.toString(),
            profile_lname_edit.text.toString(),
            profile_mobile_edit.text.toString(),
            user.access_privilege,
            user.first_usage
        )
    }

    private fun subscribeLiveData(){
        viewmodel.updateState.observe(this, {data ->
            when(data::class){
                SignInState.Loading::class ->{
                    displayProgressbar(true)
                    Log.d("Here", "subscribeLiveData: loading")
                }

                SignInState.Success::class -> {
                    val tempUser = (data as SignInState.Success).data
                    Log.d("Here", "subscribeLiveData: Success")
                    displayProgressbar(false)
                    user = tempUser
                    updateSharedPreference(tempUser)
                    setupView()
                    profile_editmode.visibility = View.GONE
                    profile_viewmode.visibility = View.VISIBLE
                    Snackbar.make(
                        view!!,
                        "Successfully updated data",
                        Snackbar.LENGTH_SHORT
                    ).show()
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

    private fun displayProgressbar(isDisplayed:Boolean){
        progress_bar_edit.visibility = if(isDisplayed) View.VISIBLE else View.GONE
    }

    private fun showErrorMsg(error: Int){
        val error_msg = ErrorFinder.getErrorMsg(error)
        edit_txt_error_msg.text = error_msg
        edit_txt_error_msg.visibility = View.VISIBLE
    }

    private fun updateSharedPreference(user: UserModel){
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

    @SuppressLint("MissingPermission")
    private fun isConnected(): Boolean{
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}