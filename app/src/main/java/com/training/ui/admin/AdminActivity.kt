package com.training.ui.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.training.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_admin.*

@AndroidEntryPoint
class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        supportActionBar?.hide()

        var controller = findNavController(R.id.adminNavHost)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.usersFragmentMain, R.id.stadiumsFragmentMain, R.id.profileFragment2))

        setupActionBarWithNavController(controller, appBarConfiguration)

        bottomNavigationView.setupWithNavController(controller)
    }
}