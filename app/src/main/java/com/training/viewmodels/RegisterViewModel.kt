package com.training.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.exceptions.EmailAlreadyExistsException
import com.training.model.LoginModel
import com.training.model.UserModel
import com.training.repository.RegisterRepository
import com.training.states.SignInState
import com.training.util.validation.ErrorFinder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
    @Inject constructor(var repository: RegisterRepository): ViewModel() {

    private val _registerState: MutableLiveData<SignInState<UserModel>> =
        MutableLiveData<SignInState<UserModel>>()

    val registerState: MutableLiveData<SignInState<UserModel>>
        get() = _registerState

    fun addUser(user: UserModel) {
        _registerState.postValue(SignInState.Loading)
        var firebase_user = user.getFirebaseFormat()
        viewModelScope.launch {
            try {
                var err = validateInput(user)
                if(err > -1){
                    repository.addUser(firebase_user)
                    _registerState.postValue(SignInState.OperationSuccess)
                    return@launch
                }else{
                    _registerState.postValue(SignInState.Error(err))
                    return@launch
                }
            } catch (e: EmailAlreadyExistsException) {
                _registerState.postValue(SignInState.Error(e.id))
                return@launch
            }
        }
    }

    private fun validateInput(user:UserModel): Int {
        return ErrorFinder.getError(user)
    }
}