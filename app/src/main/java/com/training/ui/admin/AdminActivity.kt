package com.training.ui.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.training.R
import com.training.model.UserModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_admin.*

@AndroidEntryPoint
class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
    }

    override fun onBackPressed() {
        finish()
    }
}