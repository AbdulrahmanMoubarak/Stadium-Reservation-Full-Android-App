package com.training.ui.owner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.training.R
import com.training.model.UserModel
import kotlinx.android.synthetic.main.activity_owner.*

class OwnerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner)

        val user: UserModel = intent.getSerializableExtra("user") as UserModel
        textViewOwner.text = textViewOwner.text.toString() + user.first_name+ ", ${user.access_privilege}"
    }
    override fun onBackPressed() {
        finish()
    }
}