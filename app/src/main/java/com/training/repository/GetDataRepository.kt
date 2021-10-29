package com.training.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.training.exceptions.NoDataException
import com.training.firebase.FirebaseManager
import com.training.model.FieldModel
import com.training.model.ReservationModel
import com.training.model.StadiumModel
import com.training.model.UserModel

class GetDataRepository(var firebaseManager: FirebaseManager): GetDataRepositoryInterface {


    override suspend fun getOwners(): List<UserModel> {

        return firebaseManager.getUsers()
    }

    override suspend fun getOwnersUnlinked(): List<UserModel>{
        return  firebaseManager.getUsersUnlinked()
    }

    override suspend fun getStadiumByKey(key: String): StadiumModel {
        return firebaseManager.getStadiumByKey(key)
    }

    override suspend fun getStadiumFields(key: String): List<FieldModel> {
        return firebaseManager.getStadiumFields(key)
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

    override suspend fun getUserReservations(user: UserModel): List<ReservationModel>{
        return firebaseManager.getReservationsByUser(user)
    }

    override suspend fun getUserReservationsByStatus(
        user: UserModel,
        status: String
    ): List<ReservationModel> {
        return firebaseManager.getReservationsByUserAndStatus(user, status)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun getDayUserReservations(
        user: UserModel,
        date: String
    ): List<ReservationModel> {
        return firebaseManager.getReservationsByUserAndDate(user, date)
    }

    override suspend fun getStadiumFieldReservations(
        stadium_key: String,
        field_name: String
    ): List<ReservationModel> {
        return firebaseManager.getReservationByStadiumField(stadium_key, field_name)
    }
}