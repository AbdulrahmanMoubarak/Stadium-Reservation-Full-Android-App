package com.training.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.exceptions.EmailAlreadyExistsException
import com.training.exceptions.UnknownErrorException
import com.training.model.ReservationModel
import com.training.model.StadiumModel
import com.training.model.UserModel
import com.training.repository.EditRepository
import com.training.states.AppDataState
import com.training.util.validation.ErrorFinder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel
@Inject
constructor(var repository: EditRepository) : ViewModel() {

    private val _updateState: MutableLiveData<AppDataState<UserModel>> =
        MutableLiveData<AppDataState<UserModel>>()

    val updateState: MutableLiveData<AppDataState<UserModel>>
        get() = _updateState

    fun updateUserData(user: UserModel, isPassChanged: Boolean) {
        updateState.postValue(AppDataState.Loading)
        try {
            viewModelScope.launch {
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
                    return@launch
                } else {
                    _updateState.postValue(AppDataState.Error(err))
                    return@launch
                }
            }
        } catch (e: EmailAlreadyExistsException) {
            _updateState.postValue(AppDataState.Error(e.id))
            return
        }
    }

    fun updateStadiumData(stadium: StadiumModel) {
        _updateState.postValue(AppDataState.Loading)
        try {
            viewModelScope.launch {
                repository.editStadium(stadium)
                _updateState.postValue(AppDataState.OperationSuccess)
                return@launch
            }
        } catch (e: UnknownErrorException) {
            _updateState.postValue(AppDataState.Error(e.id))
            return
        }
    }

    fun removeReservation(reservation: ReservationModel){
        _updateState.postValue(AppDataState.Loading)
        try {
            viewModelScope.launch {
                repository.removeReservation(reservation)
                _updateState.postValue(AppDataState.OperationSuccess)
                return@launch
            }
        } catch (e: UnknownErrorException) {
            _updateState.postValue(AppDataState.Error(e.id))
            return
        }
    }

    private fun validateInput(user: UserModel): Int {
        return ErrorFinder.getError(user)
    }
}