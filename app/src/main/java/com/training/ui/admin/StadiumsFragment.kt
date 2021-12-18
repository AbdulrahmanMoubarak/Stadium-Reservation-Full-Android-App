package com.training.ui.admin

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.R
import com.training.model.StadiumModel
import com.training.states.AppDataState
import com.training.ui.adapters.StadiumsAdapter
import com.training.util.constants.DataError
import com.training.util.validation.ErrorFinder
import com.training.viewmodels.DataRetrieveViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_stadiums.*

@AndroidEntryPoint
class StadiumsFragment : Fragment() {

    private val viewModel: DataRetrieveViewModel by viewModels()
    private lateinit var stadium: StadiumModel
    val adapter = StadiumsAdapter()
    private var isLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stadiums, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stadiums_recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setAdapter(adapter)
        }

        observeLiveData()

        admin_stadiums_button.setOnClickListener {
            findNavController().navigate(R.id.action_stadiumsFragment_to_stadiumRegisterationFragment)
        }

        admin_refresh_stadium.setOnClickListener {
            viewModel.getStadiumsList(spinner1.selectedItem.toString())
        }

        spinner1.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, p1: View?, i: Int, p3: Long) {
                val selectedItemText = adapterView!!.getItemAtPosition(i) as String

                val selected =
                    when {
                        selectedItemText.equals(getString(R.string.all)) -> "all"
                        selectedItemText.equals(getString(R.string.linked)) -> "linked"
                        selectedItemText.equals(getString(R.string.unlinked)) -> "unlinked"
                        else -> ""
                    }
                viewModel.getStadiumsList(selected)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    override fun onStart() {
        super.onStart()
        if(isConnected()){
            if(!isLoaded)
                viewModel.getStadiumsList("all")
        }else{
            showErrorMsg(DataError.NETWORK_ERROR)
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
                    stadiums_recyclerView.adapter = adapter
                    isLoaded = true
                    txt_err_stadiumList.visibility = View.GONE
                }

                AppDataState.Error::class ->{
                    adapter.Item_List.clear()
                    stadiums_recyclerView.adapter = adapter
                    val state = data as AppDataState.Error
                    displayProgressbar(false)
                    showErrorMsg(state.type)
                }
            }
        })
    }

    private fun showErrorMsg(error: Int){
        val error_msg = ErrorFinder.getErrorMsg(error)
        txt_err_stadiumList.text = error_msg
        txt_err_stadiumList.visibility = View.VISIBLE
    }

    private fun displayProgressbar(isDisplayed:Boolean){
        progress_bar_stadiumList.visibility = if(isDisplayed) View.VISIBLE else View.GONE
    }

    @SuppressLint("MissingPermission")
    private fun isConnected(): Boolean{
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}