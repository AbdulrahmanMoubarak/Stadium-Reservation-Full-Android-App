package com.training.repository

import com.training.exceptions.InvalidUserException
import com.training.exceptions.NoDataException
import com.training.factory.AppExceptionFactory
import com.training.model.FieldModel
import com.training.model.ReservationModel
import com.training.model.StadiumModel
import com.training.model.UserModel
import com.training.util.constants.Encryption
import com.training.util.encryption.ItemEncryptorASE

class FakeGetDataRepository(): GetDataRepositoryInterface{

    var isListEmpty = false
    var isUserInvalid = false
    var keyExists = false

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
        return UserModel(
            "email: String",
            "password: String",
            "first_name:String",
            "last_name:String",
            "phone: String",
            "01012147896",
            true,
            false,
            ""
        ).getFirebaseFormat()
    }

    override suspend fun getOwnersUnlinked(): List<UserModel> {
        if(isListEmpty)
            throw NoDataException()
        return ArrayList<UserModel>()
    }

    override suspend fun getStadiumByKey(key: String): StadiumModel {
        if(keyExists){
            throw AppExceptionFactory().getException("KeyDoesnotExistException")
        }
        return StadiumModel()
    }

    override suspend fun getStadiumFields(key: String): List<FieldModel> {
        if(isListEmpty){
            throw AppExceptionFactory().getException("NoDataException")
        }
        return emptyList()
    }

    override suspend fun getUserReservations(user: UserModel): List<ReservationModel> {
        if(isListEmpty){
            throw AppExceptionFactory().getException("NoDataException")
        }
        return emptyList()
    }

    override suspend fun getUserReservationsByStatus(
        user: UserModel,
        status: String
    ): List<ReservationModel> {
        if(isListEmpty){
            throw AppExceptionFactory().getException("NoDataException")
        }
        return emptyList()
    }

    override suspend fun getDayUserReservations(
        user: UserModel,
        date: String
    ): List<ReservationModel> {
        if(isListEmpty){
            throw AppExceptionFactory().getException("NoDataException")
        }
        return emptyList()
    }

    override suspend fun getStadiumFieldReservations(
        stadium_key: String,
        field_name: String
    ): List<ReservationModel> {
        if(isListEmpty){
            throw AppExceptionFactory().getException("NoDataException")
        }
        return emptyList()
    }

    override suspend fun getStadiumReservations(key: String): List<ReservationModel> {
        if(isListEmpty){
            throw AppExceptionFactory().getException("NoDataException")
        }
        return emptyList()
    }

    override suspend fun getStadiumDailyReservations(
        key: String,
        date: String
    ): List<ReservationModel> {
        if(isListEmpty){
            throw AppExceptionFactory().getException("NoDataException")
        }
        return emptyList()
    }

    override suspend fun getStadiumReservationsByStatus(
        key: String,
        status: String
    ): List<ReservationModel> {
        if(isListEmpty){
            throw AppExceptionFactory().getException("NoDataException")
        }
        return emptyList()
    }

}