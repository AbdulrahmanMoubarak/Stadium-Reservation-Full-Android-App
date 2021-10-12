package com.training.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.training.R
import com.training.util.encryption.ItemHasherSHA256
import com.training.util.validation.ErrorFinder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onStart() {
        subscribeObserver()

        super.onStart()
        txt_nav_reg.setOnClickListener {
            requireActivity().onBackPressed()
        }

        registerButton.setOnClickListener {
            val email = reg_email.text.toString()
            val password = ItemHasherSHA256.hashItem(reg_pass.text.toString())
           // val phone = String(ItemEncryptorASE().encrypt(requireContext() ,reg_phone.text.toString()))
            val fname = reg_fname.text.toString()
            val lname = reg_lname.text.toString()
            /*
            viewModel.scope.launch{
                viewModel.setStateEvent(
                    SignInViewModelEventState.ProceedRegister,
                    UserModel(email, password,fname,lname, phone)
                )
            }
             */
        }
    }

    private fun subscribeObserver(){
        /*
        lifecycleScope.launchWhenStarted {
            viewModel.loginState.collect {
                when(it){
                    is SignInState.Success<UserModel> ->{
                        displayProgressbar(false)
                        notifySuccess()
                        txt_nav_reg.callOnClick()
                    }
                    is SignInState.Loading ->{
                        displayProgressbar(true)
                    }
                    is SignInState.Error ->{
                        displayProgressbar(false)
                        showErrorMsg(it.type)
                    }
                    is SignInState.Filling ->{
                        displayProgressbar(false)
                        //Nothing
                    }
                }
            }
        }

         */
    }

    private fun showErrorMsg(error: Int){
        val error_msg = ErrorFinder.getErrorMsg(error)
        register_txt_error_msg.text = error_msg
        register_txt_error_msg.visibility = View.VISIBLE
    }

    private fun displayProgressbar(isDisplayed:Boolean){
        progress_bar_register.visibility = if(isDisplayed) View.VISIBLE else View.GONE
    }

    private fun notifySuccess(){
        Snackbar.make(requireView(), "Signed up successfully, now log in", Snackbar.LENGTH_SHORT)
            .show()
    }
}