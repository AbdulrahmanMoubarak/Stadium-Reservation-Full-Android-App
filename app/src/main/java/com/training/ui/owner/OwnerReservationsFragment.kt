package com.training.ui.owner

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.R
import com.training.application.MainApplication
import com.training.model.ReservationModel
import com.training.model.UserModel
import com.training.states.AppDataState
import com.training.ui.adapters.ReservationAdapterOwner
import com.training.util.constants.DataError
import com.training.viewmodels.DataRetrieveViewModel
import com.training.viewmodels.EditViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_owner_home.*
import kotlinx.android.synthetic.main.fragment_owner_reservations.*

@AndroidEntryPoint
class OwnerReservationsFragment: Fragment()  {
    private lateinit var user: UserModel
    private val viewModelGet: DataRetrieveViewModel by viewModels()
    private val viewModelEdit: EditViewModel by viewModels()
    private val recyclerAdapter =  ReservationAdapterOwner(::setReservationStatus)
    private lateinit var selected: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        user = getUserData()
        return inflater.inflate(R.layout.fragment_owner_reservations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        
        OwnerReservationRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            this.adapter = recyclerAdapter
        }

        if(user.linked) {

            Ownerspinner_reservations.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        adapterView: AdapterView<*>?,
                        p1: View?,
                        i: Int,
                        p3: Long
                    ) {
                        val selectedItemText = adapterView!!.getItemAtPosition(i) as String
                        selected =
                            when {
                                selectedItemText.equals(getString(R.string.all)) -> "all"
                                selectedItemText.equals(getString(R.string.accepted)) -> "accepted"
                                selectedItemText.equals(getString(R.string.rejected)) -> "rejected"
                                selectedItemText.equals(getString(R.string.pending)) -> "pending"
                                else -> ""
                            }
                        user.stadium_key?.let { viewModelGet.getStadiumReservations(it, selected) }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }
                }
        } else{
            displayErrorView(true)
        }

        observeLiveData()
    }

    private fun setReservationStatus(reservation: ReservationModel) {
        viewModelEdit.updateReservation(reservation)
    }

    private fun displayErrorView(isVisible: Boolean) {
        if (isVisible) {
            OwnerHasReservation.visibility = View.GONE
            OwnerHasNoReservation.visibility = View.VISIBLE
        } else {
            OwnerHasNoReservation.visibility = View.GONE
            OwnerHasReservation.visibility = View.VISIBLE
        }
    }


    private fun getUserData(): UserModel {
        var sp = requireActivity().getSharedPreferences("onLogged", Context.MODE_PRIVATE)
        val user = UserModel(
            sp.getString("email", "email").toString(),
            sp.getString("password", "pass").toString(),
            sp.getString("fname", "").toString(),
            sp.getString("lname", "").toString(),
            sp.getString("phone", "").toString(),
            sp.getString("user type", "owner").toString(),
            sp.getBoolean("first usage", false),
            sp.getBoolean("linked", false),
            sp.getString("stadium_key", "").toString()
        )
        return user
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
                    OwnerReservationRecycler.adapter = recyclerAdapter
                }
                AppDataState.Error::class -> {
                    displayProgressbar(false)
                    val state = data as AppDataState.Error
                    if (state.type == DataError.NO_DATA) {
                        displayErrorView(true)
                    } else
                        Toast.makeText(
                            requireContext(),
                            MainApplication.getApplication().getString(R.string.unexpectedError),
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
                    user.stadium_key?.let { viewModelGet.getStadiumReservations(it, selected) }
                    OwnerReservationRecycler.adapter = recyclerAdapter
                    Toast.makeText(
                        requireContext(),
                        MainApplication.getApplication().getString(R.string.statusChanged),
                        Toast.LENGTH_SHORT
                    ).show()

                }

                AppDataState.Error::class -> {
                    displayProgressbar(false)
                    Toast.makeText(
                        requireContext(),
                        MainApplication.getApplication().getString(R.string.unexpectedError),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
    private fun displayProgressbar(isDisplayed: Boolean) {
        progress_bar_OwnerReservations.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }
}