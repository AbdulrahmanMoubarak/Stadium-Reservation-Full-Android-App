package com.training.ui.customer

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.Timestamp
import com.training.R
import com.training.application.MainApplication
import kotlinx.android.synthetic.main.fragment_customer_select_time_dialog.view.*
import java.time.LocalDate
import java.util.*


class CustomerSelectTimeDialogFragment(
    var setTimes: (Timestamp, Timestamp) -> Unit,
    var checkReservationTime: ()-> Unit
) : DialogFragment() {

    lateinit var myDate: LocalDate
    lateinit var timeFrom: Timestamp
    lateinit var timeTo: Timestamp
    val now = Calendar.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {



        val builder = AlertDialog.Builder(requireActivity())
        val view = layoutInflater.inflate(R.layout.fragment_customer_select_time_dialog, null)

        builder.setView(view).setTitle(getString(R.string.dateTimeChoose)).setNegativeButton(getString(R.string.cancel)) {
                dialogInterface, i ->

        }.setPositiveButton(getString(R.string.confirm)) {
                dialogInterface, i ->
            if (view.editTextTimeFrom.text.toString() == "" || view.editTextTimeTo.text.toString() == "") {
                Toast.makeText(
                    MainApplication.getAppContext(),
                    getString(R.string.mustChooseTime),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                setTimes(timeFrom, timeTo)
                checkReservationTime()
            }
        }

        view.editTextDate_btn.setOnClickListener {
            val datepicker = DatePickerDialog(requireActivity(), { v, year, month, day ->
                var temp_date = LocalDate.of(year, month+1, day)
                view.editTextDate.setText(temp_date.toString())
                setDate(temp_date)

            }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
            datepicker.datePicker.minDate = System.currentTimeMillis() + 86400000 - 1000
            datepicker.show()
        }

        view.editTextTimeFrom_btn.setOnClickListener {
            if(view.editTextDate.text.toString() != "") {
                val timepicker = TimePickerDialog(
                    requireActivity(),
                    { timePicker, hour, min ->
                        setFromTime(Pair(hour, min))
                        var minStr = if(min < 10) "0${min}" else min.toString()
                        view.editTextTimeFrom.setText("${hour}:${minStr}")
                    },
                    0,
                    0,
                    false
                )
                timepicker.show()
            }else{
                Toast.makeText(MainApplication.getAppContext(), getString(R.string.mustChooseDate), Toast.LENGTH_SHORT).show()
            }
        }

        view.editTextTimeTo_btn.setOnClickListener {
            if(view.editTextDate.text.toString() != "") {
                val timepicker = TimePickerDialog(
                    requireActivity(),
                    { timePicker, hour, min ->
                        setToTime(Pair(hour, min))
                        var minStr = if(min < 10) "0${min}" else min.toString()
                        view.editTextTimeTo.setText("${hour}:${minStr}")
                    },
                    0,
                    0,
                    false
                )
                timepicker.show()
            }else{
                Toast.makeText(MainApplication.getAppContext(), getString(R.string.mustChooseDate), Toast.LENGTH_SHORT).show()
            }
        }

        return builder.create()
    }



    fun setDate(x: LocalDate){
        myDate = x
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setFromTime(time : Pair<Int, Int>){
        val temp_date = Calendar.getInstance()
        temp_date.set(myDate.year, myDate.monthValue-1, myDate.dayOfMonth, time.first, time.second,0)
        timeFrom = Timestamp(temp_date.time)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setToTime(time : Pair<Int, Int>){
        val temp_date = Calendar.getInstance()
        temp_date.set(myDate.year, myDate.monthValue-1, myDate.dayOfMonth, time.first, time.second,0)
        timeTo = Timestamp(temp_date.time)
    }

}