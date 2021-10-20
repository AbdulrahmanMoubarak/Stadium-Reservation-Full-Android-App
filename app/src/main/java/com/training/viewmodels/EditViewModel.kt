package com.training.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.exceptions.EmailAlreadyExistsException
import com.training.exceptions.UnknownErrorException
import com.training.model.StadiumModel
import com.training.model.UserModel
import com.training.repository.EditRepository
import com.training.states.SignInState
import com.training.util.validation.ErrorFinder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel
@Inject
constructor(var repository: EditRepository) : ViewModel() {

    private val _updateState: MutableLiveData<SignInState<UserModel>> =
        MutableLiveData<SignInState<UserModel>>()

    val updateState: MutableLiveData<SignInState<UserModel>>
        get() = _updateState

    fun updateUserData(user: UserModel, isPassChanged: Boolean) {
        updateState.postValue(SignInState.Loading)
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
                    _updateState.postValue(SignInState.Success(user))
                    _updateState.postValue(SignInState.OperationSuccess)
                    return@launch
                } else {
                    _updateState.postValue(SignInState.Error(err))
                    return@launch
                }
            }
        } catch (e: EmailAlreadyExistsException) {
            _updateState.postValue(SignInState.Error(e.id))
            return
        }
    }

    fun updateStadiumData(stadium: StadiumModel) {
        _updateState.postValue(SignInState.Loading)
        try {
            viewModelScope.launch {
                repository.editStadium(stadium)
                _updateState.postValue(SignInState.OperationSuccess)
                return@launch
            }
        } catch (e: UnknownErrorException) {
            _updateState.postValue(SignInState.Error(e.id))
            return
        }
    }

    private fun validateInput(user: UserModel): Int {
        return ErrorFinder.getError(user)
    }
}