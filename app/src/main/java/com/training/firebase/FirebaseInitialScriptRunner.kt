package com.training.firebase

import android.app.Application
import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import com.training.application.MainApplication
import com.training.util.constants.AppAdmin
import com.training.util.encryption.ItemEncryptorASE
import com.training.util.encryption.ItemHasherSHA256
import javax.inject.Inject


class FirebaseInitialScriptRunner
@Inject
constructor(var database: FirebaseDatabase) {

    fun runInitialFirebaseSeedingScript() {
        lateinit var phone: String
        MainApplication.getAppContext()?.let { c ->
            phone = String(ItemEncryptorASE().encrypt(c, AppAdmin.PHONE))
        }
        val password = ItemHasherSHA256.hashItem(AppAdmin.PASSWORD)
        val email = AppAdmin.EMAIL.replace('.', ',')
        database.getReference("Users/${email}").child("id").setValue(AppAdmin.ID)
        database.getReference("Users/${email}").child("access_privilege")
            .setValue(AppAdmin.ACCESS_PRIVILEGE)
        database.getReference("Users/${email}").child("first_name").setValue(AppAdmin.FNAME)
        database.getReference("Users/${email}").child("last_name").setValue(AppAdmin.LNAME)
        database.getReference("Users/${email}").child("phone").setValue(phone)
        database.getReference("Users/${email}").child("password").setValue(password)
    }

    private suspend fun seedFirstUser() {
        lateinit var phone2: String
        MainApplication.getAppContext()?.let { c ->
            phone2 = String(ItemEncryptorASE().encrypt(c, "01159763214"))
        }
        val password2 = ItemHasherSHA256.hashItem("password")
        val email2 = "oddaled@gmail.com".replace('.', ',')
        database.getReference("Users/${email2}").child("id").setValue(1)
        database.getReference("Users/${email2}").child("access_privilege").setValue("customer")
        database.getReference("Users/${email2}").child("first_name").setValue("Abdo")
        database.getReference("Users/${email2}").child("last_name").setValue("Moubarak")
        database.getReference("Users/${email2}").child("phone").setValue(phone2)
        database.getReference("Users/${email2}").child("password").setValue(password2)
    }
}