package com.training.repository

import com.training.exceptions.NoDataException
import com.training.firebase.FirebaseManager
import com.training.model.StadiumModel
import com.training.model.UserModel

class GetDataRepository(var firebaseManager: FirebaseManager): GetDataRepositoryInterface {


    override suspend fun getOwners(): List<UserModel> {

        return firebaseManager.getUsers()
    }

    override suspend fun getOwnersUnlinked(): List<UserModel>{
        return  firebaseManager.getUsersUnlinked()
    }

    override suspend fun getStadiumsUnlinked(): List<StadiumModel> {
        return firebaseManager.getStadiumsUnlinked()
    }

    override suspend fun getStadiumsLinked(): List<StadiumModel> {
        return firebaseManager.getStadiumsLinked()
    }

    override suspend fun getAllStadiums(): List<StadiumModel> {
        return firebaseManager.getAllStadiums()
    }

    override suspend fun getUserByEmail(email: String): UserModel {
        return firebaseManager.getUserByEmail(email)
    }
}