package com.training.repository

import com.training.model.FieldModel
import com.training.model.ReservationModel
import com.training.model.StadiumModel
import com.training.model.UserModel

interface GetDataRepositoryInterface {
    suspend fun getOwners(): List<UserModel>
    suspend fun getStadiumsUnlinked(): List<StadiumModel>
    suspend fun getStadiumsLinked(): List<StadiumModel>
    suspend fun getAllStadiums(): List<StadiumModel>
    suspend fun getUserByEmail(email: String): UserModel
    suspend fun getOwnersUnlinked(): List<UserModel>
    suspend fun getStadiumByKey(key: String): StadiumModel
    suspend fun getStadiumFields(key: String): List<FieldModel>
    suspend fun getUserReservations(user: UserModel): List<ReservationModel>
    suspend fun getUserReservationsByStatus(user: UserModel, status: String): List<ReservationModel>
    suspend fun getDayUserReservations(user: UserModel, date: String): List<ReservationModel>
    suspend fun getStadiumFieldReservations(stadium_key: String, field_name: String): List<ReservationModel>
    suspend fun getStadiumReservations(key: String): List<ReservationModel>
    suspend fun getStadiumDailyReservations(key: String, date: String): List<ReservationModel>
    suspend fun getStadiumReservationsByStatus(key: String, status: String): List<ReservationModel>
}