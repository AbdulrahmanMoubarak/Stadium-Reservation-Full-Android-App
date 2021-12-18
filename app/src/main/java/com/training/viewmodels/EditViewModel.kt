package com.training.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.exceptions.EmailAlreadyExistsException
import com.training.exceptions.UnknownErrorException
import com.training.model.FieldModel
import com.training.model.ReservationModel
import com.training.model.StadiumModel
import com.training.model.UserModel
import com.training.repository.EditRepository
import com.training.repository.EditRepositoryInterface
import com.training.states.AppDataState
import com.training.util.validation.ErrorFinder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel
@Inject
constructor(var repository: EditRepositoryInterface) : ViewModel() {

    private val _updateState: MutableLiveData<AppDataState<UserModel>> =
        MutableLiveData<AppDataState<UserModel>>()

    val updateState: MutableLiveData<AppDataState<UserModel>>
        get() = _updateState

    fun updateUserData(user: UserModel, isPassChanged: Boolean) {
        updateState.postValue(AppDataState.Loading)
        viewModelScope.launch {
            updateUserData_suspend(user, isPassChanged)
        }
    }

    suspend fun updateUserData_suspend(user: UserModel, isPassChanged: Boolean) {
        try {
            var err = validateInput(user)
            var password: String
            if (err > -1) {
                if (isPassChanged) {
                    repository.editUser(user.getFirebaseFormat())
                } else {
                    password = user.password
                    val userTemp = user.getFirebaseFormat()
                    userTemp.password = password
                    repository.editUser(userTemp)
                }
                _updateState.postValue(AppDataState.Success(user))
                delay(100)
                _updateState.postValue(AppDataState.OperationSuccess)
                return
            } else {
                _updateState.postValue(AppDataState.Error(err))
                return
            }
        } catch (e: EmailAlreadyExistsException) {
            _updateState.postValue(AppDataState.Error(e.id))
            return
        }

    }

    fun updateStadiumData(stadium: StadiumModel) {
        _updateState.postValue(AppDataState.Loading)
        viewModelScope.launch {
            updateStadiumData_suspend(stadium)
        }
    }

    suspend fun updateStadiumData_suspend(stadium: StadiumModel) {
        try {
            repository.editStadium(stadium)
            _updateState.postValue(AppDataState.OperationSuccess)
            return
        } catch (e: UnknownErrorException) {
            _updateState.postValue(AppDataState.Error(e.id))
            return
        }
    }

    fun removeReservation(reservation: ReservationModel) {
        _updateState.postValue(AppDataState.Loading)
        viewModelScope.launch {
            removeReservation_suspend(reservation)
        }
    }

    suspend fun removeReservation_suspend(reservation: ReservationModel) {
        try {
            repository.removeReservation(reservation)
            _updateState.postValue(AppDataState.OperationSuccess)
            return
        } catch (e: UnknownErrorException) {
            _updateState.postValue(AppDataState.Error(e.id))
            return
        }
    }

    fun removeStadiumField(field: FieldModel) {
        _updateState.postValue(AppDataState.Loading)
        viewModelScope.launch {
            removeStadiumField_suspend(field)
        }
    }

    suspend fun removeStadiumField_suspend(field: FieldModel) {
        try {
            repository.removeStadiumField(field)
            _updateState.postValue(AppDataState.OperationSuccess)
            return
        } catch (e: UnknownErrorException) {
            _updateState.postValue(AppDataState.Error(e.id))
            return
        }
    }

    fun updateStadiumField(field: FieldModel) {
        _updateState.postValue(AppDataState.Loading)
        viewModelScope.launch {
            updateStadiumField_suspend(field)
        }
    }

    suspend fun updateStadiumField_suspend(field: FieldModel) {
        try {
            repository.updateStadiumField(field)
            _updateState.postValue(AppDataState.OperationSuccess)
            return

        } catch (e: UnknownErrorException) {
            _updateState.postValue(AppDataState.Error(e.id))
            return
        }
    }

    fun updateReservation(reservation: ReservationModel) {
        _updateState.postValue(AppDataState.Loading)
        viewModelScope.launch {
            updateReservation_suspend(reservation)
        }
    }

    suspend fun updateReservation_suspend(reservation: ReservationModel) {
        try {
            repository.updateReservation(reservation)
            _updateState.postValue(AppDataState.OperationSuccess)
            return
        } catch (e: UnknownErrorException) {
            _updateState.postValue(AppDataState.Error(e.id))
            return
        }
    }

    private fun validateInput(user: UserModel): Int {
        return ErrorFinder.getError(user)
    }
}
