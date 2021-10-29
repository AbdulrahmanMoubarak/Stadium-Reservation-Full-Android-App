package com.training.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.exceptions.InvalidUserException
import com.training.exceptions.KeyDoesnotExistException
import com.training.exceptions.NoDataException
import com.training.model.FieldModel
import com.training.model.ReservationModel
import com.training.model.StadiumModel
import com.training.model.UserModel
import com.training.repository.GetDataRepositoryInterface
import com.training.states.AppDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataRetrieveViewModel
@Inject
constructor(var repository: GetDataRepositoryInterface): ViewModel() {

    private val _user_retrieveState: MutableLiveData<AppDataState<UserModel>> =
        MutableLiveData<AppDataState<UserModel>>()

    val user_retrieveState: MutableLiveData<AppDataState<UserModel>>
        get() = _user_retrieveState

    private val _retrieveState: MutableLiveData<AppDataState<List<UserModel>>> =
        MutableLiveData<AppDataState<List<UserModel>>>()

    val retrieveState: MutableLiveData<AppDataState<List<UserModel>>>
        get() = _retrieveState

    private val _stadiums_retrieveState: MutableLiveData<AppDataState<List<StadiumModel>>> =
        MutableLiveData<AppDataState<List<StadiumModel>>>()

    val stadiums_retrieveState: MutableLiveData<AppDataState<List<StadiumModel>>>
        get() = _stadiums_retrieveState

    private val _stadium_retrieveState: MutableLiveData<AppDataState<StadiumModel>> =
        MutableLiveData<AppDataState<StadiumModel>>()

    val stadium_retrieveState: MutableLiveData<AppDataState<StadiumModel>>
        get() = _stadium_retrieveState

    private val _fields_retrieveState: MutableLiveData<AppDataState<List<FieldModel>>> =
        MutableLiveData<AppDataState<List<FieldModel>>>()

    val fields_retrieveState: MutableLiveData<AppDataState<List<FieldModel>>>
        get() = _fields_retrieveState

    private val _reservations_retrieveState: MutableLiveData<AppDataState<List<ReservationModel>>> =
        MutableLiveData<AppDataState<List<ReservationModel>>>()

    val reservations_retrieveState: MutableLiveData<AppDataState<List<ReservationModel>>>
        get() = _reservations_retrieveState

    fun getAllUsers(){
        _retrieveState.postValue(AppDataState.Loading)
        viewModelScope.launch {
            getAllUsers_suspend()
        }
    }

    suspend fun getAllUsers_suspend(){
        try{
            val users = repository.getOwners()
            _retrieveState.postValue(AppDataState.Success(users))
        }catch (e: NoDataException){
            _retrieveState.postValue(AppDataState.Error(e.id))
        }
    }

    fun getUsersUnlinked(){
        _retrieveState.postValue(AppDataState.Loading)
        viewModelScope.launch {
            getUsersUnlinked_suspend()
        }
    }

    suspend fun getUsersUnlinked_suspend(){
        try {
            val users = repository.getOwnersUnlinked()
            _retrieveState.postValue(AppDataState.Success(users))
        }catch (e: NoDataException){
            _retrieveState.postValue(AppDataState.Error(e.id))
        }
    }

    fun getStadiumsList(filter: String){
        _stadiums_retrieveState.postValue(AppDataState.Loading)
        viewModelScope.launch {
            getStadiumsList_suspend(filter)
        }
    }

    suspend fun getStadiumsList_suspend(filter: String){
        try {
            var stadiumList: List<StadiumModel>
            when (filter.lowercase()) {
                "all" -> {
                    stadiumList=repository.getAllStadiums()
                }

                "linked" -> {
                    stadiumList=repository.getStadiumsLinked()
                }

                "unlinked" -> {
                    stadiumList=repository.getStadiumsUnlinked()
                }

                else ->{
                    stadiumList=repository.getAllStadiums()
                }
            }
            _stadiums_retrieveState.postValue(AppDataState.Success(stadiumList))
        }catch (e: NoDataException){
            _stadiums_retrieveState.postValue(AppDataState.Error(e.id))
        }
    }

    fun getUserByEmail(email: String){
        viewModelScope.launch {
            getUserByEmail_suspend(email)
        }
    }

    suspend fun getUserByEmail_suspend(email: String){
        try{
            val user = repository.getUserByEmail(email)
            _user_retrieveState.postValue(AppDataState.Success(user.getViewFormat()))
        } catch (e: InvalidUserException) {
            _user_retrieveState.postValue(AppDataState.Error(e.id))
            return
        }
    }

    fun getStadiumByKey(key: String){
        _stadium_retrieveState.postValue(AppDataState.Loading)
        viewModelScope.launch {
            getStadiumByKey_suspend(key)
        }
    }

    suspend fun getStadiumByKey_suspend(key: String){
        try {
            val stadium = repository.getStadiumByKey(key)
            _stadium_retrieveState.postValue(AppDataState.Success(stadium))
        }catch (e: KeyDoesnotExistException){
            _stadium_retrieveState.postValue(AppDataState.Error(e.id))
            return
        }
    }

    fun getStadiumFields(key: String){
        _stadium_retrieveState.postValue(AppDataState.Loading)
        viewModelScope.launch {
            getStadiumFields_suspend(key)
        }
    }

    suspend fun getStadiumFields_suspend(key: String){
        try {
            val fields = repository.getStadiumFields(key)
            _fields_retrieveState.postValue(AppDataState.Success(fields))
        }catch (e: NoDataException){
            _fields_retrieveState.postValue(AppDataState.Error(e.id))
        }
    }

    fun getUserReservation(user: UserModel, status: String){
        _reservations_retrieveState.postValue(AppDataState.Loading)
        viewModelScope.launch {
            getUserReservation_suspend(user.getFirebaseFormat(), status)
        }
    }

    suspend fun getUserReservation_suspend(user: UserModel, status: String){
        try {
            if(status.equals("all")) {
                val res_list = repository.getUserReservations(user)
                _reservations_retrieveState.postValue(AppDataState.Success(res_list))
            }else{
                val res_list = repository.getUserReservationsByStatus(user, status)
                _reservations_retrieveState.postValue(AppDataState.Success(res_list))
            }
        }catch (e: NoDataException){
            _reservations_retrieveState.postValue(AppDataState.Error(e.id))
        }
    }

    fun getUserDailyReservations(user: UserModel, date: String){
        _reservations_retrieveState.postValue(AppDataState.Loading)
        viewModelScope.launch {
            getUserDailyReservations_suspend(user.getFirebaseFormat(), date)
        }
    }

    suspend fun getUserDailyReservations_suspend(user: UserModel, date: String){
        try {
            val res_list = repository.getDayUserReservations(user, date)
            _reservations_retrieveState.postValue(AppDataState.Success(res_list))
        }catch(e: NoDataException){
            _reservations_retrieveState.postValue(AppDataState.Error(e.id))
        }
    }

    fun getStadiumFieldReservations(stadium_key: String, field_name: String){
        _reservations_retrieveState.postValue(AppDataState.Loading)
        viewModelScope.launch {
            getStadiumFieldReservations_suspend(stadium_key,field_name)
        }
    }

    suspend fun getStadiumFieldReservations_suspend(stadium_key: String, field_name: String){
        try {
            val res_list = repository.getStadiumFieldReservations(stadium_key,field_name)
            _reservations_retrieveState.postValue(AppDataState.Success(res_list))
        }catch(e: NoDataException){
            _reservations_retrieveState.postValue(AppDataState.Error(e.id))
        }
    }

}