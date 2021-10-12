package com.training.repository

import com.training.firebase.FirebaseManager
import com.training.model.LoginModel
import com.training.model.UserModel
import javax.inject.Inject

class LoginRepository
@Inject
constructor(
    var firebaseManager: FirebaseManager,
    ) {
    lateinit var userModel: UserModel
    suspend fun getUserData(user:LoginModel): UserModel?{
        val fire_user = firebaseManager.validateUserLogin(user)
        return fire_user
    }

}