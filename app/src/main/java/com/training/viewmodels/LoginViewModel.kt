package com.training.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.exceptions.InvalidPasswordException
import com.training.exceptions.InvalidUserException
import com.training.exceptions.NetworkException
import com.training.exceptions.UnknownErrorException
import com.training.model.LoginModel
import com.training.model.UserModel
import com.training.repository.LoginRepository
import com.training.repository.LoginRepositoryInterface
import com.training.states.SignInState
import com.training.util.encryption.ItemHasherSHA256
import com.training.util.validation.ErrorFinder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject constructor(
    private val repository: LoginRepositoryInterface
) : ViewModel() {

    private val _loginState: MutableLiveData<SignInState<UserModel>> =
        MutableLiveData<SignInState<UserModel>>()

    val loginState: MutableLiveData<SignInState<UserModel>>
        get() = _loginState

    fun validateLogin(user: LoginModel) {
        _loginState.postValue(SignInState.Loading)
        viewModelScope.launch {
            validate_login_suspend(user)
        }
    }

    suspend fun validate_login_suspend(user: LoginModel){

        try {
            val err = validateInput(user.email, user.password)
            if (err > -1) {
                val user = repository.getUserData(user.getFirebaseFormat())
                if (user == null) {
                    _loginState.postValue(SignInState.Error(UnknownErrorException().id))
                    Log.d("here", "validateLogin: su")
                    return
                } else {
                    _loginState.postValue(SignInState.Success(user.getViewFormat()))
                    return
                }
            } else {
                _loginState.postValue(SignInState.Error(err))
                return
            }
        } catch (e: InvalidUserException) {
            _loginState.postValue(SignInState.Error(e.id))
            return
        } catch (e: NetworkException) {
            _loginState.postValue(SignInState.Error(e.id))
            return
        } catch (e: InvalidPasswordException) {
            _loginState.postValue(SignInState.Error(e.id))
            return
        } catch (e: UnknownErrorException) {
            _loginState.postValue(SignInState.Error(e.id))
            return
        }
    }

    fun updatePassword(user: UserModel, new_pass: String) {
        val err = ErrorFinder.getPasswordError(new_pass)
        if(err > -1) {
            val hashed_pass = ItemHasherSHA256.hashItem(new_pass)
            val firebase_user = user.getFirebaseFormat()
            _loginState.postValue(SignInState.Loading)
            viewModelScope.launch {
                try {
                    repository.changePassword(firebase_user.email, hashed_pass, true)
                    _loginState.postValue(SignInState.OperationSuccess)
                    return@launch
                } catch (e: UnknownErrorException) {
                    _loginState.postValue(SignInState.Error(e.id))
                    return@launch
                }
            }
        }else{
            _loginState.postValue(SignInState.Error(err))
        }
    }

    private fun validateInput(email: String, password: String): Int {
        return ErrorFinder.getError(LoginModel(email, password))
    }
}