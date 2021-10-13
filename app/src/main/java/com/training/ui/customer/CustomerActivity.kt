package com.training.ui.customer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.training.R
import com.training.model.UserModel
import kotlinx.android.synthetic.main.activity_customer.*

class CustomerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)

        val user: UserModel = intent.getSerializableExtra("user") as UserModel
        textViewCustomer.text = textViewCustomer.text.toString() + user.first_name + ", ${user.access_privilege} , ${user.phone}"
    }
    override fun onBackPressed() {
        finish()
    }
}