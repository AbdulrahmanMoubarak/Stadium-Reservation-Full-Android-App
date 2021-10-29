package com.training.model

import com.google.firebase.Timestamp
import com.training.util.constants.ReservationStatus
import com.training.util.constants.ReservationStatus.Companion.ACCEPTED
import java.io.Serializable

class ReservationModel(): Serializable {
    lateinit var user_id: String
    lateinit var stadium_key: String
    var price: Float = 0.0f
    lateinit var start_time: Timestamp
    lateinit var end_time: Timestamp
    lateinit var game: String
    lateinit var status: String
    lateinit var location: String

    constructor(
        userId: String,
        stadiumKey: String,
        price: Float,
        startTime: Timestamp,
        endTime: Timestamp,
        game: String,
        location: String
    ) : this() {
        this.user_id = userId
        this.stadium_key = stadiumKey
        this.price = price
        this.start_time = startTime
        this.end_time = endTime
        this.game = game
        this.status = ReservationStatus.PENDING
        this.location = location
    }

    private fun setReservationStatus(status: String){
        this.status = status
    }
}