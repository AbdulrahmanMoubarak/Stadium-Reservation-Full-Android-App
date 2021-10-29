package com.training.ui.customer

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.R
import com.training.model.ReservationModel
import com.training.model.UserModel
import com.training.states.AppDataState
import com.training.ui.adapters.ReservationAdapter
import com.training.util.constants.DataError
import com.training.viewmodels.DataRetrieveViewModel
import com.training.viewmodels.EditViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_customer_home.*
import kotlinx.android.synthetic.main.fragment_customer_reservation.*
import java.util.*

@AndroidEntryPoint
class CustomerHomeFragment : Fragment() {

    private val viewModelGet: DataRetrieveViewModel by viewModels()
    private val viewModelEdit: EditViewModel by viewModels()
    val recyclerAdapter = ReservationAdapter(::removeReservation, ::openNavigation)
    private lateinit var user: UserModel
    val cal = Calendar.getInstance()

    @RequiresApi(Build.VERSION_CODES.N)
    val today = SimpleDateFormat("MM/dd/yyyy").format(Date(cal.timeInMillis)).toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        user = getUserData()
        return inflater.inflate(R.layout.fragment_customer_home, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CustomerReservationTodayRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            this.adapter = recyclerAdapter
        }

        observeLiveData()

        viewModelGet.getUserDailyReservations(user, today)
    }

    private fun removeReservation(reservation: ReservationModel) {
        viewModelEdit.removeReservation(reservation)
    }

    private fun getUserData(): UserModel {
        var sp = requireActivity().getSharedPreferences("onLogged", Context.MODE_PRIVATE)
        val user = UserModel(
            sp.getString("email", "email").toString(),
            sp.getString("password", "pass").toString(),
            sp.getString("fname", "").toString(),
            sp.getString("lname", "").toString(),
            sp.getString("phone", "").toString(),
            sp.getString("user type", "customer").toString(),
            sp.getBoolean("first usage", false),
            sp.getBoolean("linked", false),
            sp.getString("stadium_key", "").toString()
        )
        return user
    }

    private fun displayErrorView(isVisible: Boolean) {
        if (isVisible) {
            CustomerHasReservationToday.visibility = View.GONE
            CustomerHasNoReservationToday.visibility = View.VISIBLE
        } else {
            CustomerHasNoReservationToday.visibility = View.GONE
            CustomerHasReservationToday.visibility = View.VISIBLE
        }
    }

    private fun displayProgressbar(isDisplayed: Boolean) {
        progress_bar_CustomerHome.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun openNavigation(key: String) {
        viewModelGet.getStadiumByKey(key)
    }

    private fun openMap(lat: Double, lng: Double) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?daddr=$lat,$lng")
        )
        requireActivity().startActivity(intent)
    }

    private fun observeLiveData() {
        viewModelGet.reservations_retrieveState.observe(this, { data ->
            when (data::class) {
                AppDataState.Loading::class -> {
                    displayProgressbar(true)
                    displayErrorView(false)
                }

                AppDataState.Success::class -> {
                    displayProgressbar(false)
                    displayErrorView(false)
                    val state = data as AppDataState.Success
                    recyclerAdapter.setItem_List(state.data)
                    CustomerReservationTodayRecycler.adapter = recyclerAdapter
                }
                AppDataState.Error::class -> {
                    displayProgressbar(false)
                    val state = data as AppDataState.Error
                    if (state.type == DataError.NO_DATA) {
                        displayErrorView(true)
                    } else
                        Toast.makeText(
                            requireContext(),
                            "Unexpected error, please try again",
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }
        })

        viewModelGet.stadium_retrieveState.observe(this, { data ->
            when (data::class) {
                AppDataState.Loading::class -> {
                    displayProgressbar(true)
                }

                AppDataState.Success::class -> {
                    displayProgressbar(false)
                    val state = data as AppDataState.Success
                    openMap(state.data.lat, state.data.long)
                }
                AppDataState.Error::class -> {
                    displayProgressbar(false)
                    Toast.makeText(
                        requireContext(),
                        "Unexpected error, please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        viewModelEdit.updateState.observe(this, { data ->
            when (data::class) {
                AppDataState.Loading::class -> {
                    displayProgressbar(true)
                }

                AppDataState.OperationSuccess::class -> {
                    displayProgressbar(false)
                    Toast.makeText(
                        requireContext(),
                        "Successfully removed reservation",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModelGet.getUserDailyReservations(user, today)
                }

                AppDataState.Error::class -> {
                    displayProgressbar(false)
                    Toast.makeText(
                        requireContext(),
                        "Unexpected error, please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}