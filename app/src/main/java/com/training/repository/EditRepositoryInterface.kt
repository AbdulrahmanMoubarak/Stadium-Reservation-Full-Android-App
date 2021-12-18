package com.training.repository

import com.training.model.FieldModel
import com.training.model.ReservationModel
import com.training.model.StadiumModel
import com.training.model.UserModel

interface EditRepositoryInterface {
    suspend fun editUser(user: UserModel)

    suspend fun editStadium(stadium: StadiumModel)

    suspend fun removeReservation(reservationModel: ReservationModel)

    suspend fun removeStadiumField(field: FieldModel)

    suspend fun updateStadiumField(field: FieldModel)

    suspend fun updateReservation(reservation: ReservationModel)
}