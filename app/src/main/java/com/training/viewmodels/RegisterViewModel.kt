package com.training.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.exceptions.KeyAlreadyExistsException
import com.training.exceptions.EmailAlreadyExistsException
import com.training.model.StadiumModel
import com.training.model.UserModel
import com.training.repository.RegisterRepositoryInterface
import com.training.states.SignInState
import com.training.util.validation.ErrorFinder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
    @Inject constructor(var repository: RegisterRepositoryInterface): ViewModel() {

    private val _registerState: MutableLiveData<SignInState<UserModel>> =
        MutableLiveData<SignInState<UserModel>>()

    val registerState: MutableLiveData<SignInState<UserModel>>
        get() = _registerState

    fun addUser(user: UserModel) {
        _registerState.postValue(SignInState.Loading)
        viewModelScope.launch {
            addUser_suspend(user)
        }
    }

    suspend fun addUser_suspend(user: UserModel){
        try {
            var firebase_user = user.getFirebaseFormat()
            var err = validateInput(user)
            if(err > -1){
                repository.addUser(firebase_user)
                _registerState.postValue(SignInState.OperationSuccess)
                return
            }else{
                _registerState.postValue(SignInState.Error(err))
                return
            }
        } catch (e: EmailAlreadyExistsException) {
            _registerState.postValue(SignInState.Error(e.id))
            return
        }
    }

    fun addStadium(stadiumModel: StadiumModel){
        _registerState.postValue(SignInState.Loading)
        viewModelScope.launch {
            addStadiumSuspend(stadiumModel)
        }
    }

    suspend fun addStadiumSuspend(stadiumModel: StadiumModel){
        try {
            val err = validateStadium(stadiumModel)
            if(err > -1) {
                repository.RegisterStadium(stadiumModel)
                _registerState.postValue(SignInState.OperationSuccess)
                return
            }else{
                _registerState.postValue(SignInState.Error(err))
                return
            }
        }catch (e: KeyAlreadyExistsException){
            _registerState.postValue(SignInState.Error(e.id))
            return
        }
    }

    private fun validateInput(user:UserModel): Int {
        return ErrorFinder.getError(user)
    }

    fun validateStadium(stadiumModel: StadiumModel): Int{
        return ErrorFinder.getError(stadiumModel.id, stadiumModel.name, stadiumModel.location_str)
    }

    fun validateStadium(id: String, name: String, loc: String): Int{
        return ErrorFinder.getError(id, name, loc)
    }


}