package com.training.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.exceptions.KeyAlreadyExistsException
import com.training.exceptions.EmailAlreadyExistsException
import com.training.exceptions.GameAlreadyExistsException
import com.training.exceptions.UnknownErrorException
import com.training.model.FieldModel
import com.training.model.ReservationModel
import com.training.model.StadiumModel
import com.training.model.UserModel
import com.training.repository.RegisterRepositoryInterface
import com.training.states.AppDataState
import com.training.util.validation.ErrorFinder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
@Inject constructor(var repository: RegisterRepositoryInterface) : ViewModel() {

    private val _registerState: MutableLiveData<AppDataState<UserModel>> =
        MutableLiveData<AppDataState<UserModel>>()

    val registerState: MutableLiveData<AppDataState<UserModel>>
        get() = _registerState

    fun addUser(user: UserModel) {
        _registerState.postValue(AppDataState.Loading)
        viewModelScope.launch {
            addUser_suspend(user)
        }
    }

    suspend fun addUser_suspend(user: UserModel) {
        try {
            var firebase_user = user.getFirebaseFormat()
            var err = validateInput(user)
            if (err > -1) {
                repository.addUser(firebase_user)
                _registerState.postValue(AppDataState.OperationSuccess)
                return
            } else {
                _registerState.postValue(AppDataState.Error(err))
                return
            }
        } catch (e: EmailAlreadyExistsException) {
            _registerState.postValue(AppDataState.Error(e.id))
            return
        }
    }

    fun addStadium(stadiumModel: StadiumModel) {
        _registerState.postValue(AppDataState.Loading)
        viewModelScope.launch {
            addStadiumSuspend(stadiumModel)
        }
    }

    suspend fun addStadiumSuspend(stadiumModel: StadiumModel) {
        try {
            val err = validateStadium(stadiumModel)
            if (err > -1) {
                repository.RegisterStadium(stadiumModel)
                _registerState.postValue(AppDataState.OperationSuccess)
                return
            } else {
                _registerState.postValue(AppDataState.Error(err))
                return
            }
        } catch (e: KeyAlreadyExistsException) {
            _registerState.postValue(AppDataState.Error(e.id))
            return
        }
    }

    fun addStadiumField(key: String, field: FieldModel) {
        _registerState.postValue(AppDataState.Loading)
        viewModelScope.launch {
            addStadiumField_suspend(key, field)
        }
    }

    suspend fun addStadiumField_suspend(key: String, field: FieldModel) {
        try {
            repository.AddFieldToStadium(key, field)
            _registerState.postValue(AppDataState.OperationSuccess)
            return
        } catch (e: GameAlreadyExistsException) {
            _registerState.postValue(AppDataState.Error(e.id))
            return
        } catch (e: UnknownErrorException) {
            _registerState.postValue(AppDataState.Error(e.id))
            return
        }
    }

    fun addReservation(reservation: ReservationModel) {
        _registerState.postValue(AppDataState.Loading)
        viewModelScope.launch {
            addReservation_suspend(reservation)
        }
    }

    suspend fun addReservation_suspend(reservation: ReservationModel) {
        try {
            repository.addReservation(reservation)
            _registerState.postValue(AppDataState.OperationSuccess)
            return
        } catch (e: UnknownErrorException) {
            _registerState.postValue(AppDataState.Error(e.id))
            return
        }
    }


    private fun validateInput(user: UserModel): Int {
        return ErrorFinder.getError(user)
    }

    fun validateStadium(stadiumModel: StadiumModel): Int {
        return ErrorFinder.getError(stadiumModel.id, stadiumModel.name, stadiumModel.location_str)
    }

    fun validateStadium(id: String, name: String, loc: String): Int {
        return ErrorFinder.getError(id, name, loc)
    }


}