package com.training.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.model.LoginModel
import com.training.model.UserModel
import com.training.repository.SignInRepository
import com.training.util.states.SignInState
import com.training.util.states.SignInViewModelEventState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SignInViewModel
@Inject
constructor(
    private val signInRepository: SignInRepository
) : ViewModel() {

    val scope = viewModelScope

    private val _loginState: MutableStateFlow<SignInState<UserModel>> =
        MutableStateFlow(SignInState.Filling)

    val loginState: StateFlow<SignInState<UserModel>>
        get() = _loginState

    suspend fun setStateEvent(stateEvent: SignInViewModelEventState, data: Any) {
        when (stateEvent) {
            is SignInViewModelEventState.ProceedLogin -> {
                val user: LoginModel = data as LoginModel
                signInRepository.validateLoginData(user).onEach {
                    _loginState.emit(it)
                    Log.d("here", "setStateEvent: Login ${it}")
                }.launchIn(scope)

            }
            is SignInViewModelEventState.ProceedRegister -> {
                val user: UserModel = data as UserModel
                signInRepository.validateRegisterData(user).onEach {
                    _loginState.emit(it)
                    Log.d("here", "setStateEvent: signup ${it}")
                }.launchIn(scope)
            }
        }
    }

}