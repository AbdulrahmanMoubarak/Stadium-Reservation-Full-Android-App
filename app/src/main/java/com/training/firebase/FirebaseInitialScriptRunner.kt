package com.training.firebase

import android.app.Application
import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.training.application.MainApplication
import com.training.util.constants.AccessPrivilege
import com.training.util.constants.AppAdmin
import com.training.util.constants.Encryption
import com.training.util.encryption.ItemEncryptorASE
import com.training.util.encryption.ItemHasherSHA256
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FirebaseInitialScriptRunner
@Inject
constructor(var database: FirebaseFirestore) {
    companion object {
        var seeded = false
    }
    suspend fun runInitialFirebaseSeedingScript() {
        if(!seeded) {
            var phone: String = ItemEncryptorASE().encrypt(AppAdmin.PHONE, Encryption.KEY)
            val password = ItemHasherSHA256.hashItem(AppAdmin.PASSWORD)
            val email = AppAdmin.EMAIL.replace('.', ',')

            val user = hashMapOf(
                "email" to email,
                "password" to password,
                "first_name" to AppAdmin.FNAME,
                "last_name" to AppAdmin.LNAME,
                "phone" to phone,
                "access_privilege" to AppAdmin.ACCESS_PRIVILEGE,
                "first_usage" to true
            )

            var snapshot = database.collection("users").document(email).get().await()
            if (!snapshot.exists()) {
                database.collection("users")
                    .document(email).set(user)
                seeded = true
            }else{
                seeded = true
            }
        }
    }

    private suspend fun seedFirstUser() {
        var phone2: String = ItemEncryptorASE().encrypt("01159909754", Encryption.KEY)
        val password2 = ItemHasherSHA256.hashItem("password")
        val email2 = "oddaled@gmail.com".replace('.', ',')
        val user = hashMapOf(
            "email" to email2,
            "password" to password2,
            "first_name" to "AbdulRahman",
            "last_name" to "Moubarak",
            "phone" to phone2,
            "access_privilege" to AccessPrivilege.CUSTOMER,
            "first_usage" to true
        )

        database.collection("users")
            .document(email2).set(user)
    }
}