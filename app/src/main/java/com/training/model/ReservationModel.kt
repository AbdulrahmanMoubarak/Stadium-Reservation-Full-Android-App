package com.training.model

data class ReservationModel(
    var user_id: Int,
    var stadium_id: Int,
    var price: Float,
    var date: String,
    var time: String
) {
}