package com.training.ui.customer

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.training.R
import com.training.model.FieldModel
import com.training.model.ReservationModel
import com.training.model.StadiumModel
import com.training.model.UserModel
import com.training.states.AppDataState
import com.training.ui.adapters.FieldReserveAdapter
import com.training.util.constants.DataError.Companion.NO_DATA
import com.training.util.validation.DateTimeChecker
import com.training.util.validation.ErrorFinder
import com.training.viewmodels.DataRetrieveViewModel
import com.training.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_customer_select_game.*
import kotlinx.android.synthetic.main.fragment_field_dialogue.*
import kotlinx.android.synthetic.main.fragment_owner_stadium.*
import java.time.LocalDate
import java.util.*

@AndroidEntryPoint
class CustomerSelectGameFragment : Fragment() {

    private val viewModelGet: DataRetrieveViewModel by viewModels()
    private val viewModelRegister: RegisterViewModel by viewModels()
    private lateinit var user: UserModel
    private lateinit var stadium: StadiumModel
    private lateinit var selected_field: FieldModel
    lateinit var timeFrom: Timestamp
    lateinit var timeTo: Timestamp
    private var myAdapter = FieldReserveAdapter(::openDialogue)
    private var maxHoursChecked = false
    private var overlapChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable("user") as UserModel
            stadium = it.getSerializable("stadium") as StadiumModel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_select_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fieldSelectionRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            this.adapter = myAdapter
        }

        subscribeLiveData()

        viewModelGet.getStadiumFields(stadium.id)
    }

    private fun subscribeLiveData() {
        viewModelGet.fields_retrieveState.observe(this, { data ->
            when (data::class) {
                AppDataState.Loading::class -> {
                    displayProgressbar(true)
                }

                AppDataState.Success::class -> {
                    displayProgressbar(false)
                    val state = data as AppDataState.Success
                    myAdapter.setItem_List(state.data)
                    fieldSelectionRecycler.adapter = myAdapter
                }

                AppDataState.Error::class -> {
                    Log.d("Here", "subscribeLiveData: Error")
                }
            }
        })

        viewModelGet.reservations_retrieveState.observe(this, { data ->
            when (data::class) {
                AppDataState.Loading::class -> {
                    displayProgressbar(true)
                }

                AppDataState.Success::class -> {
                    displayProgressbar(false)
                    val state = data as AppDataState.Success
                    if (!maxHoursChecked) {
                        if (DateTimeChecker().checkMaxHours(state.data, timeFrom, timeTo)) {
                            viewModelGet.getStadiumFieldReservations(
                                stadium.id,
                                selected_field.game
                            )
                            maxHoursChecked = true
                        } else {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.maxHours),
                                Toast.LENGTH_SHORT
                            ).show()

                            maxHoursChecked = false
                            overlapChecked = false
                        }
                    } else {
                        if (DateTimeChecker().validateTimeOverlap(
                                state.data,
                                timeFrom,
                                timeTo
                            )
                        ) {
                            overlapChecked = true
                            submitReservation()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.fieldRegistered),
                                Toast.LENGTH_SHORT
                            ).show()
                            maxHoursChecked = false
                            overlapChecked = false
                        }
                    }
                }
                AppDataState.Error::class -> {
                    displayProgressbar(false)
                    val state = data as AppDataState.Error
                    if (state.type == NO_DATA) {
                        if (!maxHoursChecked) {
                            if (DateTimeChecker().checkMaxHours(arrayListOf<ReservationModel>(), timeFrom, timeTo)) {
                                viewModelGet.getStadiumFieldReservations(
                                    stadium.id,
                                    selected_field.game
                                )
                                maxHoursChecked = true
                            }else{
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.maxHours2),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {

                            overlapChecked = true
                            submitReservation()
                        }
                    } else
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.unexpectedError),
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }
        })

        viewModelRegister.registerState.observe(this, {data->
            when(data::class){
                AppDataState.Loading::class ->{
                    displayProgressbar(true)
                }

                AppDataState.OperationSuccess::class ->{
                    displayProgressbar(false)
                    Toast.makeText(requireContext(), getString(R.string.res_complete), Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_customerSelectGameFragment_to_customerReservationFragment)
                }

                AppDataState.Error::class ->{
                    displayProgressbar(false)
                    val state  = data as AppDataState.Error
                    Toast.makeText(requireContext(), ErrorFinder.getErrorMsg(state.type), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun displayProgressbar(isDisplayed: Boolean) {
        progress_bar_fieldListSelect.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun openDialogue(field: FieldModel) {
        this.selected_field = field
        var dialog = CustomerSelectTimeDialogFragment(::setTimes, ::validateReservation)
        dialog.show(requireActivity().supportFragmentManager, getString(R.string.time_select))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun validateReservation() {
        var hours = Math.abs(((timeTo.seconds - timeFrom.seconds)/60)/60)
        val res = ReservationModel(
            user.getFirebaseFormat().email,
            stadium.id,
            selected_field.price*hours,
            timeFrom,
            timeTo,
            selected_field.game,
            stadium.location_str
        )
        val milliseconds = res.start_time.seconds * 1000 + res.start_time.nanoseconds / 1000000
        val date = SimpleDateFormat("MM/dd/yyyy").format(Date(milliseconds)).toString()
        viewModelGet.getUserDailyReservations(user, date)
    }

    fun submitReservation() {
        maxHoursChecked = false
        overlapChecked = false
        var hours = Math.abs(((timeTo.seconds - timeFrom.seconds)/60)/60)
        val res = ReservationModel(
            user.getFirebaseFormat().email,
            stadium.id,
            selected_field.price*hours,
            timeFrom,
            timeTo,
            selected_field.game,
            stadium.location_str
        )
        viewModelRegister.addReservation(res)
    }

    private fun setTimes(from: Timestamp, to: Timestamp) {
        this.timeFrom = from
        this.timeTo = to
    }

}