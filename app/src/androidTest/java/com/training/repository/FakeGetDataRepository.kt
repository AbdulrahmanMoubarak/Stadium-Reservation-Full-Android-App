package com.training.repository

import com.training.exceptions.InvalidUserException
import com.training.exceptions.NoDataException
import com.training.model.StadiumModel
import com.training.model.UserModel
import org.junit.Assert.*

class FakeGetDataRepository(): GetDataRepositoryInterface{

    var isListEmpty = false
    var isUserInvalid = false

    override suspend fun getOwners(): List<UserModel> {
        if(isListEmpty)
            throw NoDataException()
        return ArrayList<UserModel>()
    }

    override suspend fun getStadiumsUnlinked(): List<StadiumModel> {
        if(isListEmpty)
            throw NoDataException()
        return ArrayList<StadiumModel>()
    }

    override suspend fun getStadiumsLinked(): List<StadiumModel> {
        if(isListEmpty)
            throw NoDataException()
        return ArrayList<StadiumModel>()
    }

    override suspend fun getAllStadiums(): List<StadiumModel> {
        if(isListEmpty)
            throw NoDataException()
        return ArrayList<StadiumModel>()
    }

    override suspend fun getUserByEmail(email: String): UserModel {
        if(isUserInvalid)
            throw InvalidUserException()
        return UserModel()
    }

    override suspend fun getOwnersUnlinked(): List<UserModel> {
        if(isListEmpty)
            throw NoDataException()
        return ArrayList<UserModel>()
    }

}