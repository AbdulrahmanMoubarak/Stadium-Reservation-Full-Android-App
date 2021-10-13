package com.training.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.training.factory.AppExceptionFactory
import com.training.model.LoginModel
import com.training.model.UserModel
import com.training.util.constants.AccessPrivilege
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseManager
@Inject
constructor(
    val database: FirebaseFirestore,
    var firebaseScriptRunner: FirebaseInitialScriptRunner
) {

    suspend fun checkAdminSeeding() {
        if (!FirebaseInitialScriptRunner.seeded) {
            firebaseScriptRunner.runInitialFirebaseSeedingScript()
        }
    }

    suspend fun validateUserLogin(user: LoginModel): UserModel? {
        checkAdminSeeding()

        var snapshot = database.collection("users").document(user.email).get()
            .addOnCanceledListener {
                throw AppExceptionFactory().getException("NetworkException")
            }
            .await()
        if (snapshot.exists()) {
            val firebase_user = snapshot.toObject(UserModel::class.java)
            if (user.password.equals(firebase_user?.password)) {
                Log.d("password", "validateUserLogin: ${firebase_user?.password}")
                Log.d("password", "validateUserLogin: ${user.password}")
                if (firebase_user?.first_usage == true && firebase_user.access_privilege == AccessPrivilege.CUSTOMER) {
                    database.collection("users").document(user.email).update("first_usage", false)
                }
                return firebase_user
            } else {
                throw AppExceptionFactory().getException("InvalidPasswordException")
            }
        } else {
            throw AppExceptionFactory().getException("InvalidUserException")
        }
    }

    suspend fun updateUserAttribute(
        email: String,
        attribute: String,
        value: Any,
        updateFirstUsage: Boolean
    ) {
        var snapshot = database.collection("users").document(email).get()
            .addOnCanceledListener {
                throw AppExceptionFactory().getException("NetworkException")
            }
            .await()

        if (snapshot.exists()) {
            database.collection("users").document(email).update(attribute, value)
            if (updateFirstUsage) {
                database.collection("users").document(email).update("first_usage", false)
            }
        } else {
            throw AppExceptionFactory().getException("UnknownError")
        }
    }

    suspend fun addUser(user: UserModel) {
        val temp_user = hashMapOf(
            "email" to user.email,
            "password" to user.password,
            "first_name" to user.first_name,
            "last_name" to user.last_name,
            "phone" to user.phone,
            "id" to user.id,
            "access_privilege" to user.access_privilege,
            "first_usage" to user.first_usage
        )

        var snapshot = database.collection("users").document(user.email).get().await()
        if (snapshot.exists()) {
            throw AppExceptionFactory().getException("EmailAlreadyExistsException")
        } else {
            database.collection("users")
                .document(user.email).set(temp_user)
        }
    }
}
