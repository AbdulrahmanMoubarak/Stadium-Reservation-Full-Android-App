package com.training.factory

import androidx.fragment.app.FragmentFactory
import com.training.firebase.FirebaseInitialScriptRunner
import com.training.ui.common.LoginFragment
import com.training.ui.common.RegisterFragment
import com.training.ui.common.SplashFragment
import javax.inject.Inject

class CommonFragmentFactory : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when(className){
            SplashFragment::class.java.name -> {
                SplashFragment()
            }

            LoginFragment::class.java.name -> {

                LoginFragment()
            }

            RegisterFragment::class.java.name -> {
                RegisterFragment()
            }

            else -> {
                super.instantiate(classLoader, className)
            }
        }
}