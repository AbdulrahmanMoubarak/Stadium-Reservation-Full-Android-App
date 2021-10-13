package com.training.repository

import com.training.firebase.FirebaseManager
import com.training.model.LoginModel
import com.training.model.UserModel
import javax.inject.Inject

class LoginRepository
@Inject
constructor(
    var firebaseManager: FirebaseManager,
    ): LoginRepositoryInterface {
    override suspend fun getUserData(user:LoginModel): UserModel?{
        val fire_user = firebaseManager.validateUserLogin(user)
        return fire_user
    }

    override suspend fun changePassword(email: String, new_pass:String, updateFirstUsage: Boolean){
        firebaseManager.updateUserAttribute(email, "password", new_pass, updateFirstUsage)
    }

}