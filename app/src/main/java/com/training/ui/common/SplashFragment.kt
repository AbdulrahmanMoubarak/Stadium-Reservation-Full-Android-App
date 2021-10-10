package com.training.ui.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.training.R
import com.training.factory.UserActivityFactory
import com.training.firebase.FirebaseInitialScriptRunner
import com.training.model.UserModel
import com.training.ui.admin.AdminActivity
import com.training.ui.customer.CustomerActivity
import com.training.ui.owner.OwnerActivity
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_splash, container, false)
        Handler(Looper.getMainLooper())
            .postDelayed({
                if(!isLogged()) {
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }
                else{
                    val user = getLoginData()
                    navigateToUserActivity(user)
                }
            }, 2000)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun isLogged(): Boolean{
        var sp = requireActivity().getSharedPreferences("onLogged", Context.MODE_PRIVATE)
        return  sp.getBoolean("logged", false)
    }

    private fun getLoginData(): UserModel{
        var sp = requireActivity().getSharedPreferences("onLogged", Context.MODE_PRIVATE)
        val user = UserModel(
            sp.getString("email", "email").toString(),
            sp.getString("password", "pass").toString(),
            sp.getString("fname", "").toString(),
            sp.getString("lname", "").toString(),
            sp.getString("phone", "").toString(),
        ).apply {
            setId(sp.getInt("id", -1))
            setAccessPrivilege(sp.getString("user type", "customer").toString())
        }
        return user
    }

    private fun navigateToUserActivity(user: UserModel){
        val intent = Intent(
            requireActivity(),
            UserActivityFactory().getActivityClass(user.getAccessPrivilege())
        ).apply {
            putExtra("user", user)
        }
        startActivity(intent)
    }
}