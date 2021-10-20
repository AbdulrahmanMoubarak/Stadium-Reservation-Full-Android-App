package com.training.repository

import com.training.factory.AppExceptionFactory
import com.training.model.StadiumModel
import com.training.model.UserModel
import org.junit.Assert.*

class FakeRegisterRepository: RegisterRepositoryInterface{
    var userExists = false
    var stadiumKeyExists = false
    override suspend fun addUser(user: UserModel) {
        if(userExists){
            throw AppExceptionFactory().getException("EmailAlreadyExistsException")
        }

    }

    override suspend fun RegisterStadium(stadiumModel: StadiumModel) {
        if(stadiumKeyExists){
            throw AppExceptionFactory().getException("KeyAlreadyExistsException")
        }
    }

}