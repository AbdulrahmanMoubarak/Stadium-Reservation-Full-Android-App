package com.training.repository

import com.training.model.FieldModel
import com.training.model.ReservationModel
import com.training.model.StadiumModel
import com.training.model.UserModel

interface RegisterRepositoryInterface {
    suspend fun addUser(user: UserModel)
    suspend fun RegisterStadium(stadiumModel: StadiumModel)
    suspend fun AddFieldToStadium(key: String, field: FieldModel)
    suspend fun addReservation(reservation: ReservationModel)
}