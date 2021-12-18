package com.training.repository

import com.training.factory.AppExceptionFactory
import com.training.model.FieldModel
import com.training.model.ReservationModel
import com.training.model.StadiumModel
import com.training.model.UserModel

class FakeEditRepository: EditRepositoryInterface{

    var throwError = false

    override suspend fun editUser(user: UserModel) {
        if(throwError){
            throw AppExceptionFactory().getException("EmailAlreadyExistsException")
        }
    }

    override suspend fun editStadium(stadium: StadiumModel) {
        if(throwError){
            throw AppExceptionFactory().getException("UnknownError")
        }
    }

    override suspend fun removeReservation(reservationModel: ReservationModel) {
        if(throwError){
            throw AppExceptionFactory().getException("UnknownError")
        }
    }

    override suspend fun removeStadiumField(field: FieldModel) {
        if(throwError){
            throw AppExceptionFactory().getException("UnknownError")
        }
    }

    override suspend fun updateStadiumField(field: FieldModel) {
        if(throwError){
            throw AppExceptionFactory().getException("UnknownError")
        }
    }

    override suspend fun updateReservation(reservation: ReservationModel) {
        if(throwError){
            throw AppExceptionFactory().getException("UnknownError")
        }
    }

}