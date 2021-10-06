package com.training.ui.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.training.R
import com.training.model.UserModel
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val user: UserModel = intent.getSerializableExtra("user") as UserModel
        textViewAdmin.text = textViewAdmin.text.toString() + user.first_name
    }
}