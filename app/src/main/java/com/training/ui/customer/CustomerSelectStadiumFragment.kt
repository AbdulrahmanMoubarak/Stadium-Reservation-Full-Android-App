package com.training.ui.customer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.R
import com.training.model.UserModel
import com.training.states.AppDataState
import com.training.ui.adapters.StadiumReserveAdapter
import com.training.viewmodels.DataRetrieveViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_customer_select_stadium.*

@AndroidEntryPoint
class CustomerSelectStadiumFragment : Fragment() {
    private val viewModel: DataRetrieveViewModel by viewModels()


    private lateinit var user: UserModel
    private var adapter = StadiumReserveAdapter(::createNavBundle, ::openNavigation)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable("user") as UserModel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_select_stadium, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getStadiumsList("linked")

        observeLiveData()
        stadiumSelectionRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            this.adapter = adapter
        }
    }

    private fun observeLiveData(){
        viewModel.stadiums_retrieveState.observe(this, { data ->
            when(data::class){
                AppDataState.Loading::class ->{
                    displayProgressbar(true)
                }

                AppDataState.Success::class ->{
                    displayProgressbar(false)
                    val state  = data as AppDataState.Success
                    adapter.setItem_List(state.data)
                    stadiumSelectionRecycler.adapter = adapter
                }

                AppDataState.Error::class ->{
                    adapter.Item_List.clear()
                    stadiumSelectionRecycler.adapter = adapter
                    displayProgressbar(false)
                }
            }
        })

        viewModel.stadium_retrieveState.observe(this, { data ->
            when(data::class){
                AppDataState.Loading::class ->{
                    displayProgressbar(true)
                }

                AppDataState.Success::class ->{
                    displayProgressbar(false)
                    val state  = data as AppDataState.Success
                    openMap(state.data.lat, state.data.long)
                }

                AppDataState.Error::class ->{
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

    private fun openNavigation(key: String) {
        viewModel.getStadiumByKey(key)
    }

    private fun openMap(lat: Double, lng: Double) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?daddr=$lat,$lng")
        )
        requireActivity().startActivity(intent)
    }

    private fun displayProgressbar(isDisplayed:Boolean){
        progress_bar_stadiumListSelect.visibility = if(isDisplayed) View.VISIBLE else View.GONE
    }

    fun createNavBundle(): Bundle{
        return Bundle().apply {
            putSerializable("user", user)
        }
    }

}