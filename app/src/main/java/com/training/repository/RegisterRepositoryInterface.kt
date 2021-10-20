package com.training.repository

import com.training.model.StadiumModel
import com.training.model.UserModel

interface RegisterRepositoryInterface {
    suspend fun addUser(user: UserModel)
    suspend fun RegisterStadium(stadiumModel: StadiumModel)
}