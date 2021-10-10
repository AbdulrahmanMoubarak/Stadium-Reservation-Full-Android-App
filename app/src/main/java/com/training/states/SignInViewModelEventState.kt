package com.training.states

sealed class SignInViewModelEventState {
    object ProceedLogin: SignInViewModelEventState()
    object ProceedRegister: SignInViewModelEventState()
}