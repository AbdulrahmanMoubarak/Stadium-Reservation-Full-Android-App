package com.training.repository

import com.training.firebase.FirebaseManager
import com.training.model.FieldModel
import com.training.model.ReservationModel
import com.training.model.StadiumModel
import com.training.model.UserModel
import javax.inject.Inject

class EditRepository
@Inject
constructor(var firebaseManager: FirebaseManager): EditRepositoryInterface{
    override suspend fun editUser(user: UserModel){
        firebaseManager.editUser(user)
    }

    override suspend fun editStadium(stadium: StadiumModel){
        firebaseManager.editStadium(stadium)
    }

    override suspend fun removeReservation(reservationModel: ReservationModel){
        firebaseManager.removeReservation(reservationModel)
    }

    override suspend fun removeStadiumField(field: FieldModel){
        firebaseManager.removeStadiumField(field)
    }

    override suspend fun updateStadiumField(field: FieldModel) {
        firebaseManager.updateStadiumField(field)
    }

    override suspend fun updateReservation(reservation: ReservationModel) {
        firebaseManager.updateReservation(reservation)
    }
}