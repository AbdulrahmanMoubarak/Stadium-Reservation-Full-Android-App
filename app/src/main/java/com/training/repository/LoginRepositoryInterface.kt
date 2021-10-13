package com.training.repository

import com.training.model.LoginModel
import com.training.model.UserModel

interface LoginRepositoryInterface {

    suspend fun getUserData(user: LoginModel): UserModel?
    suspend fun changePassword(email: String, new_pass:String, updateFirstUsage: Boolean)
}