package com.training.firebase

import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.training.application.MainApplication
import com.training.model.LoginModel
import com.training.model.UserModel
import com.training.states.SignInState
import com.training.util.constants.AppAdmin
import com.training.util.constants.SignInDataError
import com.training.util.encryption.ItemEncryptorASE
import com.training.util.encryption.ItemHasherSHA256
import java.lang.Integer.parseInt
import javax.inject.Inject

class FirebaseManager
@Inject
constructor(val database: FirebaseDatabase, var firebaseScriptRunner: FirebaseInitialScriptRunner) {

    private var state: SignInState<UserModel> = SignInState.Loading

    private fun setState(state : SignInState<UserModel>){
        this.state = state
    }

     fun checkAdminSeeding() {
        var seeded = false
        val users_table = database.getReference("Users")
        val checkUserExists = users_table.orderByKey().equalTo(AppAdmin.EMAIL.replace('.', ','))
        checkUserExists.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    seeded = true
                    Log.d("hereAdmin", "onDataChange: AdminSeededInternal")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("hereAdmin", "onDataChange: ${error.message}")
            }
        })
        if (!seeded) {
            firebaseScriptRunner.runInitialFirebaseSeedingScript()
        }
    }

    fun validateUser(user: LoginModel): SignInState<UserModel> {
        val users_table = database.getReference("Users")
        var state: SignInState<UserModel> = SignInState.Loading
        val mail = user.email.replace('.',',')
        users_table.child(mail).get().addOnSuccessListener {
           users_table.child(mail).child("password").get().addOnSuccessListener {
               if(user.password.equals(it.child("Users").child("password").getValue(String::class.java))){
                   state = SignInState.Success(parseData(user.email, user.password, it))
               }
               else
                   state = SignInState.Error(SignInDataError.ERROR_PASSWORD_WRONG)
           }.addOnFailureListener {
               state = SignInState.Error(SignInDataError.UNEXPECTED_ERROR)
           }.addOnCanceledListener {
               state = SignInState.Error(SignInDataError.UNEXPECTED_ERROR)
           }

        }.addOnFailureListener{
            state = SignInState.Error(SignInDataError.ERROR_EMAIL_NOT_FOUND)
        }.addOnCanceledListener {
            state = SignInState.Error(SignInDataError.UNEXPECTED_ERROR)
        }
        return state
    }

    private fun parseData(
        email: String,
        password: String,
        snapshot: DataSnapshot
    ): UserModel {
        val fname = snapshot.child("${email.replace('.', ',')}/first_name").value.toString()
        val lname = snapshot.child("${email.replace('.', ',')}/last_name").value.toString()
        val id = snapshot.child("${email.replace('.', ',')}/id").value.toString()
        val access = snapshot.child("${email.replace('.', ',')}/access_privilege").value.toString()
        var phone: String = "01022556987"
        /*
        MainApplication.getAppContext()?.let { con ->
            phone = String(
                ItemEncryptorASE().decrypt(
                    con,
                    snapshot.child("phone").value.toString()
                        .toByteArray()
                )
            )
        }

         */
        return UserModel(
            email,
            ItemHasherSHA256.hashItem(password),
            fname,
            lname,
            phone
        ).apply {
            setAccessPrivilege(access)
            setId(parseInt(id))
        }
    }

}