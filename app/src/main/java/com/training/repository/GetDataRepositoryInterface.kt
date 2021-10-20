package com.training.repository

import com.training.model.StadiumModel
import com.training.model.UserModel

interface GetDataRepositoryInterface {
    suspend fun getOwners(): List<UserModel>
    suspend fun getStadiumsUnlinked(): List<StadiumModel>
    suspend fun getStadiumsLinked(): List<StadiumModel>
    suspend fun getAllStadiums(): List<StadiumModel>
    suspend fun getUserByEmail(email: String): UserModel
    suspend fun getOwnersUnlinked(): List<UserModel>
}