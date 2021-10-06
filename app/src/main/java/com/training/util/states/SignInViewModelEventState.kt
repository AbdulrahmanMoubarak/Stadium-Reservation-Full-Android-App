package com.training.util.states

sealed class SignInViewModelEventState {
    object ProceedLogin: SignInViewModelEventState()
    object ProceedRegister: SignInViewModelEventState()
}