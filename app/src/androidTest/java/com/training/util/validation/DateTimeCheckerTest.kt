package com.training.util.validation

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.google.firebase.Timestamp
import com.training.model.ReservationModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4ClassRunner::class)
class DateTimeCheckerTest{
    lateinit var checker: DateTimeChecker

    @Before fun setup(){
        checker = DateTimeChecker()
    }

    @Test fun test_time_notMax(){
        val timeStart: Timestamp
        var cal = Calendar.getInstance().apply {
            set(2021,11,12,2,0)
            timeStart = Timestamp(time)
        }

        val timeEnd: Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,3,0)
            timeEnd = Timestamp(time)
        }

        val reservations = arrayListOf<ReservationModel>(
            ReservationModel("","",10.0f, timeStart, timeEnd,"",""),
        )

        val start:Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,5,0)
            start = Timestamp(time)
        }

        val end:Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,6,0)
            end = Timestamp(time)
        }

        val result = checker.checkMaxHours(reservations, start, end)
        assert(result == true)
    }

    @Test fun test_time_Max(){
        val timeStart: Timestamp
        var cal = Calendar.getInstance().apply {
            set(2021,11,12,2,0)
            timeStart = Timestamp(time)
        }

        val timeEnd: Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,4,0)
            timeEnd = Timestamp(time)
        }

        val reservations = arrayListOf<ReservationModel>(
            ReservationModel("","",10.0f, timeStart, timeEnd,"",""),
        )

        val start:Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,5,0)
            start = Timestamp(time)
        }

        val end:Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,6,0)
            end = Timestamp(time)
        }

        val result = checker.checkMaxHours(reservations, start, end)
        assert(result == false)
    }

    @Test fun test_overlap_noOverlap_border(){
        val timeStart: Timestamp
        var cal = Calendar.getInstance().apply {
            set(2021,11,12,2,0)
            timeStart = Timestamp(time)
        }

        val timeEnd: Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,3,0)
            timeEnd = Timestamp(time)
        }

        val reservations = arrayListOf<ReservationModel>(
            ReservationModel("","",10.0f, timeStart, timeEnd,"",""),
        )

        val start:Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,3,0)
            start = Timestamp(time)
        }

        val end:Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,4,0)
            end = Timestamp(time)
        }

        val result = checker.validateTimeOverlap(reservations, start, end)
        assert(result == true)
    }

    @Test fun test_overlap_noOverlap_far(){
        val timeStart: Timestamp
        var cal = Calendar.getInstance().apply {
            set(2021,11,12,2,0)
            timeStart = Timestamp(time)
        }

        val timeEnd: Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,3,0)
            timeEnd = Timestamp(time)
        }

        val reservations = arrayListOf<ReservationModel>(
            ReservationModel("","",10.0f, timeStart, timeEnd,"",""),
        )

        val start:Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,4,0)
            start = Timestamp(time)
        }

        val end:Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,5,0)
            end = Timestamp(time)
        }

        val result = checker.validateTimeOverlap(reservations, start, end)
        assert(result == true)
    }

    @Test fun test_overlap_start(){
        val timeStart: Timestamp
        var cal = Calendar.getInstance().apply {
            set(2021,11,12,2,0)
            timeStart = Timestamp(time)
        }

        val timeEnd: Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,3,0)
            timeEnd = Timestamp(time)
        }

        val reservations = arrayListOf<ReservationModel>(
            ReservationModel("","",10.0f, timeStart, timeEnd,"",""),
        )

        val start:Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,2,30)
            start = Timestamp(time)
        }

        val end:Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,3,30)
            end = Timestamp(time)
        }

        val result = checker.validateTimeOverlap(reservations, start, end)
        assert(result == false)
    }

    @Test fun test_overlap_end(){
        val timeStart: Timestamp
        var cal = Calendar.getInstance().apply {
            set(2021,11,12,2,0)
            timeStart = Timestamp(time)
        }

        val timeEnd: Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,3,0)
            timeEnd = Timestamp(time)
        }

        val reservations = arrayListOf<ReservationModel>(
            ReservationModel("","",10.0f, timeStart, timeEnd,"",""),
        )

        val start:Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,1,30)
            start = Timestamp(time)
        }

        val end:Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,2,30)
            end = Timestamp(time)
        }

        val result = checker.validateTimeOverlap(reservations, start, end)
        assert(result == false)
    }

    @Test fun test_overlap_inner(){
        val timeStart: Timestamp
        var cal = Calendar.getInstance().apply {
            set(2021,11,12,2,0)
            timeStart = Timestamp(time)
        }

        val timeEnd: Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,4,0)
            timeEnd = Timestamp(time)
        }

        val reservations = arrayListOf<ReservationModel>(
            ReservationModel("","",10.0f, timeStart, timeEnd,"",""),
        )

        val start:Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,2,30)
            start = Timestamp(time)
        }

        val end:Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,3,30)
            end = Timestamp(time)
        }

        val result = checker.validateTimeOverlap(reservations, start, end)
        assert(result == false)
    }

    @Test fun test_overlap_outer(){
        val timeStart: Timestamp
        var cal = Calendar.getInstance().apply {
            set(2021,11,12,2,0)
            timeStart = Timestamp(time)
        }

        val timeEnd: Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,3,0)
            timeEnd = Timestamp(time)
        }

        val reservations = arrayListOf<ReservationModel>(
            ReservationModel("","",10.0f, timeStart, timeEnd,"",""),
        )

        val start:Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,1,30)
            start = Timestamp(time)
        }

        val end:Timestamp
        cal = Calendar.getInstance().apply {
            set(2021,11,12,3,30)
            end = Timestamp(time)
        }

        val result = checker.validateTimeOverlap(reservations, start, end)
        assert(result == false)
    }
}