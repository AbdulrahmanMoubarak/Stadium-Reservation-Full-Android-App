package com.training.ui.admin

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.training.R
import com.training.model.InventoryModel
import com.training.model.StadiumModel
import com.training.states.AppDataState
import com.training.util.validation.ErrorFinder
import com.training.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_inventory_fill.*

@AndroidEntryPoint
class InventoryFillFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()

    private var StadiumName: String? = null
    private var StadiumKey: String? = null
    private var StadiumLocationStr: String? = null
    private var StadiumLocationLat: Double? = null
    private var StadiumLocationLong: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            StadiumKey = it.getString("StadiumKey")
            StadiumName = it.getString("StadiumName")
            StadiumLocationStr = it.getString("LocationStr")
            StadiumLocationLat = it.getDouble("LocationLat")
            StadiumLocationLong = it.getDouble("LocationLong")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_inventory_fill, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readTempData()
        subscribeLiveData()

        inventory_back.setOnClickListener {
            saveTempData(true)
            requireActivity().onBackPressed()
        }

        inventory_confirm.setOnClickListener {
            fillEmptyInventoryItems()
            var inventory = InventoryModel(
                HashMap<String, Int>().apply {
                    this.put(
                        "soccer balls",
                        inventory_soccer_balls.text.toString().toInt()
                    )
                    this.put(
                        "basket balls",
                        inventory_basket_balls.text.toString().toInt()
                    )
                    this.put(
                        "rackets",
                        inventory_rackets.text.toString().toInt()
                    )
                    this.put(
                        "tennis balls",
                        inventory_tennis_balls.text.toString().toInt()
                    )
                    this.put(
                        "nets",
                        inventory_nets.text.toString().toInt()
                    )
                }
            )


            var stadium = StadiumModel(
                StadiumKey,
                StadiumName,
                StadiumLocationStr,
                Pair(StadiumLocationLat, StadiumLocationLong),
                inventory
            )

            saveTempData(false)
            viewModel.addStadium(stadium)
        }
    }


    private fun fillEmptyInventoryItems() {
        if (inventory_soccer_balls.text.toString().equals("")) inventory_soccer_balls.setText("0")
        if (inventory_basket_balls.text.toString().equals("")) inventory_basket_balls.setText("0")
        if (inventory_rackets.text.toString().equals("")) inventory_rackets.setText("0")
        if (inventory_tennis_balls.text.toString().equals("")) inventory_tennis_balls.setText("0")
        if (inventory_nets.text.toString().equals("")) inventory_nets.setText("0")
    }

    private fun subscribeLiveData(){
        viewModel.registerState.observe(this, {data ->
            when(data::class){
                AppDataState.Loading::class ->{
                    displayProgressbar(true)
                }

                AppDataState.OperationSuccess::class ->{
                    displayProgressbar(false)
                    Toast.makeText(requireContext(), "Successfully added stadium", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_inventoryFillFragment_to_stadiumsFragment)
                }

                AppDataState.Error::class ->{
                    val state = data as AppDataState.Error
                    displayProgressbar(false)
                    showErrorMsg(state.type)
                }
            }
        })
    }

    private fun displayProgressbar(isDisplayed:Boolean){
        progress_bar_stadium.visibility = if(isDisplayed) View.VISIBLE else View.GONE
    }

    private fun showErrorMsg(error: Int){
        val error_msg = ErrorFinder.getErrorMsg(error)
        inventory_add_txt_error_msg.text = error_msg
        inventory_add_txt_error_msg.visibility = View.VISIBLE
    }

    private fun saveTempData(flag: Boolean){
        val sp = requireActivity().getSharedPreferences("inventoryTemp", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.apply {
            putBoolean("exist", flag)
            putString("soccer balls", inventory_soccer_balls.text.toString())
            putString("basket balls", inventory_basket_balls.text.toString())
            putString("rackets", inventory_rackets.text.toString())
            putString("tennis balls", inventory_tennis_balls.text.toString())
            putString("nets", inventory_nets.text.toString())
        }.apply()
    }

    private fun readTempData(){
        val sp = requireActivity().getSharedPreferences("inventoryTemp", Context.MODE_PRIVATE)
        sp.apply {
            val exists = sp.getBoolean("exist", false)
            if(exists){
                inventory_soccer_balls.setText(getString("soccer balls", ""))
                inventory_basket_balls.setText(getString("basket balls", ""))
                inventory_rackets.setText(getString("rackets",""))
                inventory_tennis_balls.setText(getString("tennis balls", ""))
                inventory_nets.setText(getString("nets", ""))
            }
        }
    }
}