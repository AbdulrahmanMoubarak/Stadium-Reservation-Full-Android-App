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
import com.training.states.SignInState
import com.training.util.validation.ErrorFinder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject constructor(
    private val repository: LoginRepository
): ViewModel() {

    private val _loginState: MutableLiveData<SignInState<UserModel>> =
        MutableLiveData<SignInState<UserModel>>()

    val loginState: MutableLiveData<SignInState<UserModel>>
        get() = _loginState

    fun validateLogin(user: LoginModel){
        _loginState.postValue(SignInState.Loading)
        viewModelScope.launch {
            try {
                val err = validateInput(user.email, user.password)
                if(err > -1) {
                    val user = repository.getUserData(user.getFirebaseFormat())
                    if (user == null) {
                        _loginState.postValue(SignInState.Error(UnknownErrorException().id))
                        Log.d("here", "validateLogin: su")
                        return@launch
                    }
                    else{
                        _loginState.postValue(SignInState.Success(user.getViewFormat()))
                        return@launch
                    }
                }else{
                    _loginState.postValue(SignInState.Error(err))
                    return@launch
                }
            }
            catch (e: InvalidUserException){
                _loginState.postValue(SignInState.Error(e.id))
                return@launch
            }catch (e: NetworkException){
                _loginState.postValue(SignInState.Error(e.id))
                return@launch
            }catch (e: InvalidPasswordException){
                _loginState.postValue(SignInState.Error(e.id))
                return@launch
            } catch (e: UnknownErrorException){
                _loginState.postValue(SignInState.Error(e.id))
                return@launch
            }
        }
    }

    private fun validateInput(email: String, password:String):Int{
        return ErrorFinder.getError(LoginModel(email, password))
    }
}