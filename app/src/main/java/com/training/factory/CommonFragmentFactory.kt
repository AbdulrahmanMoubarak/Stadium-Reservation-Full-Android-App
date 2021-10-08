package com.training.factory

import androidx.fragment.app.FragmentFactory
import com.training.ui.common.LoginFragment
import com.training.ui.common.RegisterFragment

class CommonFragmentFactory(): FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when(className){

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