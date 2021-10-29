package com.training.repository

import com.training.firebase.FirebaseManager
import com.training.model.ReservationModel
import com.training.model.StadiumModel
import com.training.model.UserModel
import javax.inject.Inject

class EditRepository
@Inject
constructor(var firebaseManager: FirebaseManager){
    suspend fun editUser(user: UserModel){
        firebaseManager.editUser(user)
    }

    suspend fun editStadium(stadium: StadiumModel){
        firebaseManager.editStadium(stadium)
    }

    suspend fun removeReservation(reservationModel: ReservationModel){
        firebaseManager.removeReservation(reservationModel)
    }
}