package com.training.ui.customer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.training.R
import com.training.model.ReservationModel
import com.training.model.UserModel
import com.training.states.AppDataState
import com.training.ui.adapters.ReservationAdapter
import com.training.util.constants.DataError
import com.training.util.validation.DateTimeChecker
import com.training.viewmodels.DataRetrieveViewModel
import com.training.viewmodels.EditViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_customer_home.*
import kotlinx.android.synthetic.main.fragment_customer_reservation.*
import kotlinx.android.synthetic.main.fragment_customer_select_game.*
import kotlinx.android.synthetic.main.fragment_customer_select_game.progress_bar_fieldListSelect
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_stadiums.*


@AndroidEntryPoint
class CustomerReservationFragment : Fragment() {
    private lateinit var user: UserModel
    private val viewModelGet: DataRetrieveViewModel by viewModels()
    private val viewModelEdit: EditViewModel by viewModels()
    private val recyclerAdapter = ReservationAdapter(::removeReservation, ::openNavigation)
    private lateinit var selected: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        user = getUserData()
        return inflater.inflate(R.layout.fragment_customer_reservation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CustomerReservationRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            this.adapter = recyclerAdapter
        }

        spinner_reservations.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, p1: View?, i: Int, p3: Long) {
                val selectedItemText = adapterView!!.getItemAtPosition(i) as String
                selected =
                    when {
                        selectedItemText.equals(getString(R.string.all)) -> "all"
                        selectedItemText.equals(getString(R.string.accepted)) -> "accepted"
                        selectedItemText.equals(getString(R.string.rejected)) -> "rejected"
                        selectedItemText.equals(getString(R.string.pending)) -> "pending"
                        else -> ""
                    }
                viewModelGet.getUserReservation(user, selected)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        observeLiveData()

        customerAddReservationButton.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("user", user)
            }
            findNavController().navigate(
                R.id.action_customerReservationFragment_to_customerSelectStadiumFragment,
                bundle
            )
        }
    }


    private fun removeReservation(reservation: ReservationModel){
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
            CustomerHasReservation.visibility = View.GONE
            CustomerHasNoReservation.visibility = View.VISIBLE
        } else {
            CustomerHasNoReservation.visibility = View.GONE
            CustomerHasReservation.visibility = View.VISIBLE
        }
    }

    private fun displayProgressbar(isDisplayed: Boolean) {
        progress_bar_CustomerReservations.visibility = if (isDisplayed) View.VISIBLE else View.GONE
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

    private fun observeLiveData(){
        viewModelGet.reservations_retrieveState.observe(this) { data ->
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
                    CustomerReservationRecycler.adapter = recyclerAdapter
                }
                AppDataState.Error::class -> {
                    displayProgressbar(false)
                    val state = data as AppDataState.Error
                    if (state.type == DataError.NO_DATA) {
                        displayErrorView(true)
                    } else
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.unexpectedError),
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }
        }

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
                        getString(R.string.unexpectedError),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        viewModelEdit.updateState.observe(this, { data ->
            when(data::class){
                AppDataState.Loading::class ->{
                    displayProgressbar(true)
                }

                AppDataState.OperationSuccess::class -> {
                    displayProgressbar(false)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.removed_res),
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModelGet.getUserReservation(user, selected)
                }

                AppDataState.Error::class ->{
                    val state = data as AppDataState.Error
                    displayProgressbar(false)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.unexpectedError),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

}