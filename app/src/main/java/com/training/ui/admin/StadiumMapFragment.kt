package com.training.ui.admin

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.training.R
import kotlinx.android.synthetic.main.fragment_stadium_map.*
import kotlinx.android.synthetic.main.fragment_stadium_registeration.*
import java.io.IOException

class StadiumMapFragment : Fragment(), OnMapReadyCallback {
    private var StadiumKey: String? = null
    private var StadiumName: String? = null


    private lateinit var mMap: GoogleMap
    private var currentLocation: Pair<Double, Double> = Pair(0.0, 0.0)
    private var currentLocationStr = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            StadiumKey = it.getString("StadiumKey")
            StadiumName = it.getString("StadiumName")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stadium_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMap()
        map_SearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                val location: String = map_SearchView.getQuery().toString()
                var addressList: List<Address>? = null
                if (location != null || location == "") {
                    val geocoder = Geocoder(requireContext())
                    try {
                        addressList = geocoder.getFromLocationName(location, 1)
                        addressList.let {
                            if(it.size > 0) {
                                val address: Address = it[0]
                                val latLng = LatLng(address.getLatitude(), address.getLongitude())
                                currentLocationStr = address.getAddressLine(0)
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                                val zoomLevel = 16.0f //This goes up to 21
                                mMap.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        latLng,
                                        zoomLevel
                                    )
                                )
                            }
                        }
                    } catch (e: IOException) {

                    }

                }
                return false
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })

        map_confirm.setOnClickListener {
            val bundle = Bundle()
            bundle.apply {
                putString("StadiumKey", StadiumKey)
                putString("StadiumName", StadiumName)
                putString("LocationStr", currentLocationStr)
                putDouble("LocationLat", currentLocation.first)
                putDouble("LocationLong", currentLocation.second)
            }
            findNavController().navigate(R.id.action_stadiumMapFragment_to_stadiumRegisterationFragment, bundle)
        }
    }

    private fun setupMap() {
        val mapFragment = SupportMapFragment.newInstance()
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.map, mapFragment)
            .commit()

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val success = googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(), R.raw.style_json
            )
        )
        if (!success) {
            Log.d("here", "Style parsing failed.");
        }
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(LatLng(currentLocation.first, currentLocation.second)))

        googleMap.setOnMapClickListener {
            googleMap.clear()
            map_confirm.visibility = View.VISIBLE
            currentLocation = Pair(it.latitude, it.longitude)
            googleMap.addMarker(
                MarkerOptions()
                    .position(LatLng(it.latitude, it.longitude))
                    .title("Stadium Location")
            )
        }
    }
}