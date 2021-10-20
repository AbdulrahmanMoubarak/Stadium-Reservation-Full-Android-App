package com.training.ui.admin

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.training.R
import com.training.model.InventoryModel
import com.training.model.LoginModel
import com.training.model.StadiumModel
import com.training.states.SignInState
import com.training.util.constants.DataError
import com.training.util.validation.ErrorFinder
import com.training.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_stadium_registeration.*
import java.io.IOException


@AndroidEntryPoint
class StadiumRegisterationFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()
    private var StadiumLocationLat: Double? = null
    private var StadiumLocationLong: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stadium_registeration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            Stadium_add_key.setText(it.getString("StadiumKey"))
            Stadium_add_name.setText(it.getString("StadiumName"))
            Stadium_add_location.setText(it.getString("LocationStr"))
            StadiumLocationLat = it.getDouble("LocationLat")
            StadiumLocationLong = it.getDouble("LocationLong")
        }

        stadium_cancel.setOnClickListener {
            requireActivity().onBackPressed()
        }

        stadium_next.setOnClickListener {
            val err = viewModel.validateStadium(
                Stadium_add_key.text.toString(),
                Stadium_add_name.text.toString(),
                Stadium_add_location.text.toString()
            )
            if (err > -1) {
                val bundle = Bundle().apply {
                    putString("StadiumKey", Stadium_add_key.text.toString())
                    putString("StadiumName", Stadium_add_name.text.toString())
                    putString("LocationStr", Stadium_add_location.text.toString())
                    StadiumLocationLat?.let { putDouble("LocationLat", it) }
                    StadiumLocationLong?.let { putDouble("LocationLong", it) }
                }
                findNavController().navigate(R.id.action_stadiumRegisterationFragment_to_inventoryFillFragment, bundle)
            } else {
                showErrorMsg(err)
            }
        }

        location_icon.setOnClickListener {
            if (isConnected()) {
                val bundle = Bundle()
                bundle.apply {
                    putString("StadiumKey", Stadium_add_key.text.toString())
                    putString("StadiumName", Stadium_add_name.text.toString())
                }
                findNavController().navigate(
                    R.id.action_stadiumRegisterationFragment_to_stadiumMapFragment,
                    bundle
                )
            } else {
                showErrorMsg(DataError.NETWORK_ERROR)
            }
        }


    }

    private fun showErrorMsg(error: Int) {
        val error_msg = ErrorFinder.getErrorMsg(error)
        stadium_add_txt_error_msg.text = error_msg
        stadium_add_txt_error_msg.visibility = View.VISIBLE
    }


    @SuppressLint("MissingPermission")
    private fun isConnected(): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}

// validation
// permissions




