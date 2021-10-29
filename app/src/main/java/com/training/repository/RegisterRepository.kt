package com.training.repository

import com.training.firebase.FirebaseManager
import com.training.model.FieldModel
import com.training.model.ReservationModel
import com.training.model.StadiumModel
import com.training.model.UserModel
import javax.inject.Inject

class RegisterRepository
@Inject
constructor(
    var firebaseManager: FirebaseManager,
): RegisterRepositoryInterface{

    override suspend fun addUser(user: UserModel){
        firebaseManager.addUser(user)
    }

    override suspend fun RegisterStadium(stadium: StadiumModel){
        firebaseManager.addStadium(stadium)
    }

    override suspend fun AddFieldToStadium(key: String, field: FieldModel){
        firebaseManager.addStadiumField(key, field)
    }

    override suspend fun addReservation(reservation: ReservationModel){
        firebaseManager.addReservation(reservation)
    }
}