package com.training.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.training.factory.AppExceptionFactory
import com.training.model.LoginModel
import com.training.model.UserModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseManager
@Inject
constructor(val database: FirebaseFirestore, var firebaseScriptRunner: FirebaseInitialScriptRunner) {

    suspend fun checkAdminSeeding() {
        if(!FirebaseInitialScriptRunner.seeded){
            firebaseScriptRunner.runInitialFirebaseSeedingScript()
        }
    }

    suspend fun validateUserLogin(user: LoginModel):UserModel?{
        checkAdminSeeding()
        var snapshot = database.collection("users").document(user.email).get()
            .addOnCanceledListener {
                throw AppExceptionFactory().getException("NetworkException")
            }
            .await()
        if (snapshot.exists()) {
            val firebase_user =  snapshot.toObject(UserModel::class.java)
            if (user.password.equals(firebase_user?.password)){
                Log.d("password", "validateUserLogin: ${firebase_user?.password}")
                Log.d("password", "validateUserLogin: ${user.password}")
                return firebase_user
            }else{
                throw AppExceptionFactory().getException("InvalidPasswordException")
            }
        }else{
            throw AppExceptionFactory().getException("InvalidUserException")
        }
    }
}
