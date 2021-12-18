package com.training.ui.owner

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.R
import com.training.application.MainApplication
import com.training.model.FieldModel
import com.training.model.StadiumModel
import com.training.model.UserModel
import com.training.states.AppDataState
import com.training.ui.adapters.FieldAdapter
import com.training.util.validation.ErrorFinder
import com.training.viewmodels.DataRetrieveViewModel
import com.training.viewmodels.EditViewModel
import com.training.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_customer_reservation.*
import kotlinx.android.synthetic.main.fragment_owner_stadium.*

@AndroidEntryPoint
class OwnerStadiumFragment : Fragment() {

    private val viewModelGet: DataRetrieveViewModel by viewModels()
    private val viewModelRegister: RegisterViewModel by viewModels()
    private val viewModelEdit: EditViewModel by viewModels()
    private lateinit var user: UserModel
    private lateinit var stadium: StadiumModel
    private lateinit var fieldData: FieldModel
    private var isRemove = false
    private var adapter = FieldAdapter(::removeField, ::updateFieldStatus, ::openAlertDialog)

    private fun onPressOk(field: FieldModel){
        this.fieldData = field
        viewModelRegister.addStadiumField(stadium.id, fieldData)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = getLocalUserData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_owner_stadium, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        owner_stadiumFieldsRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            setAdapter(adapter)
        }

        if(user.linked){
            layout_hasStadium.visibility = View.VISIBLE
            user.stadium_key?.let{viewModelGet.getStadiumByKey(it)}
            user.stadium_key?.let{viewModelGet.getStadiumFields(it)}
        }
        else{
            txt_hasNoStadium.visibility = View.VISIBLE
        }

        subscribeLiveData()
        subscribeRegisterLiveData()

        owner_fieldAdd.setOnClickListener {
            openDialogue()
        }

        owner_stadium_active.setOnClickListener {
            stadium.active = !stadium.active
            viewModelEdit.updateStadiumData(stadium)
        }
    }

    private fun subscribeLiveData(){
        viewModelGet.stadium_retrieveState.observe(this, {data ->
            when(data::class){
                AppDataState.Loading::class ->{
                    displayProgressbar(true)
                }

                AppDataState.Success::class ->{
                    displayProgressbar(false)
                    val state  = data as AppDataState.Success
                    stadium = state.data
                    displayStadiumData()

                }

                AppDataState.Error::class ->{
                    Log.d("Here", "subscribeLiveData: Error")
                }
            }
        })


        viewModelGet.fields_retrieveState.observe(this, {data ->
            when(data::class){
                AppDataState.Loading::class ->{
                    displayProgressbar(true)
                }

                AppDataState.Success::class ->{
                    displayProgressbar(false)
                    val state  = data as AppDataState.Success
                    adapter.setItem_List(state.data)
                    owner_stadiumFieldsRecycler.adapter = adapter
                    displayFields()
                }

                AppDataState.Error::class ->{
                    Log.d("Here", "subscribeLiveData: Error")
                    txt_noFields.visibility = View.VISIBLE
                    owner_StadiumFields.visibility = View.GONE
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
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.cannotDeleteField),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                AppDataState.Error::class -> {
                    displayProgressbar(false)
                    if(isRemove) {
                        viewModelEdit.removeStadiumField(fieldData)
                        adapter.Item_List.remove(fieldData)
                    }else{
                        viewModelEdit.updateStadiumField(fieldData)
                    }
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
                    if(isRemove) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.removed_field),
                            Toast.LENGTH_SHORT
                        ).show()
                        isRemove = false
                        owner_stadiumFieldsRecycler.adapter = adapter
                    } else{
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.statusChanged),
                            Toast.LENGTH_SHORT
                        ).show()
                        owner_stadiumFieldsRecycler.adapter = adapter
                    }
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

    private fun removeField(field: FieldModel){
        fieldData = field
        isRemove = true
        viewModelGet.getStadiumFieldReservations(field.stadium_key, field.game)
    }

    private fun updateFieldStatus(field: FieldModel){
        viewModelEdit.updateStadiumField(field)
    }

    private fun displayStadiumData() {
        owner_stadiumName.setText(stadium.name)
        owner_stadiumLocation.setText(stadium.location_str)
        setAvailable()
    }

    private fun displayProgressbar(isDisplayed:Boolean){
        owner_stadium_progress_bar_loading.visibility = if(isDisplayed) View.VISIBLE else View.GONE
    }


    private fun getLocalUserData(): UserModel {
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

    private fun subscribeRegisterLiveData(){
        viewModelRegister.registerState.observe(this, {data ->
            when(data::class){
                AppDataState.Loading::class ->{
                    displayProgressbar(true)
                }

                AppDataState.OperationSuccess::class ->{
                    displayProgressbar(false)
                    Toast.makeText(requireContext(), "Successfully added game", Toast.LENGTH_SHORT).show()
                    adapter.addItem(fieldData)
                    owner_stadiumFieldsRecycler.adapter = adapter
                    displayFields()
                }

                AppDataState.Error::class ->{
                    displayProgressbar(false)
                    val state  = data as AppDataState.Error
                    Toast.makeText(requireContext(), ErrorFinder.getErrorMsg(state.type), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun openDialogue(){
        var dialog = FieldDialogueFragment(::onPressOk)
        dialog.show(requireActivity().supportFragmentManager, "Field")
    }

    private fun displayFields(){
        if(adapter.Item_List.size != 0){
            txt_noFields.visibility = View.GONE
            owner_StadiumFields.visibility = View.VISIBLE
        }else{
            txt_noFields.visibility = View.VISIBLE
            owner_StadiumFields.visibility = View.GONE
        }
    }

    private fun openAlertDialog():  AlertDialog.Builder{
        val dialogBuilder = AlertDialog.Builder(requireContext()).apply {
            setMessage(MainApplication.getApplication().getString(R.string.remove))
            setTitle(MainApplication.getApplication().getString(R.string.warning))
        }
        return dialogBuilder
    }

    private fun setAvailable(){
        owner_stadium_active.isChecked = stadium.active
    }
}