package com.training.repository

import com.training.factory.AppExceptionFactory
import com.training.model.LoginModel
import com.training.model.UserModel
import com.training.repository.LoginRepositoryInterface
import com.training.util.constants.Encryption
import com.training.util.encryption.ItemEncryptorASE
import com.training.util.encryption.ItemHasherSHA256

class FakeLoginRepository: LoginRepositoryInterface {

    var networkErrorFlag = false
    var invalidUserFlag = false
    var wrongPasswordFlag = false
    var unknowErrorFlag = false

    fun checkNetworkError(){
        if(networkErrorFlag)
            throw AppExceptionFactory().getException("NetworkException")
    }

    fun checkinvalidUserkError(){
        if(invalidUserFlag)
            throw AppExceptionFactory().getException("InvalidUserException")
    }

    fun checkPasswordError(){
        if(wrongPasswordFlag)
            throw AppExceptionFactory().getException("InvalidPasswordException")
    }

    fun checkUnknownError(){
        if(unknowErrorFlag)
            throw AppExceptionFactory().getException("UnknownError")
    }

    override suspend fun getUserData(user: LoginModel): UserModel? {
        checkNetworkError()
        checkPasswordError()
        checkinvalidUserkError()
        checkUnknownError()

        return UserModel(
            "email@gmail.com",
            ItemHasherSHA256.hashItem("pass"),
            "Ahmad",
            "Salah",
            ItemEncryptorASE().encrypt("01012151369", Encryption.KEY),
            "customer",
            false
        )
    }

    override suspend fun changePassword(
        email: String,
        new_pass: String,
        updateFirstUsage: Boolean
    ) {

    }

}