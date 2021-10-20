package com.training.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.training.factory.AppExceptionFactory
import com.training.model.LoginModel
import com.training.model.StadiumModel
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
            "access_privilege" to user.access_privilege,
            "first_usage" to user.first_usage,
            "linked" to user.linked,
            "stadium_key" to null
        )

        var snapshot = database.collection("users").document(user.email).get().await()
        if (snapshot.exists()) {
            throw AppExceptionFactory().getException("EmailAlreadyExistsException")
        } else {
            database.collection("users")
                .document(user.email).set(temp_user)
        }
    }

    suspend fun addStadium(stadium: StadiumModel){

        val temp_stadium = hashMapOf(
            "id" to stadium.id,
            "name" to stadium.name,
            "owner_id" to stadium.owner_id,
            "location_str" to stadium.location_str,
            "long" to stadium.long,
            "lat" to stadium.lat,
            "fields" to stadium.fields,
            "inventory" to stadium.inventory,
            "active" to stadium.active,
            "assigned" to stadium.assigned
        )

        var snapshot = database.collection("stadiums").document(stadium.id).get().await()
        if (snapshot.exists()) {
            throw AppExceptionFactory().getException("KeyAlreadyExistsException")
        } else {
            database.collection("stadiums")
                .document(stadium.id).set(temp_stadium)
        }
    }

    suspend fun editUser(user: UserModel){

        val temp_user = hashMapOf(
            "email" to user.email,
            "password" to user.password,
            "first_name" to user.first_name,
            "last_name" to user.last_name,
            "phone" to user.phone,
            "access_privilege" to user.access_privilege,
            "first_usage" to user.first_usage,
            "linked" to user.linked,
            "stadium_key" to user.stadium_key
        )

        var snapshot = database.collection("users").document(user.email).get()
            .addOnCanceledListener {
                throw AppExceptionFactory().getException("NetworkException")
            }
            .await()

        if (snapshot.exists()) {
            database.collection("users").document(user.email).set(temp_user)
        } else {
            throw AppExceptionFactory().getException("UnknownError")
        }
    }

    suspend fun editStadium(stadium: StadiumModel){
        val temp_stadium = hashMapOf(
            "id" to stadium.id,
            "name" to stadium.name,
            "owner_id" to stadium.owner_id,
            "location_str" to stadium.location_str,
            "long" to stadium.long,
            "lat" to stadium.lat,
            "fields" to stadium.fields,
            "inventory" to stadium.inventory,
            "active" to stadium.active,
            "assigned" to stadium.assigned
        )

        var snapshot = database.collection("stadiums").document(stadium.id).get()
            .addOnCanceledListener {
                throw AppExceptionFactory().getException("NetworkException")
            }
            .await()

        if (snapshot.exists()) {
            database.collection("stadiums").document(stadium.id).set(temp_stadium)
        } else {
            throw AppExceptionFactory().getException("UnknownError")
        }
    }

    suspend fun getUsers(): List<UserModel>{
        var snapshot = database.collection("users").whereEqualTo("access_privilege", "owner").get()
            .addOnCanceledListener {
                throw AppExceptionFactory().getException("NetworkException")
            }
            .await()

        if (!snapshot.isEmpty) {
            val userList = arrayListOf<UserModel>()
            for(user in snapshot){
                userList.add(user.toObject(UserModel::class.java).getViewFormat())
            }
            return userList
        } else {
            throw AppExceptionFactory().getException("NoDataException")
        }
    }

    suspend fun getStadiumsUnlinked(): List<StadiumModel>{
        var snapshot = database.collection("stadiums").whereEqualTo("assigned", false).get()
            .addOnCanceledListener {
                throw AppExceptionFactory().getException("NetworkException")
            }
            .await()

        if (!snapshot.isEmpty) {
            val stadiumList = arrayListOf<StadiumModel>()
            for(user in snapshot){
                stadiumList.add(user.toObject(StadiumModel::class.java))
            }
            return stadiumList
        } else {
            throw AppExceptionFactory().getException("NoDataException")
        }
    }

    suspend fun getUsersUnlinked(): List<UserModel>{
        var snapshot = database.collection("users").whereEqualTo("linked", false)
            .whereEqualTo("access_privilege","owner").get()
            .addOnCanceledListener {
                throw AppExceptionFactory().getException("NetworkException")
            }
            .await()

        if (!snapshot.isEmpty) {
            val userList = arrayListOf<UserModel>()
            for(user in snapshot){
                userList.add(user.toObject(UserModel::class.java).getViewFormat())
            }
            return userList
        } else {
            throw AppExceptionFactory().getException("NoDataException")
        }
    }

    suspend fun getStadiumsLinked(): List<StadiumModel>{
        var snapshot = database.collection("stadiums").whereEqualTo("assigned", true).get()
            .addOnCanceledListener {
                throw AppExceptionFactory().getException("NetworkException")
            }
            .await()

        if (!snapshot.isEmpty) {
            val stadiumList = arrayListOf<StadiumModel>()
            for(user in snapshot){
                stadiumList.add(user.toObject(StadiumModel::class.java))
            }
            return stadiumList
        } else {
            throw AppExceptionFactory().getException("NoDataException")
        }
    }

    suspend fun getAllStadiums(): List<StadiumModel>{
        var snapshot = database.collection("stadiums").get()
            .addOnCanceledListener {
                throw AppExceptionFactory().getException("NetworkException")
            }
            .await()

        if (!snapshot.isEmpty) {
            val stadiumList = arrayListOf<StadiumModel>()
            for(user in snapshot){
                stadiumList.add(user.toObject(StadiumModel::class.java))
            }
            return stadiumList
        } else {
            throw AppExceptionFactory().getException("NoDataException")
        }
    }

    suspend fun getUserByEmail(email: String): UserModel{
        var snapshot = database.collection("users").document(email).get()
            .addOnCanceledListener {
                throw AppExceptionFactory().getException("NetworkException")
            }
            .await()

        if(snapshot.exists()){
            var user = snapshot.toObject(UserModel::class.java) as UserModel
            return user
        }else{
            throw AppExceptionFactory().getException("InvalidUserException")
        }
    }
}
