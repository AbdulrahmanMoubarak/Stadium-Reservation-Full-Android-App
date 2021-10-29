package com.training.util.validation

import com.google.firebase.Timestamp
import com.training.model.FieldModel
import com.training.model.ReservationModel
import java.time.LocalDateTime
import java.util.*
import kotlin.math.abs

class DateTimeChecker {
    val MAX_HOURS = 2
    val cal = Calendar.getInstance()

    fun validateTimeOverlap(reservations: List<ReservationModel>, start: Timestamp, end: Timestamp): Boolean{
        for(reservation in reservations){
            if(start.compareTo(reservation.start_time) == 0){
                return false
            }
            if(end.compareTo(reservation.end_time) == 0){
                return false
            }
            if(start.compareTo(reservation.end_time) == 0){
                return true
            }
            if(end.compareTo(reservation.start_time) == 0){
                return true
            }
            if(start.compareTo(reservation.start_time) > 0 && start.compareTo(reservation.end_time) < 0){
                return false
            }
            if(start.compareTo(reservation.start_time) < 0 && end.compareTo(reservation.end_time) > 0){
                return false
            }
            if(start.compareTo(reservation.start_time) > 0 && end.compareTo(reservation.end_time) < 0){
                return false
            }
            if(end.compareTo(reservation.start_time) > 0 && end.compareTo(reservation.end_time) < 0){
                return false
            }
        }
        return true
    }


    fun checkMaxHours(reservations: List<ReservationModel>, start: Timestamp, end: Timestamp): Boolean{
        cal.timeInMillis = start.seconds*1000
        val temp_start = if(cal.get(Calendar.HOUR) > 12) cal.get(Calendar.HOUR) - 12 else cal.get(Calendar.HOUR)

        cal.timeInMillis = end.seconds*1000
        val temp_end = if(cal.get(Calendar.HOUR) > 12) cal.get(Calendar.HOUR) - 12 else cal.get(Calendar.HOUR)

        var max = Math.abs(temp_end-temp_start)

        if(max > MAX_HOURS){
            return false
        }
        for(reservation in reservations){
            cal.timeInMillis = reservation.start_time.seconds*1000
            val res_start = if(cal.get(Calendar.HOUR) > 12) cal.get(Calendar.HOUR) - 12 else cal.get(Calendar.HOUR)

            cal.timeInMillis = reservation.end_time.seconds*1000
            val res_end = if(cal.get(Calendar.HOUR) > 12) cal.get(Calendar.HOUR) - 12 else cal.get(Calendar.HOUR)
            max += abs(res_end - res_start)
            if(max > MAX_HOURS){
                return false
            }
        }
        return true
    }
}